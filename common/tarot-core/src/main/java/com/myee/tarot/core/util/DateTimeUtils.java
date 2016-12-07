package com.myee.tarot.core.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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
    private final static Logger logger = LoggerFactory.getLogger(DateTimeUtils.class);

    public static final String DEFAULT_DATE_FORMAT_PATTERN_SHORT = "yyyy-MM-dd";

    public static final String DEFAULT_TIME_FORMAT_PATTERN_SHORT = "HH:mm:ss";

    public static final String DATE_FORMAT_PATTERN_FULL = "yyyyMMddHHmmss";

    public static final String DEFAULT_DATE_FORMAT_PATTERN_FULL = "yyyy-MM-dd HH:mm:ss";

    public static final String HOUR_END = " 23:59:59";

    public static final String HOUR_START = " 00:00:00";

    private static Map<String, DateTimeFormatter> dateFormatCache = new ConcurrentHashMap<String, DateTimeFormatter>();

    /**	yyyy*/
    public final static String PATTERN_YEAR = "yyyy";
    /**	yyyyMM	*/
    public final static String PATTERN_SHORTMONTH = "yyyyMM";
    /**	yyyy-MM	*/
    public final static String PATTERN_YEAR_MONTH = "yyyy-MM";
    /**	yyyy-MM-dd	*/
//    public final static String PATTERN_DATE = "yyyy-MM-dd";
    /**	yyyyMMdd	*/
    public final static String PATTERN_SHORTDATE = "yyyyMMdd";
    /**	yyyy-MM-dd HH:mm:ss	*/
//    public final static String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    /**	yyyy-MM-dd_HH-mm-ss	用于文件名的格式化*/
    public final static String PATTERN_NAME_DATETIME = "yyyy-MM-dd_HH-mm-ss";
    /**	yyyyMMddHHmmss	*/
//    public final static String PATTERN_SHORTDATETIME = "yyyyMMddHHmmss";
    /** yyyy-MM-dd'T'HH:mm:ssZ **/
    public final static String PATTREN_ES_SEARCH = "yyyy-MM-dd'T'HH:mm:ssZ";

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

        DateTimeFormatter sdf = null;
        if (dateFormatCache.containsKey(pattern)) {
            sdf = dateFormatCache.get(pattern);
       } else {
            try {
               sdf = DateTimeFormat.forPattern(pattern);
               dateFormatCache.put(pattern, sdf);
           } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sdf = DateTimeFormat.forPattern(DEFAULT_DATE_FORMAT_PATTERN_FULL);
           }
        }
       return DateTime.now().toString(sdf);
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
        DateTimeFormatter sdf = null;
        if (dateFormatCache.containsKey(pattern)) {
            sdf = dateFormatCache.get(pattern);
        } else {
            try {
                sdf = DateTimeFormat.forPattern(pattern);
                dateFormatCache.put(pattern, sdf);
            } catch (Exception e) {
                logger.error(e.getMessage(),e);
                sdf = DateTimeFormat.forPattern(DEFAULT_DATE_FORMAT_PATTERN_FULL);
            }
        }
        return new DateTime(date).toString(sdf);
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

       DateTimeFormatter sdf = null;
        try {
            if (dateFormatCache.containsKey(pattern)) {
                sdf = dateFormatCache.get(pattern);
                return DateTime.parse(dateTimeString,sdf).toDate();
            } else {
                sdf = DateTimeFormat.forPattern(pattern);
                dateFormatCache.put(pattern, sdf);
                return DateTime.parse(dateTimeString,sdf).toDate();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**	返回yyyy-MM-dd_HH-mm-ss格式的字符串	*/
    public static String getNormalNameDateTime() {
        return getDateString(new Date(), PATTERN_NAME_DATETIME);
    }

    /**	返回yyyyMMdd格式的字符串	*/
    public static String getShortDate() {
        return toShortDate(new Date());
    }

    /**	返回yyyyMMdd格式的字符串	*/
    public static String toShortDate(Date dateTime) {
        if(dateTime == null){
            return null;
        }
        return getDateString(dateTime, PATTERN_SHORTDATE);
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

    public static Date getDateByStringEs(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        Date date = null;//注意是空格+UTC
        try {
            date = format.parse(dateStr.replace("Z", " UTC"));
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
        return date;
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
     * 获取当天开始时间
     *
     * @return
     * @author jijun
     * @date 2014年6月30日
     */
    public static Date startToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        return calendar.getTime();
    }

    /**
     * 获取当天结束时间
     *
     * @return
     * @author jijun
     * @date 2014年6月30日
     */
    public static Date endToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        calendar.set(Calendar.MILLISECOND,999);
        return calendar.getTime();
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
        return new DateTime(date).toString(DEFAULT_DATE_FORMAT_PATTERN_FULL);
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

    /**	返回Date对象,可以输入yyyyMMdd格式和yyyyMMddHHmmss格式*/
    public static Date toDate(Long dateTime) {
        if(dateTime == null){
            return null;
        }
        String dateStr = String.valueOf(dateTime);
        Date d = toDate(dateStr);
        return d;
    }

    /**
     * 获取距离现在N天后/前的时间
     */
    public static Date getInternalDateByDay(Date d, int days) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        now.setTime(d);
        now.add(Calendar.DATE, days);
        return now.getTime();
    }

    /**	返回yyyyMMddHHmmss格式的字符串	*/
    public static String toShortDateTime(Date dateTime) {
        if(dateTime == null){
            return null;
        }
        return getDateString(dateTime, DATE_FORMAT_PATTERN_FULL);
    }

    /**	返回yyyyMMddHHmmss格式的字符串	*/
    public static String toESString(Date dateTime) {
        if(dateTime == null){
            return null;
        }
        return getDateString(dateTime, PATTREN_ES_SEARCH);
    }

    /**	返回yyyyMMddHHmmss格式的长整型	*/
    public static Long toShortDateTimeL(Date dateTime) {
        String dateStr = toShortDateTime(dateTime);
        if(dateTime == null){
            return null;
        }
        return Long.valueOf(dateStr);
    }

    /** 返回yyyyMMddHHmmss格式的长整型 */
    public static Long getShortDateTimeL() {
        return toShortDateTimeL(new Date());
    }

    /**	返回yyyyMMddHHmmss格式的字符串	*/
    public static String getShortDateTime() {
        return toShortDateTime(new Date());
    }

    /**	返回Date对象,yyyyMM格式,yyyyMMdd格式，yyyyMMddHHmmss格式，yyyy-MM格式，yyyy-MM-dd格式，yyyy-MM-dd HH:mm:ss格式*/
    public static Date toDate(String dateTime) {
        if(dateTime == null){
            return null;
        }

        try {
            Date d = null;
            if(dateTime.length() == 6){
                d = getDateByString(dateTime, PATTERN_SHORTMONTH);
            }else if(dateTime.length() == 7){
                d = getDateByString(dateTime, PATTERN_YEAR_MONTH);
            }else if(dateTime.length() == 8){
                d = getDateByString(dateTime, PATTERN_SHORTDATE);
            }else if(dateTime.length() == 10){
                d = getDateByString(dateTime, DEFAULT_DATE_FORMAT_PATTERN_SHORT);
            }else if(dateTime.length() == 14){
                d = getDateByString(dateTime, DATE_FORMAT_PATTERN_FULL);
            }else if(dateTime.length() == 19){
                d = getDateByString(dateTime, DEFAULT_DATE_FORMAT_PATTERN_FULL);
            }
            return d;
        } catch (Exception e) {
            logger.error("toDate(String dateTime) error...");
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    /**返回yyyy格式Integer */
    public static Integer getYear() {
        return TypeConverter.toInteger(getYear(new Date()));
    }

    /**返回yyyy格式 字符串*/
    public static String getYear(Date date) {
        if(date == null){
            return null;
        }
        return TypeConverter.toString(date.getYear() + 1900);//Date类年份是从1900年算的
    }

    /**返回MM格式字符串 */
    public static String getMonth() {
        return getCurrentDateString().substring(5, 7);
    }

    /**返回MM格式的整型 */
    public static Integer getMonthInt() {
        return TypeConverter.toInteger(getMonth());
    }

    /**返回yyyyMM格式 */
    public static Integer getYearMonth(){
        String yearMonth = getYear() + getMonth();
        return TypeConverter.toInteger(yearMonth);
    }

    /**返回yyyyMMdd格式Integer*/
    public static Integer getYearMonthDay(){
        return TypeConverter.toInteger(toShortDate(new Date()));
    }

    /**	返回Date类型	,可以输入毫秒格式时间*/
    public static Date toMillis(Long millis) {
        if(millis == null){
            return null;
        }
        return new Date(millis);
    }
}
