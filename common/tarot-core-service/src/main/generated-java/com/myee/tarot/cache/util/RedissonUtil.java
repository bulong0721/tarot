package com.myee.tarot.cache.util;

import com.google.common.collect.Lists;
import com.myee.tarot.cache.entity.CommonCache;
import com.myee.tarot.cache.entity.MealsCache;
import com.myee.tarot.cache.entity.MetricCache;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RFuture;
import org.redisson.api.RLiveObjectService;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Martin on 2016/9/5.
 */
public final class RedissonUtil {

    @Value("${redis.redissonCache}")
    private String REDISSON_CACHE;

    @Autowired
    private RedissonClient redissonClient;

    public CommonCache commonCache() {
        RLiveObjectService liveObjectService = redissonClient.getLiveObjectService();
        return liveObjectService.getOrCreate(CommonCache.class, REDISSON_CACHE);
    }

    public MealsCache mealsCache() {
        RLiveObjectService liveObjectService = redissonClient.getLiveObjectService();
        return liveObjectService.getOrCreate(MealsCache.class, REDISSON_CACHE);
    }

    public MetricCache metricCache() {
        RLiveObjectService liveObjectService = redissonClient.getLiveObjectService();
        return liveObjectService.getOrCreate(MetricCache.class, REDISSON_CACHE);
    }

    /**
     *  生成自增长的主键，支持批量
     * @param tableName
     * @param idCount
     * @return
     */
    public List<Long> incrementKey(String tableName, long idCount) {
        RAtomicLong at = redissonClient.getAtomicLong(tableName);
        List<Long> idList = Lists.newArrayList();
        for (int i = 0; i < idCount; i++) {
            RFuture<Long> id = at.addAndGetAsync(1);
            try {
                idList.add(id.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }
        return idList;
    }

}
