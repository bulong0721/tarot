package com.myee.tarot.web.util;

import com.myee.tarot.core.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtil {

    private              Date   startDate       = new Date(new Date().getTime());
    private              Date   endDate         = new Date(new Date().getTime());
    private static final Logger LOGGER          = LoggerFactory.getLogger(DateUtil.class);
    private final static String LONGDATE_FORMAT = "EEE, d MMM yyyy HH:mm:ss Z";

    public static String generateTimeStamp() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmSS");
        return format.format(new Date());
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
}
