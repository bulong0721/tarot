package com.myee.tarot.cache.uitl;

import com.myee.tarot.cache.entity.CommonCache;
import com.myee.tarot.cache.entity.MealsCache;
import com.myee.tarot.cache.entity.MetricCache;
import org.redisson.api.*;

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

}
