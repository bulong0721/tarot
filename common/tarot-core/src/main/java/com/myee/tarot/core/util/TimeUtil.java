package com.myee.tarot.core.util;

import com.myee.tarot.core.time.SystemTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Ray.Fu on 2016/4/27.
 */
public class TimeUtil {

    /**
     * 获取时间比较
     * @param date
     * @return
     */
    public static Map<String,Date> getAfterDate(Date date) {
        Map<String,Date> map = new HashMap<String,Date>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = df.format(date);
        //获取当前一天后一天
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
        Date tomorrow = calendar.getTime();
        String tomorrowDate = df.format(tomorrow);
        String dateTimeBegin = todayDate + " 00:00:00";
        String dateTimeEnd = tomorrowDate + " 02:30:00";
        Date bTime = null;
        Date eTime = null;
        try {
            bTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTimeBegin);
            eTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTimeEnd);
            map.put("bTime",bTime);
            map.put("eTime",eTime);
            return map;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  null;
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

    public static void main(String[] args){
        System.out.print(whetherToday(new Date()));
    }


}
