package com.myee.tarot.weixin.common;

/**
 * Created by Chay on 2016/5/6.
 */
public class Constants {
    public static final String DOWNLOAD_HTTP = System.getProperty("cleverm.push.http");
    public static final String DOWNLOAD_HOME = System.getProperty("cleverm.push.dirs");
    public static final Long EXPIRE_AFTERACCESS_TIME = 60L;//心跳连接session过期时间,安卓应用是30秒心跳一次

}
