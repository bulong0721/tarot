package com.myee.tarot.core.util;

import com.myee.tarot.core.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class DateUtil {

    private Date startDate = new Date(new Date().getTime());
    private Date endDate = new Date(new Date().getTime());
    private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);
    private final static String LONGDATE_FORMAT = "EEE, d MMM yyyy HH:mm:ss Z";

    private final static String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final static String DATETIME_FORMAT_SLASH = "yyyy/MM/dd HH:mm:ss";

    public static java.text.DateFormat dateTimeMISFormat = null;
    // 默认显示日期时间的格式 精确到毫秒
    public static final String DATATIMEF_STR_MIS = "yyyyMMddHHmmssSSS";
    static {
        dateTimeMISFormat = new SimpleDateFormat(DATATIMEF_STR_MIS);
    }

    public static String generateTimeStamp() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmSS");
        return format.format(new Date());
    }

    /**
     * 获取某天的0时0分0秒的Date
     *
     * @param date
     * @return
     */
    public static Date getZeroClockOfDate(Date date) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        Date dateZero = new Date(date.getTime() - gc.get(gc.HOUR_OF_DAY) * 60 * 60
                * 1000 - gc.get(gc.MINUTE) * 60 * 1000 - gc.get(gc.SECOND)
                * 1000);
        return new Date(dateZero.getTime());
    }

    /**
     * 获取某天的后一天的某个时，分，秒的Date
     *
     * @param date
     * @return
     */
    public static Date getNextDayOfDate(Date date, int hour, int minute, int second) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime();   //这个时间就是日期往后推一天的结果
        calendar.setTime(date);
        calendar.set(calendar.HOUR_OF_DAY, hour);
        calendar.set(calendar.MINUTE, minute);
        calendar.set(calendar.SECOND, second);
        return calendar.getTime();
    }

    public static String formatDate(Date dt) {

        if (dt == null)
            return null;
        SimpleDateFormat format = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
        return format.format(dt);

    }

    public static String formatYear(Date dt) {

        if (dt == null)
            return null;
        SimpleDateFormat format = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT_YEAR);
        return format.format(dt);

    }

    public static String formatDateTime(Date dt) {

        if (dt == null)
            return null;
        SimpleDateFormat format = new SimpleDateFormat(DATETIME_FORMAT);
        return format.format(dt);
    }

    public static String formatLongDate(Date date) {

        if (date == null)
            return null;
        SimpleDateFormat format = new SimpleDateFormat(LONGDATE_FORMAT);
        return format.format(date);

    }

    public static String formatDateMonthString(Date dt) {

        if (dt == null)
            return null;
        SimpleDateFormat format = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
        return format.format(dt);

    }

    public static Date getDate(String date) throws Exception {
        DateFormat myDateFormat = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
        return myDateFormat.parse(date);
    }


    public static Date getDateTime(String date) throws Exception {
        DateFormat myDateFormat = new SimpleDateFormat(DATETIME_FORMAT_SLASH);
        return myDateFormat.parse(date);
    }

    public static Date addDaysToCurrentDate(int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, days);
        return c.getTime();

    }

    public static Date getDate() {

        return new Date(new Date().getTime());

    }

    public static String getPresentDate() {

        Date dt = new Date();

        SimpleDateFormat format = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
        return format.format(new Date(dt.getTime()));
    }

    public static String getPresentYear() {

        Date dt = new Date();

        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return format.format(new Date(dt.getTime()));
    }

    public void processPostedDates(HttpServletRequest request) {
        Date dt = new Date();
        DateFormat myDateFormat = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);
        Date sDate = null;
        Date eDate = null;
        try {
            if (request.getParameter("startdate") != null) {
                sDate = myDateFormat.parse(request.getParameter("startdate"));
            }
            if (request.getParameter("enddate") != null) {
                eDate = myDateFormat.parse(request.getParameter("enddate"));
            }
            this.startDate = sDate;
            this.endDate = eDate;
        } catch (Exception e) {
            LOGGER.error("", e);
            this.startDate = new Date(dt.getTime());
            this.endDate = new Date(dt.getTime());
        }
    }

    public Date getEndDate() {
        return endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    /**
     * 返回毫秒格式的秒数
     */
    public static Long getSecondsOfCurrentMillis() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * 得到给定日期n小时前的时间
     */
    public static Date getHistoryDateTime(Date starttime, Integer hours) {
        if (starttime == null || hours == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(starttime);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - hours);
        return calendar.getTime();
    }

    /**
     * 判断当前是否为今天
     * @param time
     * @return
     */
    public static boolean whetherToday(Date time) {
        Calendar current = Calendar.getInstance();
        Calendar today = Calendar.getInstance();	//今天

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set( Calendar.HOUR_OF_DAY, 0);
        today.set( Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Calendar tomorrow = Calendar.getInstance();	//明天

        tomorrow.set(Calendar.YEAR, current.get(Calendar.YEAR));
        tomorrow.set(Calendar.MONTH, current.get(Calendar.MONTH));
        tomorrow.set(Calendar.DAY_OF_MONTH,current.get(Calendar.DAY_OF_MONTH)+1);
        tomorrow.set( Calendar.HOUR_OF_DAY, 0);
        tomorrow.set( Calendar.MINUTE, 0);
        tomorrow.set(Calendar.SECOND, 0);

        current.setTime(time);

        if(current.after(today)&&current.before(tomorrow)){
            return true;
        }else{
            return false;
        }

    }

    /**
     * 获取订单编号
     * 点点笔通用生成规则
     * @return
     */
    public static String getOrderNum() {
        Calendar nowCalendar = Calendar.getInstance(Locale.CHINESE);
        nowCalendar.setTime(DateUtil.now());
        return dateTimeMISFormat.format(nowCalendar.getTime())+RandomUtil.threeRandom();
    }

    public static Date now() {
        return Calendar.getInstance(Locale.CHINESE).getTime();
    }
}
