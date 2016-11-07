package com.myee.tarot.cache.uitl;

import com.google.common.collect.Lists;
import com.myee.tarot.cache.entity.CommonCache;
import com.myee.tarot.cache.entity.MealsCache;
import com.myee.tarot.cache.entity.MetricCache;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Martin on 2016/9/5.
 */
public final class RedissonUtil {

    static final String ENV_TEST = "ENV_TEST";
    static final String MEAL_CACHE = "MEAL_CACHE";
    static final String METRIC_CACHE = "METRIC_CACHE";

    public static CommonCache commonCache(RedissonClient client) {
        RLiveObjectService liveObjectService = client.getLiveObjectService();
        return liveObjectService.getOrCreate(CommonCache.class, ENV_TEST);
    }

    public static MealsCache mealsCache(RedissonClient client) {
        RLiveObjectService liveObjectService = client.getLiveObjectService();
        return liveObjectService.getOrCreate(MealsCache.class, MEAL_CACHE);
    }

    public static MetricCache metricCache(RedissonClient client) {
        RLiveObjectService liveObjectService = client.getLiveObjectService();
        return liveObjectService.getOrCreate(MetricCache.class, METRIC_CACHE);
    }

    /**
     *  生成自增长的主键，支持批量
     * @param client
     * @param tableName
     * @param idCount
     * @return
     */
    public static List<Long> incrementKey(RedissonClient client, String tableName, long idCount) {
        RAtomicLong at = client.getAtomicLong(tableName);
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

        client.shutdown();
        return idList;
    }
}
