package com.myee.tarot.uitl;

import com.myee.djinn.dto.DrawToken;
import com.myee.djinn.dto.ShopDetail;
import com.myee.djinn.dto.WaitToken;
import com.myee.tarot.apiold.domain.BaseDataInfo;
import com.myee.tarot.apiold.view.MenuDataInfo;
import com.myee.tarot.campaign.domain.MerchantActivity;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;

/**
 * Created by Martin on 2016/8/22.
 */
public final class CacheUtil {
    public static final String CACHE_ACTIVITY = "campaign.store.activity";
    public static final String CACHE_TABLE_INFO = "shop.table.info";
    public static final String CACHE_MENU_INFO = "shop.menu.info";

    private CacheUtil() {
    }

    public static <K, V> IgniteCache<K, V> getCache(Ignite ignite, String cacheName) {
        return ignite.getOrCreateCache(cacheName);
    }

    public static <K, V> V getFromCache(Ignite ignite, String cacheName, K key) {
        IgniteCache<K, V> cache = ignite.getOrCreateCache(cacheName);
        return cache.get(key);
    }

    public static <K, V> void putInCache(Ignite ignite, String cacheName, K key, V value) {
        IgniteCache<K, V> cache = ignite.getOrCreateCache(cacheName);
        cache.put(key, value);
    }

    public static IgniteCache<Long, MerchantActivity> activityCache(Ignite ignite) {
        return getCache(ignite, CACHE_ACTIVITY);
    }

    public static IgniteCache<String, BaseDataInfo> tableInfoCache(Ignite ignite) {
        return getCache(ignite, CACHE_TABLE_INFO);
    }

    public static IgniteCache<String, MenuDataInfo> menuInfoCache(Ignite ignite) {
        return getCache(ignite, CACHE_MENU_INFO);
    }

    public static final String CACHE_SHOP_OF_CLIENT = "shopOfClient_";
    public static IgniteCache<Long, ShopDetail> shopOfClient(Ignite ignite, Long clientId) {
        return getCache(ignite, CACHE_SHOP_OF_CLIENT + clientId);
    }

    public static final String CACHE_WAIT_OF_TYPE = "waitOfTableType_";
    public static IgniteCache<String, WaitToken> waitOfTableType(Ignite ignite, Long tableType) {
        return getCache(ignite, CACHE_WAIT_OF_TYPE + tableType);
    }

    public static final String CACHE_DRAW_OF_STORE = "drawOfStore_";
    public static IgniteCache<String, DrawToken> drawOfStore(Ignite ignite, Long shopId) {
        return getCache(ignite, CACHE_DRAW_OF_STORE + shopId);
    }
}
