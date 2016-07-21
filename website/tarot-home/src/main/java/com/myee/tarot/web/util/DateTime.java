package com.myee.tarot.web.util;


import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.*;

/***
 * @author enva.liang@clever-m.com
 * 时间类型定义：<br/>
 * 1、normalDate："yyyy-MM-dd"，String<br/>
 * 2、normalDateTime:"yyyy-MM-dd HH:mm:ss"，String<br/>
 * 2、millis： 毫秒格式，Long<br/>
 * 3、shortDate： 20151218，Integer<br/>
 * 4、shortDateTime： 20151218101203，Long<br/>
 * 5、date： Date对象<br/>
 */
public class DateTime {
	
	private final static Logger logger = LoggerFactory.getLogger(DateTime.class);

	/**	yyyyMM	*/
	public final static String PATTERN_SHORTMONTH = "yyyyMM";
	/**	yyyy-MM	*/
	public final static String PATTERN_YEAR_MONTH = "yyyy-MM";
	/**	yyyy-MM-dd	*/
	public final static String PATTERN_DATE = "yyyy-MM-dd";
	/**	yyyyMMdd	*/
	public final static String PATTERN_SHORTDATE = "yyyyMMdd";
	/**	yyyy-MM-dd HH:mm:ss	*/
	public final static String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
	/**	yyyyMMddHHmmss	*/
	public final static String PATTERN_SHORTDATETIME = "yyyyMMddHHmmss";

	/**	返回yyyyMMddHHmmss格式的字符串	*/
	public static String getShortDateTime() {
		return toShortDateTime(new Date());
	}

	/**	返回yyyyMMddHHmmss格式的字符串	*/
	public static String toShortDateTime(Date dateTime) {
		if(dateTime == null){
			return null;
		}
		return DateFormatUtils.format(dateTime, PATTERN_SHORTDATETIME);
	}

	/**	返回yyyy-MM-dd HH:mm:ss格式的字符串,可以输入yyyyMMdd格式和yyyyMMddHHmmss格式*/
	public static String toNormalDateTime(Long dateTime) {
		if(dateTime == null){
			return null;
		}
		Date d = toDate(dateTime);
		if(d == null){
			return null;
		}
		return DateFormatUtils.format(d, PATTERN_DATETIME);
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

	/**	返回Date对象,yyyyMM格式,yyyyMMdd格式，yyyyMMddHHmmss格式，yyyy-MM格式，yyyy-MM-dd格式，yyyy-MM-dd HH:mm:ss格式*/
	public static Date toDate(String dateTime) {
		if(dateTime == null){
			return null;
		}

		try {
			Date d = null;
			if(dateTime.length() == 6){
				d = DateUtils.parseDate(dateTime, new String[]{PATTERN_SHORTMONTH});
			}else if(dateTime.length() == 7){
				d = DateUtils.parseDate(dateTime, new String[]{PATTERN_YEAR_MONTH});
			}else if(dateTime.length() == 8){
				d = DateUtils.parseDate(dateTime, new String[]{PATTERN_SHORTDATE});
			}else if(dateTime.length() == 10){
				d = DateUtils.parseDate(dateTime, new String[]{PATTERN_DATE});
			}else if(dateTime.length() == 14){
				d = DateUtils.parseDate(dateTime, new String[]{PATTERN_SHORTDATETIME});
			}else if(dateTime.length() == 19){
				d = DateUtils.parseDate(dateTime, new String[]{PATTERN_DATETIME});
			}
			return d;
		} catch (ParseException e) {
			logger.warn("toDate(String dateTime) error...");
			e.printStackTrace();
		}
		return null;
	}

	/*public static void main(String[] args) {
		Long dateStr = new Date().getTime()*1000;
		Date d = toDate(dateStr);
		System.out.println(DateFormatUtils.format(d, PATTERN_DATETIME));
	}*/

}