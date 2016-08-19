package com.myee.tarot.weixin.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.myee.tarot.core.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Martin on 2016/1/20.
 */
public abstract class RedisOperation {
    private Logger logger = LoggerFactory.getLogger(RedisOperation.class);
    private final RedisTemplate redisTemplate;

    static StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

    protected RedisOperation(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    protected void hset(final String redisKey,final String filedKey,final Object object, Date date) {
        try {
            if (object != null) {
                RedisCallback callback = new RedisCallback() {
                    @Override
                    public Object doInRedis(
                            RedisConnection connection)
                            throws DataAccessException {
                            connection.hSet(redisTemplate.getKeySerializer().serialize(redisKey),
                                    redisTemplate.getHashKeySerializer().serialize(filedKey),
                                    redisTemplate.getHashValueSerializer().serialize(JSON.toJSONString(object)));
                        return null;
                    }
                };
                redisTemplate.executePipelined(callback, stringRedisSerializer);
            }
            if(date != null) {
                redisTemplate.expireAt(redisKey, date);
            }
        } catch (Throwable e) {
            logger.error("hset key:" + redisKey + " fieldkey:" + filedKey + " error:", e);
        }
    }

    protected void hsetSimple(String redisKey,Object object, Date date) {
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(redisKey, object);
            if(date != null) {
                redisTemplate.expireAt(redisKey,date);
            }
        } catch (Throwable e) {
            logger.error("hset key:" + redisKey + " value:" + object + " error:", e);
        }
    }

    protected void hdelete(String redisKey, String... hashKeys) {
        try {
            redisTemplate.opsForHash().delete(redisKey, hashKeys);
        } catch (Throwable e) {
            logger.error("hdelete key:" + redisKey + " fieldkey:" + hashKeys + " error:", e);
        }
    }

    /**
     * ValueOperations 删除某个key
     * @param redisKey
     */
    protected void hdeleteSimple(String redisKey) {
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.getOperations().delete(redisKey);
        } catch (Throwable e) {
            logger.error("hdelete key:" + redisKey + " error:", e);
        }
    }


    protected void set(String redisKey, Object object) {
        try {
            BoundValueOperations operations = redisTemplate.boundValueOps(redisKey);
            operations.set(JSON.toJSONString(object));
        } catch (Throwable e) {
            logger.error("set key:" + redisKey + "  error:", e);
        }
    }

    protected <T> T get(String redisKey, Class<T> clazz) {
        try {
            String objectJson = (String) redisTemplate.opsForValue().get(redisKey);
            if (StringUtil.isBlank(objectJson)) {
                return null;
            }
            return JSON.parseObject(objectJson, clazz);
        } catch (Throwable e) {
            logger.error("getvalue:" + redisKey + " error:", e);
        }
        return null;
    }

    protected Object getSimple(String redisKey) {
        try {
            Object objectJson = redisTemplate.opsForValue().get(redisKey);
            return objectJson;
        } catch (Throwable e) {
            logger.error("getvalue:" + redisKey + " error:", e);
        }
        return null;
    }

    protected <T> T hget(String redisKey, String filedKey, Class<T> clazz) {
        try {
            String objectJson = (String) redisTemplate.opsForHash().get(redisKey, filedKey);
            if (StringUtil.isBlank(objectJson)) {
                return null;
            }
            return JSON.parseObject(objectJson, clazz);
        } catch (Throwable e) {
            logger.error("hget key:" + redisKey + " fieldkey:" + filedKey + " error:", e);
        }
        return null;
    }

    protected Map<String, String> hgetall(String redisKey) {
        try {
            HashOperations<String, String, String> opt = redisTemplate.opsForHash();
            return opt.entries(redisKey);
        } catch (Throwable e) {
            logger.error("hgetall KEY:" + redisKey + " error:", e);
        }
        return null;
    }

    protected <T> Map<String, T> hgetall(String redisKey, final Class<T> clazz) {
        try {
            HashOperations<String, String, String> opt = redisTemplate.opsForHash();
            return Maps.transformEntries(opt.entries(redisTemplate.getStringSerializer().deserialize(redisKey.getBytes()).toString()), new Maps.EntryTransformer<String, String, T>() {
                @Override
                public T transformEntry(String key, String value) {
                    return JSON.parseObject(value, clazz);
                }
            });
        } catch (Throwable e) {
            logger.error("hget key:" + redisKey + " error:", e);
        }
        return Maps.newHashMap();
    }

    protected <T> void hsetall(String redisKey, Map<String, T> map) {
        try {
            BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(redisKey);
            boundHashOperations.putAll(Maps.transformEntries(map, new Maps.EntryTransformer<String, T, String>() {
                @Override
                public String transformEntry(String key, T value) {
                    return JSON.toJSONString(value);
                }
            }));
        } catch (Throwable e) {
            logger.error("hsetAll key:" + redisKey + " error:", e);
        }
    }

    protected void flushDB() {
        try {
                RedisCallback callback = new RedisCallback() {
                    @Override
                    public Object doInRedis(
                            RedisConnection connection)
                            throws DataAccessException {
                        connection.flushDb();
                        return null;
                    }
                };
                redisTemplate.executePipelined(callback, stringRedisSerializer);
        } catch (Throwable e) {
            logger.error("flushdb failure!", e);
        }
    }

    /**
     * 指定key追加value
     * @param key
     * @param value
     * @return
     */
    public long append(String key, String value, Date date) {
        BoundListOperations list = redisTemplate.boundListOps(key);
        long rs = list.leftPush(value);
        redisTemplate.expireAt(key,date);
        return rs;
    }

    /**
     * 获取list中value的集合
     * @param key
     * @return
     */
    public List<String> list(String key) {
        ListOperations<String,String> listOpt = redisTemplate.opsForList();
        return listOpt.range(key,0,listOpt.size(key));
    }
}
