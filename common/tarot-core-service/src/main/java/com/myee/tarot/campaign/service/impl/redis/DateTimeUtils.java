package com.myee.tarot.campaign.service.impl.redis;


import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

/**
 * INFO: 日期时间工具类
 * User: zhaokai@mail.qianwang365.com
 * Date: 2014/9/17
 * Time: 13:13
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */
public class DateTimeUtils {

    public static final String DEFAULT_DATE_FORMAT_PATTERN_SHORT = "yyyy-MM-dd";

    public static final String DEFAULT_TIME_FORMAT_PATTERN_SHORT = "HH:mm:ss";

    public static final String DATE_FORMAT_PATTERN_FULL = "yyyyMMddHHmmss";

    public static final String DEFAULT_DATE_FORMAT_PATTERN_FULL = "yyyy-MM-dd HH:mm:ss";

    public static final String HOUR_END = " 23:59:59";

    public static final String HOUR_START = " 00:00:00";

    private static Map<String, FastDateFormat> dateFormatCache = new ConcurrentHashMap<String, FastDateFormat>();

    /**
     * 以yyyy-MM-dd HH:mm:ss形式返回当前时间的字符串
     *
     * @return
     */
    public static String getCurrentDateTimeString() {
        return getCurrentDateString(DEFAULT_DATE_FORMAT_PATTERN_FULL);
    }

    /**
     * 以yyyy-MM-dd形式返回当前日期的字符串
     *
     * @return
     */
    public static String getCurrentDateString() {
        return getCurrentDateString(DEFAULT_DATE_FORMAT_PATTERN_SHORT);
    }

    /**
     * 返回pattern所指定的当前时间的字符串
     *
     * @param pattern
     * @return
     */
    public static String getCurrentDateString(String pattern) {
        if (pattern == null || "".equals(pattern.trim())) {
            return null;
        }
        FastDateFormat sdf = null;
        if (dateFormatCache.containsKey(pattern)) {
            sdf = dateFormatCache.get(pattern);
        } else {
            try {
                sdf = FastDateFormat.getInstance(pattern);
                dateFormatCache.put(pattern, sdf);
            } catch (Exception e) {
                e.printStackTrace();
                sdf = FastDateFormat.getInstance(DEFAULT_DATE_FORMAT_PATTERN_FULL);
            }
        }
        return sdf.format(new Date());
    }

    /**
     * 返回时间date所指定的日期格式的字符串形式
     *
     * @param date
     * @param pattern
     * @return
     */
    public static String getDateString(Date date, String pattern) {
        if (date == null || pattern == null || "".equals(pattern.trim())) {
            return null;
        }
        FastDateFormat sdf = null;
        if (dateFormatCache.containsKey(pattern)) {
            sdf = dateFormatCache.get(pattern);
        } else {
            try {
                sdf = FastDateFormat.getInstance(pattern);
                dateFormatCache.put(pattern, sdf);
            } catch (Exception e) {
                e.printStackTrace();
                sdf = FastDateFormat.getInstance(DEFAULT_DATE_FORMAT_PATTERN_FULL);
            }
        }
        return sdf.format(date);
    }

    /**
     * 将dateTimeString按照格式pattern转换成Date
     *
     * @param dateTimeString
     * @param pattern
     * @return
     */
   public static Date getDateByString(String dateTimeString, String pattern) {
        if (dateTimeString == null || "".equals(dateTimeString.trim()) || pattern == null || "".equals(pattern.trim())) {
            return null;
        }
        FastDateFormat sdf = null;
        try {
            if (dateFormatCache.containsKey(pattern)) {
                sdf = dateFormatCache.get(pattern);
                return sdf.parse(dateTimeString);
            } else {
                sdf = FastDateFormat.getInstance(pattern);
                dateFormatCache.put(pattern, sdf);
                return sdf.parse(dateTimeString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        Date date = getDateByString("2016-5-30 23:30:34","yyyy-MM-dd HH:mm:ss");
        System.out.println(date.getTime());
    }

    /**
     * 将dateTimeString按照默认格式yyyy-MM-dd HH:mm:ss转换成Date
     *
     * @param dateTimeString
     * @return
     */
    public static Date getDateByString(String dateTimeString) {
        return getDateByString(dateTimeString, DEFAULT_DATE_FORMAT_PATTERN_FULL);
    }

    /**
     * 获取一天开始时间
     *
     * @param startDate 格式yyyy-MM-dd
     * @return
     * @author jijun
     * @date 2014年6月30日
     */
    public static Date startOneDay(String startDate) {
        return getDateByString(startDate + HOUR_START);
    }

    /**
     * 获取一天结束时间
     *
     * @param endDate 格式yyyy-MM-dd
     * @return
     * @author jijun
     * @date 2014年6月30日
     */
    public static Date endOneDay(String endDate) {
        return getDateByString(endDate + HOUR_END);
    }

    /**
     * 根据时间获取默认格式yyyy-MM-dd HH:mm:ss的String表达式
     *
     * @param date
     * @author jijun
     * @date 2014年7月1日
     */
    public static String getDefaultDateString(Date date) {

        return DateFormatUtils.format(date, DEFAULT_DATE_FORMAT_PATTERN_FULL);
    }


    /**
     * 计算2个时间差,date1-date2
     *
     * @param date1
     * @param date2
     * @return 时间差，单位妙
     */
    public static int getTimeGap(Date date1, Date date2) {

        long result = (date1.getTime() - date2.getTime()) / 60000;
        return Integer.parseInt(String.valueOf(result));
    }

    /**
     * 创建时间
     *
     * @param date1 yyyy-MM-dd
     * @param date2 HH:mm:ss
     * @return
     */
    public static Date createDate(String date1, String date2) {

        try {
            return DateTimeUtils.getDateByString(date1 + " " + date2);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取客户端要求的日期格式yyyy.MM.dd
     *
     * @param dateTime
     * @return
     */
    public static String convertToAppClientDateFormat(String dateTime) {
        if (dateTime != null) {
            return dateTime.replace("-", ".");
        }
        return dateTime;
    }

    /**
     * 获取服务端要求的日期格式yyyy-MM-dd
     *
     * @param dateTime
     * @return
     */
    public static String convertToServerDateFormat(String dateTime) {
        if (dateTime != null) {
            return dateTime.replace(".", "-");
        }
        return dateTime;
    }

    /**
     * 获取距离现在N天后/前的时间
     *
     */
    public static Date getInternalDateByDay(Date d, int days) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(d);
        now.add(Calendar.DATE, days);
        return now.getTime();
    }

}
