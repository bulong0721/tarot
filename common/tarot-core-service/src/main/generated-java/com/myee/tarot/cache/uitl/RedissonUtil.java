package com.myee.tarot.cache.uitl;

import com.google.common.collect.Lists;
import com.myee.tarot.cache.entity.CommonCache;
import com.myee.tarot.cache.entity.MealsCache;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import java.util.List;

/**
 * Created by Martin on 2016/9/5.
 */
public final class RedissonUtil {

    static final String ENV_TEST = "ENV_TEST";
    static final String MEAL_CACHE = "MEAL_CACHE";

    public static CommonCache commonCache(RedissonClient client) {
        RLiveObjectService liveObjectService = client.getLiveObjectService();
        return liveObjectService.getOrCreate(CommonCache.class, ENV_TEST);
    }

    public static MealsCache mealsCache(RedissonClient client) {
        RLiveObjectService liveObjectService = client.getLiveObjectService();
        return liveObjectService.getOrCreate(MealsCache.class, MEAL_CACHE);
    }

    /**
     *  生成自增长的主键，支持批量
     * @param client
     * @param tableName
     * @param idCount
     * @return
     */
    public static List<Long> incrementKey(RedissonClient client, String tableName, long idCount) {
       /* Config config = new Config();
        // for single server
        config.useSingleServer()
                .setAddress("127.0.0.1:6379")
                .setConnectionPoolSize(10);

        // 2. Create Redisson instance
        RedissonClient redisson = Redisson.create(config);*/

        RAtomicLong at = client.getAtomicLong(tableName);
        List<Long> idList = Lists.newArrayList();
        for (int i = 0; i < idCount; i++) {
            Long id = at.addAndGet(1);
            idList.add(id);
        }

        client.shutdown();
        return idList;
    }
}
