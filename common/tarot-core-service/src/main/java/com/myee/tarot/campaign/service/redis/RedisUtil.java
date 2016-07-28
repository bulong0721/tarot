package com.myee.tarot.campaign.service.redis;

import com.alibaba.fastjson.JSON;
import com.myee.tarot.campaign.domain.bean.Pager;
import com.myee.tarot.campaign.domain.bean.RedisKeyConstants;
import com.myee.tarot.campaign.domain.bean.RedisObjInfo;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.data.redis.serializer.StringRedisSerializer;


import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * http://redis.io/commands/spop
 * redis公共类.
 * <p/>
 * redisCommons.hsetNoExpire(RedisKeyConstants.getRedisKey(RedisKeyConstants.PRODUCT_INFO,id), RedisKeyConstants.CINEMA_INFO_BASE, cinemaRedis);
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class RedisUtil {

    private RedisTemplate redisTemplate;
    /**
     * 缓存是否开启，默认开启,true开启，false不开启
     */
    private volatile boolean redisSwitch = true;
    /**
     * redis默认超时天数，单位天
     */
    private int redisTimeoutDay = 1;
    /**
     * redis默认超时时间，时分秒
     */
    private String redisTimeoutTime = "03:00:00";

    static StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();


    /**
     * 每天缓存过期时间
     */
    private String redisOrderTopCountTimeoutTime = "23:59:59";

    private static final Logger logger = LoggerFactory
            .getLogger(RedisUtil.class);

    /**
     * 用于限制每日执行次数的场景。
     * 根据传入的redisKey进行自增，若超过计数上限topCount返回false，否则返回true
     * 建议在key中增加当日日期，以确保每天的count是新的。
     *
     * @param redisKey 场景对应的redisKey
     * @param topCount 计数上限
     * @return 超过计数上限topCount返回false，否则返回true
     */
    public boolean increaseOneCountInDay(String redisKey, String hashKey, int topCount) {
        try {
            BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(redisKey);
            if (boundHashOperations.get(hashKey) == null) {
                boundHashOperations.put(hashKey, "0");
                //设置缓存过期时间
                String date = DateTimeUtils.getCurrentDateString(DateTimeUtils.DEFAULT_DATE_FORMAT_PATTERN_SHORT);
                boundHashOperations.expireAt(DateTimeUtils.createDate(date, redisOrderTopCountTimeoutTime));
            }
            //判断是否超过上限
            return boundHashOperations.increment(hashKey, 1) <= topCount;
        } catch (Exception e) {
            logger.error("increaseOneCountInDay  error:", e);
        }
        //异常情况，保证流程走下去
        return true;
    }

    /**
     * 将 key 的值设为 value ，当且仅当 key 不存在
     *
     * @param redisKey
     * @param timeout
     * @param unit
     * @return 不存在, set 成功 = true;已经存在=false
     */
    public boolean setIfAbsent(String redisKey, long timeout, TimeUnit unit) {
        boolean result = true;
        try {
            BoundValueOperations boundValueOperations = redisTemplate.boundValueOps(redisKey);

            result = boundValueOperations.setIfAbsent(redisKey);
            boundValueOperations.expire(timeout, unit);
        } catch (Exception e) {
            logger.error("setIfAbsent error:", e);
        }
        return result;
    }

    /**
     * 添加redis缓存
     *
     * @param redisKey
     * @param object
     * @date 2014年7月10日
     */
    public void set(String redisKey, Object object) {
        if (redisSwitch) {
            try {
                if (ObjectUtils.isNotEmpty(object)) {
                    BoundValueOperations operations = redisTemplate.boundValueOps(redisKey);
                    operations.set(JSON.toJSONString(object));
                    //设置缓存过期时间
                    Date newDate = DateTimeUtils.getInternalDateByDay(new Date(), redisTimeoutDay);
                    String date = DateTimeUtils.getDateString(newDate, DateTimeUtils.DEFAULT_DATE_FORMAT_PATTERN_SHORT);
                    operations.expireAt(DateTimeUtils.createDate(date, redisTimeoutTime));
                }
            } catch (Throwable e) {
                logger.error("set key:" + redisKey + "  error:", e);
            }
        }

    }


    public void batchSet(final List<RedisObjInfo> redisObjInfos) {
        if (redisSwitch) {
            try {
                if (ObjectUtils.isNotEmpty(redisObjInfos)) {
                    RedisCallback callback = new RedisCallback() {
                        @Override
                        public Object doInRedis(
                                RedisConnection connection)
                                throws DataAccessException {
                            for (RedisObjInfo redisObjInfo : redisObjInfos) {
                                connection.set(getRedisTemplate().getKeySerializer().serialize(redisObjInfo.getRedisKey()),
                                        getRedisTemplate().getValueSerializer().serialize(redisObjInfo.getValue()));
                            }
                            return null;
                        }
                    };
                    redisTemplate.executePipelined(callback, stringRedisSerializer);
                }
            } catch (Throwable e) {
                logger.error("hgetallBatchWithBean  error:", e);
            }
        }
    }


    /**
     * 添加redis缓存，设置超时时间
     *
     * @param redisKey 缓存key
     * @param object   缓存对象
     * @param date     超时时间点
     */

    public void set(String redisKey, Object object, Date date) {
        if (redisSwitch) {
            try {
                if (ObjectUtils.isNotEmpty(object)) {
                    BoundValueOperations operations = redisTemplate.boundValueOps(redisKey);
                    operations.set(JSON.toJSONString(object));
                    operations.expireAt(date);
                }
            } catch (Throwable e) {
                logger.error("set key:" + redisKey + "  error:", e);
            }
        }
    }

    /**
     * 添加redis缓存，设置超时时间
     * 此超时时间会覆盖掉前一个超时时间
     *
     * @param redisKey 缓存key
     * @param object   缓存对象
     * @param timeout  超时时间
     * @param unit     超时单位
     */
    public void set(String redisKey, Object object, long timeout, TimeUnit unit) {
        if (redisSwitch) {
            try {
                if (ObjectUtils.isNotEmpty(object)) {
                    redisTemplate.opsForValue().set(redisKey, JSON.toJSONString(object), timeout, unit);
                }
            } catch (Throwable e) {
                logger.error("set key:" + redisKey + "  error:", e);
            }
        }
    }

    /**
     * 查询相同key的元素个数
     *
     * @param redisKey 缓存key
     */
    public int getKeySize(String redisKey) {
        if (redisSwitch) {
            try {
                return redisTemplate.keys(redisKey).size();
            } catch (Throwable e) {
                logger.error("set key:" + redisKey + "  error:", e);
            }
            return 0;
        }
        return 0;
    }


    /**
     * 设置指定的KEY  多少秒后失效
     *
     * @param redisKey
     * @param seconds
     */
    public void expire(String redisKey, int seconds) {
        if (redisSwitch) {
            try {
                BoundValueOperations opt = redisTemplate.boundValueOps(redisKey);
                opt.expire(seconds, TimeUnit.SECONDS);
            } catch (Throwable e) {
                logger.error("expire key: " + redisKey + " error:", e);
            }
        }
    }

    /**
     * 添加redis的map缓存
     *
     * @param redisKey
     * @param filedKey
     * @param object
     */
    public void hset(String redisKey, String filedKey, Object object) {
        if (redisSwitch) {
            try {
                if (ObjectUtils.isNotEmpty(object)) {
                    BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(redisKey);
                    boundHashOperations.put(filedKey, JSON.toJSONString(object));
                    //设置缓存过期时间
                    Date newDate = DateTimeUtils.getInternalDateByDay(new Date(), redisTimeoutDay);
                    String date = DateTimeUtils.getDateString(newDate, DateTimeUtils.DEFAULT_DATE_FORMAT_PATTERN_SHORT);
                    boundHashOperations.expireAt(DateTimeUtils.createDate(date, redisTimeoutTime));
                }
            } catch (Throwable e) {
                logger.error("hset key:" + redisKey + " fieldkey:" + filedKey + " error:", e);
            }
        }
    }

    /**
     * 添加redis的list缓存
     *
     * @param redisKey
     * @param object
     */
    public void leftPushList(String redisKey, Object object,long timeout,TimeUnit unit) {
        if (redisSwitch) {
            try {
                if (ObjectUtils.isNotEmpty(object)) {
                    BoundListOperations boundListOperations = redisTemplate.boundListOps(redisKey);
                    boundListOperations.leftPush(JSON.toJSONString(object));
                    boundListOperations.expire(timeout, unit);
                }
            } catch (Throwable e) {
                logger.error("hset key:" + redisKey + " error:", e);
            }
        }
    }


    public void hdel(String redisKey, String filedKey) {
        if (redisSwitch) {
            try {
                BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(redisKey);
                boundHashOperations.delete(filedKey);
            } catch (Throwable e) {
                logger.error("hdel key:" + redisKey + " fieldkey:" + filedKey + " error:", e);
            }
        }
    }

    /**
     * 从集合中删除
     * @param redisKey
     * @param filedKey
     */
    public void deleteSet(String redisKey, String filedKey) {
        if (redisSwitch) {
            try {
                BoundSetOperations boundSetOperations = redisTemplate.boundSetOps(redisKey);
                boundSetOperations.remove(filedKey);
            } catch (Throwable e) {
                logger.error("hdel key:" + redisKey + " fieldkey:" + filedKey + " error:", e);
            }
        }
    }

    /**
     * 添加默认失效时间的缓存
     *
     * @param redisKey
     * @param filedKey
     * @param object
     */
    public void hsetJudgeExpire(String redisKey, String filedKey, Object object) {
        if (redisSwitch) {
            try {
                BoundHashOperations<String, String, String> boundHashOperations = redisTemplate.boundHashOps(redisKey);
                boundHashOperations.put(filedKey, JSON.toJSONString(object));
                if (boundHashOperations.getExpire() < 0) {
                    boundHashOperations.expire(RedisKeyConstants.DEFAULT_ONE_DAY_TIMEOUT, TimeUnit.SECONDS);
                }
            } catch (Throwable e) {
                logger.error("hsetJudgeExpire key:" + redisKey + " fieldkey:" + filedKey + " error:", e);
            }
        }
    }

    /**
     * 设置缓存不过期的REDIS
     *
     * @param redisKey
     * @param hashKey
     * @param object
     */
    public void hsetNoExpire(String redisKey, String hashKey, Object object) {
        if (redisSwitch) {
            try {
                BoundHashOperations<String, String, String> boundHashOperations = redisTemplate.boundHashOps(redisKey);
                boundHashOperations.put(hashKey, JSON.toJSONString(object));
            } catch (Throwable e) {
                logger.error("hsetNoExpire error:", e);
            }
        }
    }


    /**
     * 添加redis的map缓存，设置超时时间
     *
     * @param redisKey 缓存key
     * @param filedKey
     * @param object   缓存对象
     * @param date     超时时间点
     */
    public void hset(String redisKey, String filedKey, Object object, Date date) {
        if (redisSwitch) {
            try {
                if (ObjectUtils.isNotEmpty(object)) {
                    BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(redisKey);
                    boundHashOperations.put(filedKey, JSON.toJSONString(object));
                    boundHashOperations.expireAt(date);
                }
            } catch (Throwable e) {
                logger.error("hset key:" + redisKey + " fieldkey:" + filedKey + " error:", e);
            }
        }
    }

    /**
     * 添加redis的map缓存，设置超时时间
     * 此超时时间会覆盖掉前一个超时时间
     *
     * @param redisKey 缓存key
     * @param filedKey
     * @param object   缓存对象
     * @param timeout  超时时间
     * @param unit     超时单位
     */
    public void hset(String redisKey, String filedKey, Object object, long timeout, TimeUnit unit) {
        if (redisSwitch) {
            try {
                if (ObjectUtils.isNotEmpty(object)) {
                    BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(redisKey);
                    boundHashOperations.put(filedKey, JSON.toJSONString(object));
                    boundHashOperations.expire(timeout, unit);
                }
            } catch (Throwable e) {
                logger.error("hset key:" + redisKey + " fieldkey:" + filedKey + " error:", e);
            }
        }
    }

    /**
     * 添加redis的set缓存，设置超时时间
     * 此超时时间会覆盖掉前一个超时时间
     *
     * @param redisKey 缓存key
     * @param filedKey
     * @param timeout  超时时间
     * @param unit     超时单位
     */
    public void addSet(String redisKey, String filedKey, long timeout, TimeUnit unit) {
        if (redisSwitch) {
            try {
                BoundSetOperations boundSetOperations = redisTemplate.boundSetOps(redisKey);
                boundSetOperations.add(filedKey);
                boundSetOperations.expire(timeout, unit);
            } catch (Throwable e) {
                logger.error("hset key:" + redisKey + " fieldkey:" + filedKey + " error:", e);
            }
        }
    }

    /**
     * 保存缓存
     *
     * @param redisKey
     * @param map
     * @param timeout
     * @param unit
     */
    public void hsetAll(String redisKey, Map<String, String> map, long timeout, TimeUnit unit) {
        if (redisSwitch) {
            try {
                BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(redisKey);
                boundHashOperations.putAll(map);
                boundHashOperations.expire(timeout, unit);
            } catch (Throwable e) {
                logger.error("hsetAll key:" + redisKey + " error:", e);
            }
        }
    }

    /**
     * 获取redis非list缓存
     *
     * @param redisKey
     * @param clazz
     * @return
     */
    public <T> T get(String redisKey, Class<T> clazz) {
        if (redisSwitch) {
            try {
                String objectJson = (String) redisTemplate.opsForValue().get(redisKey);
                if (StringUtils.isBlank(objectJson)) {
                    return null;
            }
                return JSON.parseObject(objectJson, clazz);
            } catch (Throwable e) {
                logger.error("getvalue:" + redisKey + " error:", e);
            }
        }
        return null;
    }

    /**
     * 获取redis非list缓存
     *
     * @param redisKey
     * @return
     */
    public String getString(String redisKey) {
        if (redisSwitch) {
            try {
                String objectJson = (String) redisTemplate.opsForValue().get(redisKey);
                if (StringUtils.isBlank(objectJson)) {
                    return null;
                }
                return objectJson;
            } catch (Throwable e) {
                logger.error("getString:" + redisKey + " error:", e);
            }
        }
        return null;
    }

    /**
     * 获取redis的list缓存
     *
     * @param redisKey
     * @param clazz
     * @return
     * @author jijun
     * @date 2014年7月10日
     */
    public <T> List<T> getList(String redisKey, Class<T> clazz) {
        if (redisSwitch) {
            try {
                String objectJson = (String) redisTemplate.opsForValue().get(redisKey);
                if (StringUtils.isBlank(objectJson)) {
                    return new ArrayList<T>();
                }
                return JSON.parseArray(objectJson, clazz);
            } catch (Throwable e) {
                logger.error("getList:" + redisKey + " error:", e);
            }
        }

        return new ArrayList<T>();
    }

    /**
     * 获取redis的map缓存
     *
     * @param redisKey
     * @param filedKey
     * @param clazz
     * @return
     * @author jijun
     * @date 2014年7月10日
     */
    public <T> T hget(String redisKey, String filedKey, Class<T> clazz) {
        if (redisSwitch) {
            try {
                String objectJson = (String) redisTemplate.opsForHash().get(redisKey, filedKey);
                if (StringUtils.isBlank(objectJson)) {
                    return null;
                }
                return JSON.parseObject(objectJson, clazz);
            } catch (Throwable e) {
                logger.error("hget key:" + redisKey + " fieldkey:" + filedKey + " error:", e);
            }
        }
        return null;
    }

    /**
     * 获取redis中map的list缓存
     *
     * @param redisKey
     * @param filedKey
     * @param clazz
     * @return
     * @author jijun
     * @date 2014年7月10日
     */
    public <T> List<T> hgetList(String redisKey, String filedKey, Class<T> clazz) {
        if (redisSwitch) {
            try {
                String objectJson = (String) redisTemplate.opsForHash().get(redisKey, filedKey);
                if (StringUtils.isBlank(objectJson)) {
                    return new ArrayList<T>();
                }
                return JSON.parseArray(objectJson, clazz);
            } catch (Throwable e) {
                logger.error("hgetList key:" + redisKey + " fieldkey:" + filedKey + " error:", e);
            }
        }
        return new ArrayList<T>();
    }

    /**
     * 删除redis某个key的缓存
     *
     * @param redisKey
     * @author jijun
     * @date 2014年7月10日
     */
    public void delete(String redisKey) {
        if (redisSwitch) {
            try {
                redisTemplate.delete(redisKey);
            } catch (Throwable e) {
                logger.error("delete KEY:" + redisKey + " error:", e);
            }
        }
    }

    /**
     * 删除redis中map的key
     *
     * @param redisKey
     * @param hashKeys
     * @date 2014年7月10日
     */
    public void hdelete(String redisKey, String... hashKeys) {
        if (redisSwitch) {
            try {
                redisTemplate.opsForHash().delete(redisKey, hashKeys);
            } catch (Throwable e) {
                logger.error("hdelete key:" + redisKey + " fieldkey:" + hashKeys + " error:", e);
            }
        }
    }


    /**
     * RPUSH key value [value ...]
     * <p/>
     * 将一个或多个值 value 插入到列表 key 的表尾(最右边)。
     * <p/>
     * 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表尾：比如对一个空列表 mylist 执行 RPUSH mylist a b c ，得出的结果列表为 a b c ，等同于执行命令 RPUSH mylist a 、 RPUSH mylist b 、 RPUSH mylist c 。
     * <p/>
     * 如果 key 不存在，一个空列表会被创建并执行 RPUSH 操作。
     * <p/>
     * 当 key 存在但不是列表类型时，返回一个错误。
     *
     * @param redisKey
     * @param obj
     * @param second
     * @return
     */
    public Long rpush(String redisKey, long second, String... obj) {
        if (redisSwitch) {
            try {
                BoundListOperations<String, String> opt = redisTemplate.boundListOps(redisKey);
                Long result = opt.rightPushAll(obj);
                opt.expire(second, TimeUnit.SECONDS);
                return result;
            } catch (Throwable e) {
                logger.error("rightPushAll key:" + redisKey + " error:", e);
            }
        }
        return null;
    }

    /**
     * LPUSH key value [value ...]
     * <p/>
     * 将一个或多个值 value 插入到列表 key 的表头
     * <p/>
     * 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表头： 比如说，对空列表 mylist 执行命令 LPUSH mylist a b c ，列表的值将是 c b a ，这等同于原子性地执行 LPUSH mylist a 、 LPUSH mylist b 和 LPUSH mylist c 三个命令。
     * <p/>
     * 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。
     *
     * @param redisKey
     * @param second
     * @param obj
     * @return
     */
    public Long lpush(String redisKey, long second, String... obj) {
        if (redisSwitch) {
            try {
                BoundListOperations<String, String> opt = redisTemplate.boundListOps(redisKey);
                //CollectionUtils.reverseArray(obj);
                Long result = opt.leftPushAll(obj);
                if (second > 0)
                    opt.expire(second, TimeUnit.SECONDS);
                return result;
            } catch (Throwable e) {
                logger.error("leftPushAll key:" + redisKey + " error:", e);
            }
        }
        return null;
    }


    /**
     * List（列表） »
     * LREM
     * LREM key count value
     * <p/>
     * 根据参数 count 的值，移除列表中与参数 value 相等的元素。
     * <p/>
     * count 的值可以是以下几种：
     * <p/>
     * count > 0 : 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count 。
     * count < 0 : 从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值。
     * count = 0 : 移除表中所有与 value 相等的值。
     * 可用版本：
     * >= 1.0.0
     * 时间复杂度：
     * O(N)， N 为列表的长度。
     * 返回值：
     * 被移除元素的数量。
     * 因为不存在的 key 被视作空表(empty list)，所以当 key 不存在时， LREM 命令总是返回 0 。
     *
     * @param key
     * @param obj
     * @return
     */
    public void lrem(String key, String... obj) {
        if (redisSwitch) {
            try {
                BoundListOperations<String, String> opt = redisTemplate.boundListOps(key);
                for (String objStr : obj) {
                    Long result = opt.remove(0, objStr);
                }
            } catch (Throwable e) {
                logger.error("LIST lrem KEY:" + key + " error:", e);
            }
        }
    }

    /**
     * LTRIM key start stop
     * <p/>
     * 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。
     * <p/>
     * 举个例子，执行命令 LTRIM list 0 2 ，表示只保留列表 list 的前三个元素，其余元素全部删除。
     * <p/>
     * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
     * <p/>
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     * <p/>
     * 当 key 不是列表类型时，返回一个错误。
     *
     * @param key
     * @param size
     */
    public void ltrim(String key, Integer size) {
        if (redisSwitch) {
            try {
                BoundListOperations<String, String> opt = redisTemplate.boundListOps(key);
                opt.trim(0l, Long.parseLong((size - 1) + ""));
            } catch (Throwable e) {
                logger.error("LIST ltrim KEY:" + key + " error:", e);
                logger.error("LIST  error:", e);
            }
        }
    }

    /**
     * LLEN key
     * <p/>
     * 返回列表 key 的长度。
     * <p/>
     * 如果 key 不存在，则 key 被解释为一个空列表，返回 0 .
     * <p/>
     * 如果 key 不是列表类型，返回一个错误。
     *
     * @param key
     * @return
     */
    public Long llen(String key) {
        if (redisSwitch) {
            try {
                ListOperations opt = redisTemplate.opsForList();
                return opt.size(key);
            } catch (Throwable e) {
                logger.error("LIST llen KEY:" + key + " error:", e);
            }
        }
        return null;
    }

    /**
     * RANGE key start stop
     * <p/>
     * 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定。
     * <p/>
     * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
     * <p/>
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     * <p/>
     * 注意LRANGE命令和编程语言区间函数的区别
     * <p/>
     * 假如你有一个包含一百个元素的列表，对该列表执行 LRANGE list 0 10 ，结果是一个包含11个元素的列表，这表明 stop 下标也在 LRANGE 命令的取值范围之内(闭区间)，这和某些语言的区间函数可能不一致，比如Ruby的 Range.new 、 Array#slice 和Python的 range() 函数。
     * <p/>
     * 超出范围的下标
     * <p/>
     * 超出范围的下标值不会引起错误。
     * <p/>
     * 如果 start 下标比列表的最大下标 end ( LLEN list 减去 1 )还要大，那么 LRANGE 返回一个空列表。
     * <p/>
     * 如果 stop 下标比 end 下标还要大，Redis将 stop 的值设置为 end 。
     *
     * @param key
     * @param pager
     * @return
     */
    public List lrandge(String key, Pager pager) {
        if (redisSwitch) {
            try {
                long start = 0;
                long end = -1;
                if (pager != null) {
                    start = (pager.getPageIndex() - 1) * pager.getPageSize();
                    end = start + pager.getPageSize() - 1;
                }
                ListOperations opt = redisTemplate.opsForList();
                return opt.range(key, start, end);
            } catch (Throwable e) {
                logger.error("lrandge KEY" + key + "error:", e);
            }
        }
        return null;
    }


    //============================================以下定义 SortedSet（有序集合）的操作方法==========================
    public static final double POSITIVE_INFINITY = 1.0 / 0.0;

    /**
     * A constant holding the negative infinity of type
     * {@code double}. It is equal to the value returned by
     * {@code Double.longBitsToDouble(0xfff0000000000000L)}.
     */
    public static final double NEGATIVE_INFINITY = -1.0 / 0.0;

    /**
     * ZCARD key
     * <p/>
     * 返回有序集 key 的基数。
     *
     * @param redisKey
     * @return
     */
    public long zcard(String redisKey) {
        if (redisSwitch) {
            try {
                ZSetOperations<String, String> opt = redisTemplate.opsForZSet();
                return opt.zCard(redisKey);
            } catch (Throwable e) {
                logger.error("zcard KEY" + redisKey + " error:", e);
            }
        }
        return 0;
    }

    /**
     * 移除有序集 key 中的一个或多个成员，不存在的成员将被忽略。
     * <p/>
     * 当 key 存在但不是有序集类型时，返回一个错误。
     *
     * @param key
     * @param member
     * @return
     */
    public Long zrem(String key, String member) {
        if (redisSwitch) {
            try {
                BoundZSetOperations<String, String> opt = redisTemplate.boundZSetOps(key);
                Long result = opt.remove(member);
                return result;
            } catch (Throwable e) {
                logger.error("zrem KEY" + key + "member:" + member + " error:", e);
            }
        }
        return null;
    }

    /**
     * @param key
     * @param tuples
     * @return
     */
    public Long zadd(String key, Set<TypedTuple<String>> tuples) {
        return this.zadd(key, tuples, RedisKeyConstants.DEFAULT_NEVER_TIMEOUT);
    }

    /**
     * @param key
     * @param tuples
     * @param timeSeconds
     * @return
     */
    public Long zadd(String key, Set<TypedTuple<String>> tuples, int timeSeconds) {
        if (redisSwitch) {
            try {
                BoundZSetOperations<String, String> opt = redisTemplate.boundZSetOps(key);
                Long result = opt.add(tuples);

                if (timeSeconds > 0) {
                    //设置缓存过期时间
                    Date newDate = DateTimeUtils.getInternalDateByDay(new Date(), redisTimeoutDay);
                    String date = DateTimeUtils.getDateString(newDate, DateTimeUtils.DEFAULT_DATE_FORMAT_PATTERN_SHORT);
                    opt.expireAt(DateTimeUtils.createDate(date, redisTimeoutTime));
                }

                return result;
            } catch (Throwable e) {
                logger.error("zadd key:" + key + " error:", e);
            }
        }
        return null;
    }


    /**
     * @param key
     * @param value
     * @param valueSrore
     */
    public void zaddOne(String key, String value, Double valueSrore) {
        zaddOne(key, value, valueSrore, RedisKeyConstants.DEFAULT_NEVER_TIMEOUT);
    }

    /**
     * @param key
     * @param value
     * @param valueSrore
     * @param timeSeconds
     */
    public void zaddOne(String key, String value, Double valueSrore, int timeSeconds) {
        if (redisSwitch) {
            try {
                BoundZSetOperations<String, String> opt = redisTemplate.boundZSetOps(key);
                Boolean result = opt.add(value, valueSrore);

                if (result) {
                    if (timeSeconds > 0) {
                        //设置缓存过期时间
                        Date newDate = DateTimeUtils.getInternalDateByDay(new Date(), redisTimeoutDay);
                        String date = DateTimeUtils.getDateString(newDate, DateTimeUtils.DEFAULT_DATE_FORMAT_PATTERN_SHORT);
                        opt.expireAt(DateTimeUtils.createDate(date, redisTimeoutTime));
                    }
                }
            } catch (Throwable e) {
                logger.error("zadd key:" + key + " value:" + value + " score:" + valueSrore + " error:", e);
            }
        }
    }


    /**
     * @param redisKey
     * @param pager
     * @return
     */
    public Set<TypedTuple<String>> zrange(String redisKey, Pager pager) {
        if (redisSwitch) {
            try {
                long start = 0;
                long end = -1;
                if (pager != null) {
                    start = (pager.getPageIndex() - 1) * pager.getPageSize();
                    end = start + pager.getPageSize() - 1;
                }
                ZSetOperations<String, String> opt = redisTemplate.opsForZSet();
                return opt.rangeWithScores(redisKey, start, end);
            } catch (Throwable e) {
                logger.error("zrange error:", e);
            }
        }
        return null;
    }

    public Set<TypedTuple<String>> rangeByScoreWithScores(String redisKey, Pager pager, Double min, Double max) {
        if (redisSwitch) {
            try {
                long start = 0;
                long end = -1;
                if (pager != null) {
                    start = (pager.getPageIndex() - 1) * pager.getPageSize();
                    end = start + pager.getPageSize() - 1;
                }
                ZSetOperations<String, String> opt = redisTemplate.opsForZSet();
                return opt.rangeByScoreWithScores(redisKey, (min == null) ? NEGATIVE_INFINITY : min,
                        (max == null) ? POSITIVE_INFINITY : max, start, end);
            } catch (Throwable e) {
                logger.error("rangeByScoreWithScores error:", e);
            }
        }
        return null;
    }

    /**
     * ZREVRANGE key start stop [WITHSCORES]
     * <p/>
     * 返回有序集 key 中，指定区间内的成员。
     * <p/>
     * 其中成员的位置按 score 值递减(从大到小)来排列。
     * 具有相同 score 值的成员按字典序的逆序(reverse lexicographical order)排列。
     *
     * @param redisKey
     * @param pager
     * @return
     */
    public Set<TypedTuple<String>> zrevrange(String redisKey, Pager pager) {
        if (redisSwitch) {
            try {
                long start = 0;
                long end = -1;
                if (pager != null) {
                    start = (pager.getPageIndex() - 1) * pager.getPageSize();
                    end = start + pager.getPageSize() - 1;
                }
                ZSetOperations<String, String> opt = redisTemplate.opsForZSet();
                return opt.reverseRangeWithScores(redisKey, start, end);
            } catch (Throwable e) {
                logger.error("zrevrange KEY:" + redisKey + " error:", e);
            }
        }
        return null;
    }



    public Set<TypedTuple<String>> zrevrangeByMaxOrder(String redisKey, long maxOrder,long pageSize) {
        if (redisSwitch) {
            try {
                long start = maxOrder;
                long end = maxOrder + pageSize -1;
                ZSetOperations<String, String> opt = redisTemplate.opsForZSet();
                return opt.reverseRangeWithScores(redisKey, start, end);
            } catch (Throwable e) {
                logger.error("zrevrange KEY:" + redisKey + " error:", e);
            }
        }
        return null;
    }

    /**
     * @param redisKey
     * @param pageSize
     * @param min
     * @param max
     * @return
     */
    public Set<TypedTuple<String>> zrevrangeByScore(String redisKey, Long pageSize, Double min, Double max) {
        if (redisSwitch) {
            try {
                long start = 0;

                ZSetOperations<String, String> opt = redisTemplate.opsForZSet();
                return opt.reverseRangeByScoreWithScores(redisKey,
                        (min == null) ? NEGATIVE_INFINITY : min,
                        (max == null) ? POSITIVE_INFINITY : max, start, pageSize);
            } catch (Throwable e) {
                logger.error("zrevrangeByScore KEY:" + redisKey + " error:", e);
            }
        }
        return null;
    }


    /**
     * ZREVRANGEBYSCORE key max min [WITHSCORES] [LIMIT offset count]
     * <p/>
     * 返回有序集 key 中， score 值介于 max 和 min 之间(默认包括等于 max 或 min )的所有的成员。有序集成员按 score 值递减(从大到小)的次序排列。
     * <p/>
     * 具有相同 score 值的成员按字典序的逆序(reverse lexicographical order )排列。
     * <p/>
     * 除了成员按 score 值递减的次序排列这一点外， ZREVRANGEBYSCORE 命令的其他方面和 ZRANGEBYSCORE 命令一样。
     *
     * @param redisKey
     * @param pager
     * @param min
     * @param max
     * @return
     */
    public Set<TypedTuple<String>> zrevrangeByScore(String redisKey, Pager pager, Double min, Double max) {
        if (redisSwitch) {
            try {
                long start = 0;

                ZSetOperations<String, String> opt = redisTemplate.opsForZSet();
                return opt.reverseRangeByScoreWithScores(redisKey,
                        (min == null) ? NEGATIVE_INFINITY : min,
                        (max == null) ? POSITIVE_INFINITY : max, start, pager.getPageSize());
            } catch (Throwable e) {
                logger.error("zrevrangeByScore KEY:" + redisKey + " error:", e);
            }
        }
        return null;
    }


    /**
     * ZREVRANGEBYSCORE key max min [WITHSCORES] [LIMIT offset count]
     * <p/>
     * 返回有序集 key 中， score 值介于 max 和 min 之间(默认包括等于 max 或 min )的所有的成员。有序集成员按 score 值递减(从大到小)的次序排列。
     * <p/>
     * 具有相同 score 值的成员按字典序的逆序(reverse lexicographical order )排列。
     * <p/>
     * 除了成员按 score 值递减的次序排列这一点外， ZREVRANGEBYSCORE 命令的其他方面和 ZRANGEBYSCORE 命令一样。
     *
     * @param redisKey
     * @param min
     * @param max
     * @return
     */
    public Set<TypedTuple<String>> zrevrangeByScore(String redisKey, Double min, Double max) {
        if (redisSwitch) {
            try {

                ZSetOperations<String, String> opt = redisTemplate.opsForZSet();
                return opt.reverseRangeByScoreWithScores(redisKey,
                        (min == null) ? NEGATIVE_INFINITY : min,
                        (max == null) ? POSITIVE_INFINITY : max);
            } catch (Throwable e) {
                logger.error("zrevrangeByScore KEY:" + redisKey + " error:", e);
            }
        }
        return null;
    }


    /**
     * 获取MAP 对应HSETALL(KEY)
     * redisCommons.hsetNoExpire(RedisKeyConstants.getRedisKey(RedisKeyConstants.PRODUCT_INFO,id), RedisKeyConstants.CINEMA_INFO_BASE, cinemaRedis);
     * INFO--BASE,EXTENDS
     *
     * @param redisKey INFO
     * @return BASE, EXTENDS
     */
    public Map<String, String> hgetall(String redisKey) {
        if (redisSwitch) {
            try {
                HashOperations<String, String, String> opt = redisTemplate.opsForHash();
                return opt.entries(redisKey);
            } catch (Throwable e) {
                logger.error("hgetall KEY:" + redisKey + " error:", e);
            }
        }
        return null;
    }

    /**
     * 根据 值得redisKEY  返回对象序列号结果
     * 如 ：
     * INFO* __BASE---NAME,EXTENDS
     * <p/>
     * 传入INFO0,INFO1  返回 MAP< KEY0: BASE|NAME|EXTENDS       KEY1: BASE|NAME|EXTENDS
     *
     * @param redisKeys
     * @return
     */
    public List<Map<String, String>> hgetallBatch(final List<String> redisKeys) {
        if (redisSwitch) {
            try {
                RedisCallback callback = new RedisCallback() {
                    @Override
                    public Object doInRedis(
                            RedisConnection connection)
                            throws DataAccessException {
                        for (String redisKey : redisKeys) {
                            connection.hGetAll(redisKey.getBytes());
                        }
                        return null;
                    }
                };
                return (List<Map<String, String>>) redisTemplate.executePipelined(callback, stringRedisSerializer);
            } catch (Throwable e) {
                logger.error("hgetallBatch KEY:" + ArrayUtils.toString(redisKeys) + " error:", e);
            }
        }
        return null;
    }

    /**
     * 批量加载缓存内容
     *
     * @param redisKey
     * @param hashKeys
     * @return
     */
    public List<String> hgetBatch(final String redisKey, final List<String> hashKeys) {
        if (redisSwitch) {
            try {
                HashOperations<String, String, String> opt = redisTemplate.opsForHash();
                return opt.multiGet(redisKey, hashKeys);
                /*
                RedisCallback callback = new RedisCallback() {
                    @Override
                    public Object doInRedis(
                            RedisConnection connection)
                            throws DataAccessException {
                        byte[][] result = new byte[hashKeys.size()][];
                        int i = 0;
                        for (String hashKey : hashKeys) {
                            result[i++] = hashKey.getBytes();
                        }
                        connection.hMGet(redisKey.getBytes(), result);
                        return null;
                    }
                };
                return (List<String>) redisTemplate.executePipelined(callback, stringRedisSerializer);*/
            } catch (Throwable e) {
                logger.error("hgetBatch error:", e);
            }
        }
        return null;
    }

    /**
     * 批量加载缓存内容
     *
     * @param redisKeys
     * @param hkey
     * @return
     */
    public List<Map<String, String>> hgetBatch(final List<String> redisKeys, final String hkey) {
        if (redisSwitch) {
            try {
                RedisCallback callback = new RedisCallback() {
                    @Override
                    public Object doInRedis(
                            RedisConnection connection)
                            throws DataAccessException {
                        for (String redisKey : redisKeys) {
                            //key is English.All charset ok.
                            connection.hGet(redisKey.getBytes(), hkey.getBytes());
                        }
                        return null;
                    }
                };
                return (List<Map<String, String>>) redisTemplate.executePipelined(callback, stringRedisSerializer);
            } catch (Throwable e) {
                logger.error("hgetall error:", e);
            }
        }
        return null;
    }

    /**
     * SADD key member [member ...]
     * <p/>
     * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。
     * <p/>
     * 假如 key 不存在，则创建一个只包含 member 元素作成员的集合。
     * <p/>
     * 当 key 不是集合类型时，返回一个错误。
     * RedisSetCommands
     * <p/>
     * <p/>
     * redis的set数据结构的数据增加。
     *
     * @param key    redis key
     * @param values values
     * @return 增加的记录数
     */
    public Long sAdd(String key, List<String> values) {
        if (redisSwitch) {
            try {
                BoundSetOperations boundSetOperations = redisTemplate.boundSetOps(key);
                Long result = boundSetOperations.add(values.toArray());
                //设置缓存过期时间
                Date newDate = DateTimeUtils.getInternalDateByDay(new Date(), redisTimeoutDay);
                String date = DateTimeUtils.getDateString(newDate, DateTimeUtils.DEFAULT_DATE_FORMAT_PATTERN_SHORT);
                boundSetOperations.expireAt(DateTimeUtils.createDate(date, redisTimeoutTime));
                return result;
            } catch (Throwable e) {
                logger.error("sAdd KEY:" + key + " error:", e);
            }
        }
        return 0L;
    }

    /*public Long sAdd(String key, List<String> values, long second) {
        if (redisSwitch) {
            try {
                BoundSetOperations boundSetOperations = redisTemplate.boundSetOps(key);
                Long result = boundSetOperations.add(values.toArray());
                if (second > 0) {
                    boundSetOperations.expire(second, TimeUnit.SECONDS);
                }
                return result;
            } catch (Throwable e) {
                logger.error("sAdd KEY:" + key + " error:", e);
            }
        }
        return 0L;
    }*/

    /*public Long sAdd(String key, String values, long second) {
        if (redisSwitch) {
            try {
                BoundSetOperations boundSetOperations = redisTemplate.boundSetOps(key);
                Long result = boundSetOperations.add(values);
                if (second > 0) {
                    //设置缓存过期时间
                    Date newDate = DateTimeUtils.getInternalDateByDay(new Date(), redisTimeoutDay);
                    String date = DateTimeUtils.getDateString(newDate, DateTimeUtils.DEFAULT_DATE_FORMAT_PATTERN_SHORT);
                    boundSetOperations.expireAt(DateTimeUtils.createDate(date, redisTimeoutTime));
                }
                return result;
            } catch (Throwable e) {
                logger.error("sAdd KEY:" + key + " value:" + values + " error:", e);
            }
        }
        return 0L;
    }*/

    public Long sAdd(String key, String values) {
        if (redisSwitch) {
            try {
                BoundSetOperations boundSetOperations = redisTemplate.boundSetOps(key);
                Long result = boundSetOperations.add(values);
                //设置缓存过期时间
                Date newDate = DateTimeUtils.getInternalDateByDay(new Date(), redisTimeoutDay);
                String date = DateTimeUtils.getDateString(newDate, DateTimeUtils.DEFAULT_DATE_FORMAT_PATTERN_SHORT);
                boundSetOperations.expireAt(DateTimeUtils.createDate(date, redisTimeoutTime));
                return result;
            } catch (Throwable e) {
                logger.error("sAdd KEY:" + key + " value:" + values + " error:", e);
            }
        }
        return 0L;
    }

    public void sRem(String key, String member) {
        if (redisSwitch) {
            try {
                BoundSetOperations boundSetOperations = redisTemplate.boundSetOps(key);
                boundSetOperations.remove(member);
            } catch (Throwable e) {
                logger.error("sRem KEY:" + key + " value:" + member + " error:", e);
            }
        }
    }

    /**
     * Get size of set at {@code key}.
     * redis set的记录数。
     *
     * @param key redis key
     * @return set的记录数
     */
    public Long sCard(String key) {
        if (redisSwitch) {
            try {
                return redisTemplate.boundSetOps(key).size();
            } catch (Throwable e) {
                logger.error("sCard KEY:" + key + "  error:", e);
            }
        }
        return 0L;
    }

    /**
     * Removes and returns a random element from the set value stored at key.
     * 弹出redis set的一条记录。
     * http://redis.io/commands/spop
     *
     * @param key redis key
     * @return set value
     */
    public String sPop(String key) {
        if (redisSwitch) {
            try {
                return (String) redisTemplate.boundSetOps(key).pop();
            } catch (Throwable e) {
                logger.error("sPop error:", e);
            }
        }
        return null;
    }

    /**
     * Check if set at {@code key} contains {@code value}.
     *
     * @param key
     * @param value
     * @return
     */
    public boolean sIsMember(String key, String value) {
        if (redisSwitch) {
            try {
                return redisTemplate.boundSetOps(key).isMember(value);
            } catch (Throwable e) {
                logger.error("sIsMember KEY:" + key + " member:" + value + "  error:", e);
            }
        }
        return true;
    }

    /**
     * 求差集
     *
     * @param key      源key
     * @param otherKey 其他key
     * @param destKey  目标key
     */
    public void sdiffAndStore(String key, String otherKey, String destKey) {
        if (redisSwitch) {
            try {
                redisTemplate.boundSetOps(key).diffAndStore(otherKey, destKey);
            } catch (Throwable e) {
                logger.error("sdiffAndStore error:", e);
            }
        }
    }

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean isRedisSwitch() {
        return redisSwitch;
    }

    public void setRedisSwitch(boolean redisSwitch) {
        this.redisSwitch = redisSwitch;
    }

    public int getRedisTimeoutDay() {
        return redisTimeoutDay;
    }

    public void setRedisTimeoutDay(int redisTimeoutDay) {
        this.redisTimeoutDay = redisTimeoutDay;
    }

    public String getRedisTimeoutTime() {
        return redisTimeoutTime;
    }

    public void setRedisTimeoutTime(String redisTimeoutTime) {
        this.redisTimeoutTime = redisTimeoutTime;
    }


    /**
     * Hash（哈希表） »
     * HINCRBY
     * HINCRBY key field increment
     * <p/>
     * 为哈希表 key 中的域 field 的值加上增量 increment 。
     * <p/>
     * 增量也可以为负数，相当于对给定域进行减法操作。
     * <p/>
     * 如果 key 不存在，一个新的哈希表被创建并执行 HINCRBY 命令。
     * <p/>
     * 如果域 field 不存在，那么在执行命令前，域的值被初始化为 0 。
     * <p/>
     * 对一个储存字符串值的域 field 执行 HINCRBY 命令将造成一个错误。
     * <p/>
     * 本操作的值被限制在 64 位(bit)有符号数字表示之内。
     *
     * @param key       源key
     * @param fileKey   FILDLEY
     * @param increment 为哈希表 key 中的域 field 的值加上增量 increment 。
     * @return 返回最新的参数值
     */
    public void hincrby(String key, String fileKey, Long increment) {
        if (redisSwitch) {
            try {
                BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(key);

                boundHashOperations.increment(fileKey, increment);
                // boundHashOperations.expire(100, TimeUnit.SECONDS);
                // return this.hget(key, fileKey, Long.class);
            } catch (Throwable e) {
                logger.error("setHincrby error:", e);
            }
        }
        // return 0L;
    }

    public void hincrby(String key, String fileKey, Integer increment) {
        if (redisSwitch) {
            try {
                BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(key);

                boundHashOperations.increment(fileKey, increment);
                // boundHashOperations.expire(100, TimeUnit.SECONDS);
                // return this.hget(key, fileKey, Long.class);
            } catch (Throwable e) {
                logger.error("setHincrby key" + key + " file:" + fileKey + " error:", e);
            }
        }
        // return 0L;
    }

    /**
     * Hash（哈希表） »
     * HLEN
     * HLEN key
     * <p/>
     * 返回哈希表 key 中域的数量。
     * <p/>
     * 时间复杂度：
     * O(1)
     * 返回值：
     * 哈希表中域的数量。
     * 当 key 不存在时，返回 0 。
     *
     * @param key
     * @return
     */
    public long hlen(String key) {
        try {
            BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(key);
            return boundHashOperations.size();
        } catch (Throwable e) {
            logger.error("hashredis hlen error:", e);
            return 0l;
        }

    }

    /**
     * Hash（哈希表） »
     * HEXISTS
     * HEXISTS key field
     * <p/>
     * 查看哈希表 key 中，给定域 field 是否存在。
     * <p/>
     * 可用版本：
     * >= 2.0.0
     * 时间复杂度：
     * O(1)
     * 返回值：
     * 如果哈希表含有给定域，返回 1 。
     * 如果哈希表不含有给定域，或 key 不存在，返回 0 。
     *
     * @param key
     * @param filed
     * @return
     */
    public boolean hexists(String key, String filed) {
        try {
            BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(key);
            return boundHashOperations.hasKey(filed);
        } catch (Throwable e) {
            logger.error("hashredis hexists key" + key + " filed:" + filed + " error:", e);
            return false;
        }

    }

    /**
     * 批量读取
     *
     * @param redisKeys
     * @return
     */
    public List<String> hgetallBatchWithBean(final List<RedisObjInfo> redisKeys) {
        if (redisSwitch) {
            try {
                RedisCallback callback = new RedisCallback() {
                    @Override
                    public Object doInRedis(
                            RedisConnection connection)
                            throws DataAccessException {
                        for (RedisObjInfo redisObjInfo : redisKeys) {
                            connection.hGet(redisObjInfo.getRedisKey().getBytes(), redisObjInfo.getFieldKey().getBytes());
                        }
                        return null;
                    }
                };
                return (List<String>) redisTemplate.executePipelined(callback, stringRedisSerializer);
            } catch (Throwable e) {
                logger.error("hgetallBatchWithBean  error:", e);
            }
        }
        return null;
    }

    /**
     * 批量写入
     *
     * @param redisObjInfos
     */
    public void batchHSet(final List<RedisObjInfo> redisObjInfos) {
        if (redisSwitch) {
            try {
                if (ObjectUtils.isNotEmpty(redisObjInfos)) {
                    RedisCallback callback = new RedisCallback() {
                        @Override
                        public Object doInRedis(
                                RedisConnection connection)
                                throws DataAccessException {
                            for (RedisObjInfo redisObjInfo : redisObjInfos) {
                                connection.hSet(getRedisTemplate().getKeySerializer().serialize(redisObjInfo.getRedisKey()),
                                        getRedisTemplate().getHashKeySerializer().serialize(redisObjInfo.getFieldKey()),
                                        getRedisTemplate().getHashValueSerializer().serialize(redisObjInfo.getValue()));
                            }
                            return null;
                        }
                    };
                    redisTemplate.executePipelined(callback, stringRedisSerializer);
                }
            } catch (Throwable e) {
                logger.error("hgetallBatchWithBean  error:", e);
            }
        }
    }


    /**
     * 批量写入
     *
     * @param redisObjInfos
     */
    public void batchHSetObj(final List<RedisObjInfo> redisObjInfos) {
        if (redisSwitch) {
            try {
                if (ObjectUtils.isNotEmpty(redisObjInfos)) {
                    RedisCallback callback = new RedisCallback() {
                        @Override
                        public Object doInRedis(
                                RedisConnection connection)
                                throws DataAccessException {
                            for (RedisObjInfo redisObjInfo : redisObjInfos) {
                                connection.hSet(getRedisTemplate().getKeySerializer().serialize(redisObjInfo.getRedisKey()),
                                        getRedisTemplate().getHashKeySerializer().serialize(redisObjInfo.getFieldKey()),
                                        getRedisTemplate().getHashValueSerializer().serialize(JSON.toJSONString(redisObjInfo.getObjValue())));
                                connection.expire(getRedisTemplate().getKeySerializer().serialize(redisObjInfo.getRedisKey()), RedisKeyConstants.DEFAULT_ONE_DAY_TIMEOUT);
                            }

                            return null;
                        }
                    };
                    redisTemplate.executePipelined(callback, stringRedisSerializer);
                }
            } catch (Throwable e) {
                logger.error("hgetallBatchWithBean  error:", e);
            }
        }
    }


    public void renameKey(String zaddKey, String newKey) {
        if (redisSwitch) {
            try {
                BoundKeyOperations operations = redisTemplate.boundValueOps(zaddKey);
                operations.rename(newKey);
            } catch (Throwable e) {
                logger.error("renameKey  error:", e);
            }
        }
    }


    /**
     *  计算大小
     * @param redisKey
     */
    public Long getSetSize(String redisKey) {
        if (redisSwitch) {
            try {
                BoundSetOperations boundSetOperations = redisTemplate.boundSetOps(redisKey);
                return boundSetOperations.size();
            } catch (Throwable e) {
                logger.error("hdel key:" + redisKey + " error:", e);
            }
            return 0l;
        } else {
            return 0l;
        }
    }

    /**
     *  计算大小
     *
     * @param redisKey
     */
    public Long getListSize(String redisKey) {
        if (redisSwitch) {
            try {
                BoundListOperations boundListOperations = redisTemplate.boundListOps(redisKey);
                return boundListOperations.size();
            } catch (Throwable e) {
                logger.error("getListSize key:" + redisKey + " error:", e);
            }
            return 0l;
        } else {
            return 0l;
        }
    }
}
