package com.myee.tarot.core.util;

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
        calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
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


}
