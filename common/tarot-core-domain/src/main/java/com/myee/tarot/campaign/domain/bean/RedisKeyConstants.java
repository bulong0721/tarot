package com.myee.tarot.campaign.domain.bean;

import java.text.MessageFormat;

/**
 * INFO: REDIS缓存KEY
 * User: zhaokai@mail.qianwang365.com
 * Date: 2014/9/20
 * Time: 10:23
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */
public class RedisKeyConstants {
    /**
     * redis默认超时小时，单位 秒
     */
    public static final int DEFAULT_NEVER_TIMEOUT = -1;

    /**
     * redis默认超时小时，单位 秒
     */
    public static final int DEFAULT_ONE_DAY_TIMEOUT = 24 * 3600;

    /**
     * redis默认超时小时，单位 秒
     */
    public static final int DEFAULT_ONE_HOUR_TIMEOUT = 3600;

    /**
     * redis默认超时小时，单位 秒
     */
    public static final int DEFAULT_ONE_MINUTE_TIMEOUT = 60;
    /**
     * redis默认超时小时，单位 秒
     */
    public static final int DEFAULT_FIVE_MINUTE_TIMEOUT = 300;


    /**
     * redis默认超时小时，单位 秒
     */
    public static final int DEFAULT_TEN_MINUTE_TIMEOUT = 600;


    /**
     * redis的key前缀，每个key统一以此开始
     */
    public final static String REDIS_KEY_PREFIX = "merchant";


    /**
     * 商家平台系统参数
     */
    public static final String MERCHAT_SYSTEM_PARAMETER = "MERCHAT_SYSTEM_PARAMETER";

    /**
     * 某商家是否锁定
     */
    public static final String MERCHAT_USER_LOCK = "BIZ_LOGIN_LOCK_";


    /**
     * 系统内置标签
     */
    public static final String SYSTEM_DEAULT_LABES = REDIS_KEY_PREFIX + ":system_labels";

    /**
     * 缓存商品信息，包含基本信息、多图片 *
     */
    public final static String PRODUCT_INFO = REDIS_KEY_PREFIX + ":product";
    /**
     * 基本信息 *
     */
    public final static String PRODUCT_INFO_BASE = "produc_base";

    /**
     * 基本信息 *
     */
    public final static String PRODUCT_INFO_BASE_DESC = "produc_base_desc";

    /**
     * 库存 *
     */
    public final static String PRODUCT_INFO_STOCK = "product_stock";
    /**
     * 多图片 存储LIST *
     */
    public final static String PRODUCT_INFO_IMGS = "product_imgs";


    /**
     * 最后一次审核不通过信息
     */
    public final static String PRODUCT_INFO_LAST_AUDIT = "product_last_audit";


    /**
     * 系统热门标签
     */
    public final static String HOT_LABELS = REDIS_KEY_PREFIX + ":label";

    /**
     * 集市标签全部的信息（已上架)
     */
    public final static String MARKET_LABELS = REDIS_KEY_PREFIX + ":market:labels:ids";

    /**
     * 集市标签信息更新标记  需要重置MARKET_LABEL_INFO下的列表数据
     */
    public final static String MARKET_LABELS_RELOAD_FLAG = REDIS_KEY_PREFIX + ":market:labels:reload";

    /**
     * 集市标签下 单个标签下商品
     */
    public final static String MARKET_ONELABEL_PRODUCTS = REDIS_KEY_PREFIX + ":market:single:{0}:products";


    /**
     * 集市标签下 单个标签下商品
     */
    public final static String MARKET_ONELABEL_PRODUCTS_TMP = REDIS_KEY_PREFIX + ":market:single:{0}:productsTMP";

    /**
     * 集市标签下商品列表 更新FLAG
     */
    public final static String MARKET_ONELABEL_PRODUCTS_RELOAD = REDIS_KEY_PREFIX + ":market:single:{0}:reload";

    /**
     * 缓存  集市标签下 全部的商品信息
     */
    public final static String MARKET_ALL_GOODS = REDIS_KEY_PREFIX + ":market:all:products";

    /**
     * 缓存  集市标签下 全部的商品信息
     */
    public final static String MARKET_ALL_GOODS_TMP = REDIS_KEY_PREFIX + ":market:all:productstmp";

    /**
     * 购物集市全部商品列表 更新标记（通常由后台BA 发起的集市标签下商品绑定触发
     */
    public final static String MARKET_ALL_GOODS_RELOAD = REDIS_KEY_PREFIX + ":market:all:reload";


    /**
     * 普通标签下商品列表（只缓存第一页)
     *
     * @deprecated 已经废弃
     */
    public final static String GOODS_LABEL_PRODUCTS = REDIS_KEY_PREFIX + ":label:products";


    /**
     * 黑名单
     */
    public final static String BLACK_USER = REDIS_KEY_PREFIX + ":user:locks";

    /**
     * 某商品被收藏总数
     */
    public final static String PRODUCT_FAV_NUM = REDIS_KEY_PREFIX + ":collection:productnum:id";

    /**
     * 某用户收藏的商品IDS
     */
    public final static String USER_PRODUCT_FAVS = REDIS_KEY_PREFIX + ":collection:user:id";

    /**
     * 用户最近使用的前N条标签
     */
    public final static String labels_for_add_list = REDIS_KEY_PREFIX + ":labels_for_add:user";


    /**
     * 商品标签更新标记   用于更新某个用户下添加商品页面的最近标签
     */
    public final static String labels_for_add_update_flag = REDIS_KEY_PREFIX + ":labels_for_add:update";


    /**
     * 某用户好友发布的商品IDS
     */
    public final static String USER_FRIENDS_PRODUCT = REDIS_KEY_PREFIX + ":user:{0}:friends:products";


    public static String getRedisKey(String type, Object... args) {
        StringBuilder sb = new StringBuilder();
        sb.append(type);
        if (args == null || args.length == 0) {
            return sb.toString();
        }
        sb.append(":");
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i].toString());
            if (i < args.length - 1) {
                sb.append("_");
            }
        }
        return sb.toString();
    }

    public static String getReplaceRedisKey(String type, Object replaceValue) {
        String str = MessageFormat.format(type, replaceValue.toString());
        return str;
    }

    public static void main(String[] args) {

        String s ="merchant:user:{0}:friends:products";
        Integer t  = 125;
        String s2 = getReplaceRedisKey(s,t);
        System.out.print(s2);
    }
}
