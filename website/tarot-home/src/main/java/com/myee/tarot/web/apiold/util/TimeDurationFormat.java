package com.myee.tarot.web.apiold.util;

import com.myee.tarot.web.util.DateUtil;

import java.util.Date;


/**
 * 按枚举Format中的格式，格式化一个时间段
 * 例如：3800秒可以格式化为“1小时3分钟”、“01：03：20”等格式
 * @author zq.song
 *
 */
public class TimeDurationFormat {
	private Format format;
	
	private long days;
	private long hours;
	private long minutes;
	private long seconds;
	
	public enum Format {
		/** 时间段格式：XX天XX小时XX分 XX秒*/
		STRING_DEFAULT(11, "XXdXXhXXmXXs"),
		/** 时间段格式：XX天XX小时XX分XX秒,全显示，不同于上面的default只会显示两级*/
		STRING_DDHHMMSS(11, "XXdXXhXXmXXs"),
		/** 时间段格式：XX天XX小时XX分*/
		STRING_DDHHMM(15, "XXdXXhXXm"),
		/** 时间段格式：XX天XX小时*/
		STRING_DDHH(12, "XXdXXh"),
		/** 时间段格式：XX小时XX分钟 */
		STRING_HHMM(13, "XXhXXm"),
		/** 时间段格式：XX分钟XX秒 */
		STRING_MMSS(14, "XXmXXs"),
		/** 时间段格式：xx分钟 */
		STRING_MM(21, "XXm"),
		/**时间段格式：hh:mm:ss */
		TIME_HHMMSS(31, "hh:mm:ss"),
		/**时间段格式：hh:mm */
		TIME_HHMM(32, "hh:mm");
		
		
		private int id;
		private String format;
		
		private Format(int id, String format) {
			this.id = id;
			this.format = format;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getFormat() {
			return format;
		}

		public void setFormat(String format) {
			this.format = format;
		}
	}
	
	public TimeDurationFormat() {
		this.format = Format.STRING_DEFAULT;
	}
	
	public TimeDurationFormat(Format format) {
		this.format = format;
	}
	
	/**
	 * 从天开始格式化
	 * @param time	时间段，单位（秒）
	 */
	private void initDay(long time) {
		days = time/(24*60*60);
		hours = (time/(60*60)-days*24);
		minutes = ((time/(60))-days*24*60-hours*60);
		seconds = (time-days*24*60*60-hours*60*60-minutes*60);
	}
	
	/**
	 * 从小时开始格式化
	 * @param time	时间段，单位（秒）
	 */
	private void initHour(long time) {
		hours = (time / (60 * 60));
		minutes = (time/60 - hours*60);
		seconds = (time - hours*60*60 - minutes*60);
	}
	
	/**
	 * 从分钟开始格式化
	 * @param time	时间段，单位（秒）
	 */
	private void initMintue(long time) {
		minutes = (time/60);
		seconds = (time - minutes*60);
	}
	
	/**
	 * 格式化时间
	 * @param time	时间，单位秒
	 * @return
	 */
	public String format(long time) {
		if (this.format == Format.STRING_DEFAULT) {
			initDay(time);
			return format_defualt();
		} else if (this.format == Format.STRING_DDHHMMSS) {
			initDay(time);
			return format_1();
		} else if (this.format == Format.STRING_DDHH) {
			initDay(time);
			return format_12();
		} else if (this.format == Format.STRING_DDHHMM) {
			initDay(time);
			return format_15();
		} else if (this.format == Format.STRING_HHMM) {
			initHour(time);
			return format_13();
		} else if (this.format == Format.STRING_MMSS) {
			initMintue(time);
			return format_14();
		} else if (this.format == Format.STRING_MM) {
			initMintue(time);
			return format_21();
		} else if (this.format == Format.TIME_HHMMSS) {
			initHour(time);
			return format_31();
		} else if (this.format == Format.TIME_HHMM) {
			initHour(time);
			return format_32();
		} else {
			initDay(time);
			return format_defualt();
		}
	}
	
	/**
	 * 格式化两段时间之差
	 * @param startTime		开始时间
	 * @param endTime		结束时间
	 * @return
	 */
	public String format(Date startTime, Date endTime) {
		if (startTime == null || endTime == null)
			return null;
		long time = endTime.getTime() - startTime.getTime();
		return this.format(time / 1000);
	}
	
	private String format_defualt() {
		StringBuffer intervalBuff = new StringBuffer();
		if (days > 0) {
			intervalBuff.append(days).append("天");
			if (hours > 0){
				intervalBuff.append(hours).append("小时");
			}
			return intervalBuff.toString();
		}else if (hours > 0) {
			intervalBuff.append(hours).append("小时");
			if (minutes > 0) {
				intervalBuff.append(minutes).append("分钟");
			}
			return intervalBuff.toString();
		}else if (minutes > 0) {
			intervalBuff.append(minutes).append("分钟");
			if (seconds > 0) {
				intervalBuff.append(seconds).append("秒");
			}
			return intervalBuff.toString();
		}else{
			intervalBuff.append(seconds).append("秒");
			return intervalBuff.toString();
		}
	}

	private String format_1() {
		StringBuffer intervalBuff = new StringBuffer();
		if (days > 0) {
			intervalBuff.append(days).append("天");
			if (hours > 0 || days > 0 ){
				intervalBuff.append(hours).append("小时");
			}
			if (minutes > 0 || hours >0 ) {
				intervalBuff.append(minutes).append("分钟");
			}
			if (seconds > 0 || minutes > 0 ) {
				intervalBuff.append(seconds).append("秒");
			}
			return intervalBuff.toString();
		}else if (hours > 0) {
			intervalBuff.append(hours).append("小时");
			if (minutes > 0 || hours >0 ) {
				intervalBuff.append(minutes).append("分钟");
			}
			if (seconds > 0 || minutes > 0 ) {
				intervalBuff.append(seconds).append("秒");
			}
			return intervalBuff.toString();
		}else if (minutes > 0) {
			intervalBuff.append(minutes).append("分钟");
			if (seconds > 0 || minutes > 0 ) {
				intervalBuff.append(seconds).append("秒");
			}
			return intervalBuff.toString();
		}else{
			intervalBuff.append(seconds).append("秒");
			return intervalBuff.toString();
		}
	}

	private String format_15() {
		StringBuffer intervalBuff = new StringBuffer();
		if (days > 0) {
			intervalBuff.append(days).append("天");
			if (hours > 0){
				intervalBuff.append(hours).append("小时");
			}
			return intervalBuff.toString();
		}else if (hours > 0) {
			intervalBuff.append(hours).append("小时");
			if (minutes > 0) {
				intervalBuff.append(minutes).append("分钟");
			}
			return intervalBuff.toString();
		}else if (minutes > 0) {
			intervalBuff.append(minutes).append("分钟");
			return intervalBuff.toString();
		}else{
			return "0分钟";
		}
	}
	
	private String format_12() {
		StringBuffer intervalBuff = new StringBuffer();
		
		if (days > 0)
			intervalBuff.append(days).append("天");
		intervalBuff.append(hours).append("小时");
		
		return intervalBuff.toString();
	}
	
	private String format_13() {
		StringBuffer intervalBuff = new StringBuffer();
		
		if (hours > 0)
			intervalBuff.append(hours).append("h");
		intervalBuff.append(minutes).append(" m");
		
		return intervalBuff.toString();
	}
	
	private String format_14() {
		StringBuffer intervalBuff = new StringBuffer();
		
		if (minutes > 0)
			intervalBuff.append(minutes).append("分钟");
		intervalBuff.append(seconds).append("秒");
		
		return intervalBuff.toString();
	}
	
	private String format_21() {
		StringBuffer intervalBuff = new StringBuffer();
		
		if (seconds > 30)
			minutes +=1;
		
		intervalBuff.append(minutes).append("分钟");
		
		return intervalBuff.toString();
	}
	
	private String format_31() {
		StringBuffer intervalBuff = new StringBuffer();
		if (hours > 0 && hours <= 9) {
			intervalBuff.append("0").append(hours).append(":");
		} else if (hours >= 10) {
			intervalBuff.append(hours).append(":");
		} else {
			intervalBuff.append("00").append(":");
		}
		
		if (minutes > 0 && minutes <= 9) {
			intervalBuff.append("0").append(minutes).append(":");
		} else if (minutes >= 10) { 
			intervalBuff.append(minutes).append(":");
		} else {
			intervalBuff.append("00").append(":");
		}
		
		if (seconds > 0 && seconds <= 9) {
			intervalBuff.append("0").append(seconds);
		} else if (seconds >= 10) {
			intervalBuff.append(seconds);
		} else {
			intervalBuff.append("00");
		}
		
		return intervalBuff.toString();
	}
	
	private String format_32() {
		StringBuffer intervalBuff = new StringBuffer();
		if (hours > 0 && hours <= 9) {
			intervalBuff.append("0").append(hours).append(":");
		} else if (hours >= 10) {
			intervalBuff.append(hours).append(":");
		} else {
			intervalBuff.append("00").append(":");
		}
		
		if (minutes > 0 && minutes <= 9) {
			intervalBuff.append("0").append(minutes);
		} else if (minutes >= 10) { 
			intervalBuff.append(minutes);
		} else {
			intervalBuff.append("00");
		}
		
		return intervalBuff.toString();
	}
	
	public static void main(String[] args) {
		TimeDurationFormat format = new TimeDurationFormat(Format.STRING_DEFAULT);
		System.out.println("--------------" + format.format(400000));
		Date startTime = DateUtil.getHistoryDateTime(new Date(), 3);
		Date endTime = new Date();
		format = new TimeDurationFormat(Format.TIME_HHMMSS);
		System.out.println("--------------" + format.format(startTime, endTime));
	}
}
