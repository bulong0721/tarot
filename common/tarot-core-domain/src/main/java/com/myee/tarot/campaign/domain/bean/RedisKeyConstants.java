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


    public final static String REDIS_KEY_PREFIX = "campaign";

    /**
     * 商户ID获取活动
     */
    public final static String STORE_ACTIVITY = REDIS_KEY_PREFIX +":store:activity";

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
