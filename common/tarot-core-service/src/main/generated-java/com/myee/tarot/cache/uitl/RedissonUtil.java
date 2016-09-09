package com.myee.tarot.cache.uitl;

import com.myee.djinn.dto.DrawToken;
import com.myee.djinn.dto.ShopDetail;
import com.myee.djinn.dto.WaitToken;
import com.myee.tarot.apiold.domain.BaseDataInfo;
import com.myee.tarot.apiold.view.MenuDataInfo;
import com.myee.tarot.cache.entity.CommonCache;
import com.myee.tarot.cache.entity.MealsCache;
import com.myee.tarot.campaign.domain.MerchantActivity;
import org.redisson.Redisson;
import org.redisson.api.RLiveObjectService;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;

import java.util.Map;
import java.util.Set;

/**
 * Created by Martin on 2016/9/5.
 */
public final class RedissonUtil {

    static final String ENV_TEST = "ENV_TEST";

    public static CommonCache commonCache(RedissonClient client) {
        RLiveObjectService liveObjectService = client.getLiveObjectService();
        return liveObjectService.getOrCreate(CommonCache.class, ENV_TEST);
    }

    public MealsCache mealsCache(RedissonClient client) {
        RLiveObjectService liveObjectService = client.getLiveObjectService();
        return liveObjectService.getOrCreate(MealsCache.class, ENV_TEST);
    }
}
