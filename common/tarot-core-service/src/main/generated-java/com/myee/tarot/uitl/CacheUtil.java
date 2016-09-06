package com.myee.tarot.uitl;

import com.myee.djinn.dto.DrawToken;
import com.myee.djinn.dto.ShopDetail;
import com.myee.djinn.dto.WaitToken;
import com.myee.tarot.apiold.domain.BaseDataInfo;
import com.myee.tarot.apiold.view.MenuDataInfo;
import com.myee.tarot.campaign.domain.MerchantActivity;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteSet;
import org.apache.ignite.configuration.CollectionConfiguration;

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

    public static final String CACHE_TABLE_TYPE_INFO = "shop.table.Type.info";

    public static IgniteCache<String, BaseDataInfo> tableTypeInfoCache(Ignite ignite) {
        return getCache(ignite, CACHE_TABLE_TYPE_INFO);
    }

    public static IgniteCache<String, MenuDataInfo> menuInfoCache(Ignite ignite) {
        return getCache(ignite, CACHE_MENU_INFO);
    }

    public static final String CACHE_TASTE_INFO = "shop.menu.taste.info";
    public static IgniteCache<String, BaseDataInfo> tasteInfoCache(Ignite ignite) {
        return getCache(ignite, CACHE_TASTE_INFO);
    }

    public static final String CACHE_SHOP_OF_CLIENT = "shopOfClient_";

    public static IgniteCache<Long, ShopDetail> shopOfClient(Ignite ignite, Long clientId) {
        return getCache(ignite, CACHE_SHOP_OF_CLIENT + clientId);
    }

    //存放所有取号查进展需要用的缓存
    public static final String CACHE_CUSTOMER_TOKEN = "weixin.customerToken";
    public static IgniteCache<String, String> customerTokenCache(Ignite ignite) {
        return getCache(ignite, CACHE_CUSTOMER_TOKEN);
    }

    public static final String CACHE_WAIT_OF_TYPE = "waitOfTableType_";

    public static IgniteCache<String, WaitToken> waitOfTableType(Ignite ignite, Long tableType) {
        return getCache(ignite, CACHE_WAIT_OF_TYPE + tableType);
    }

    public static String getWaitOfTableType(Long sceneId) {
        return CACHE_WAIT_OF_TYPE + sceneId;
    }

    public static final String CACHE_DRAW_OF_STORE = "drawOfStore_";

    public static IgniteCache<String, DrawToken> drawOfStore(Ignite ignite, Long shopId) {
        return getCache(ignite, CACHE_DRAW_OF_STORE + shopId);
    }

    //二维码参数和唯一码的缓存key
    public static final String KEY_QRCODE_PARAM_IDENTITYCODE = "qrCodeParameterToIdentityCode_";
    public static String getIdentityCode(Long qrCodeParameter) {
        return KEY_QRCODE_PARAM_IDENTITYCODE + qrCodeParameter;
    }

    //将排号的key放入Set中，如A01，A02，getAll取出所有某缓存的值的时候需要用到
    public static final String CACHE_WAIT_OF_TYPE_SET = "waitTokenTypeKeySet_";
    public static IgniteSet<String> waitOfTableTypeSet(Ignite ignite, String tableType, CollectionConfiguration setCfg) {
        return ignite.set(CACHE_WAIT_OF_TYPE_SET + tableType, setCfg);
    }
}
