package com.myee.tarot.web.util;

import java.util.EmptyStackException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import org.apache.commons.validator.GenericValidator;

public class ValidatorUtil {

	/** 是否字母组合	*/
	public static boolean isAlpha(String str) {
		if (str == null) {
			return false;
		}
		byte abyte0[] = str.getBytes();
		for (int i = 0; i < abyte0.length; i++) {
			if ((abyte0[i] < 65 || abyte0[i] > 90)
					&& (abyte0[i] < 97 || abyte0[i] > 122)) {
				return false;
			}
		}
		return true;
	}

	/**	是否字母或数字组合	*/
	public static boolean isAlphaNumeric(String s) {
		if (s==null){
			return false;
		}
		byte abyte0[] = s.getBytes();
		for (int i = 0; i < abyte0.length; i++)
			if ((abyte0[i] < 48 || abyte0[i] > 57)
					&& (abyte0[i] < 65 || abyte0[i] > 90)
					&& (abyte0[i] < 97 || abyte0[i] > 122))
				return false;

		return true;
	}

	/** 判断字符的长度6-15之间 */
	public static boolean isCharSize(String s) {
		if (s==null){
			return false;
		}
		if (s.length()>=6 && s.length()<=15){
			return true;
		}
		return false;
	}

	/**	是否纯数字	*/
	public static boolean isNumeric(String s) {
		if (s==null){
			return false;
		}
		byte abyte0[] = s.getBytes();
		if (abyte0.length == 0) {
			return false;
		}
		for (int i = 0; i < abyte0.length; i++) {
			if (abyte0[i] < 48 || abyte0[i] > 57) {
				return false;
			}
		}
		return true;
	}

	/**	是否纯中文	*/
	public static boolean isChinese(String text) {
		if (StringUtil.isNullOrEmpty(text)){
			return false;
		}
		for (int i = 0; i < text.length(); i++) {
			String aChar = text.substring(i, i+1);
			boolean isChinese = Pattern.matches("[\u4E00-\u9FA5]", aChar);
			if(!isChinese){
				return false;
			}
		}
		return true;
	}



	/**	判断民用车牌格式：<br>
	 * 1、第一位是汉字<br>
	 * 2、长度在6－30之间<br>
	 * 3、不允许纯数字（防止跟可能需要的手机号码登录冲突）<br>
	 * 4、不允许@符号（防止跟可能需要的email登录冲突）<br>
	 * 	*/
	public static boolean isCivilPlateNum(String plateNum) {
		if (plateNum == null){
			return false;
		}
		if (plateNum.length() != 7){
			//长度不等于7
			return false;
		}
		String firstChar = String.valueOf(plateNum.charAt(0));
		if (!isChinese(firstChar)){
			//第一个字符不是中文
			return false;
		}
		String secondChar = String.valueOf(plateNum.charAt(1));
		if (!isAlpha(secondChar)){
			//第二个字符不是字母
			return false;
		}
		String tailChar = plateNum.substring(2);
		if (!isAlphaNumeric(tailChar)){
			return false;
		}

		return true;
	}

	/**
	 * 判断一个字符串是否电话号码,包括固话和手机
	 * <br>固定电话:(086)021-64661249*0000格式,只能是数字和-()*这4种符号，长度在7-30位之间
	 * <br>手机号码:支持移动、电信、联通的所有号段
	 * @param phone  需要检测的字符串
	 * @return
	 */
	public static boolean isTelePhone(String phone) {
		if (StringUtil.isNullOrEmpty(phone)){
			return false;
		}

		if (isMobile(phone)){
			//如果为手机号码，则直接返回true
			return true;
		}

		if (isPhone(phone)){
			//如果为固定电话，则直接返回true
			return true;
		}

		return false;
	}

	/**
	 * 判断是否为电话号码
	 * <br>长度在1-30位之间
	 * <br>只能是数字和-()*这4种符号
	 * @param phone    将要验证的字符串
	 * @return   返回true或false
	 */
	public static boolean isPhone(String phone) {
		if (phone == null) {
			return false;
		}
		String strTemp = "0123456789-()*";
		for (int i = 0; i < phone.length(); i++) {
			if (strTemp.indexOf(phone.charAt(i)) == -1) {
				return false;
			}
		}
		if (phone.length() >= 1 && phone.length() <= 30) {
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 判断是否为手机号码,11位手机号码,
	 * 0+手机号码：013812545467将会返回false
	 * @param phone    将要验证的字符串
	 * @return   返回true或false
	 */
	public static boolean isMobile(String phone) {
		if (phone != null) {
			String regexMobile = "^(13[0-9]|14[0|1|2|3|4|5|6|7]|15[0-9]|17[0-9]|18[0-9])\\d{8}$";
			Pattern mobileP = Pattern.compile(regexMobile);
			Matcher m = mobileP.matcher(phone);
			return m.find();
		}
		return false;
	}

	/**
	 * 验证手机号是否是多个逗号隔开的，无空格。当手机号为空或“”时，说明用户不填，也是可以的。
	 * @param phone
	 * @return true验证通过，false验证失败
	 */
	public static boolean isMultiMobile(String phone) {
		if(phone == null || phone.equals("")){
			return true;
		}

		String phones[]= phone.split(",");
		for(int i=0;i<phones.length;i++){
			if(!ValidatorUtil.isMobile(phones[i])){
				return false;
			}

		}
		return true;
	}

	/**	是否邮编	*/
	public static boolean isZip(String s) {
		if (s != null) {
			if (isNumeric(s) && s.length() == 6) {
				return true;
			}
		}
		return false;
	}

	/**	是否QQ号码	*/
	public static boolean isQQ(String s) {
		if (s != null) {
			if (isNumeric(s) && s.length() >= 5 && s.length() <= 16) {
				return true;
			}
		}
		return false;
	}

	/**	是否正确格式日期，有8位的格式或者14位包括时间的格式
	 * 格式1：20080924
	 * 格式2：20080924153326
	 * 	*/
	public static boolean isValidISODate(String s) {
		if (s == null || !isNumeric(s))
			return false;
		if (s.length() != 8 && s.length() != 14)
			return false;
		if (s.length() == 14) {
			if (Integer.parseInt(s.substring(8, 10)) > 24)
				return false;
			if (Integer.parseInt(s.substring(10, 12)) > 60)
				return false;
			if (Integer.parseInt(s.substring(12, 14)) > 60)
				return false;
		}
		int i = Integer.parseInt(s.substring(0, 4));
		int j = Integer.parseInt(s.substring(4, 6));
		int k = Integer.parseInt(s.substring(6, 8));
		byte byte0 = 31;
		if (j < 1 || j > 12)
			return false;
		if (j == 4 || j == 6 || j == 9 || j == 11)
			byte0 = 30;
		else if (j == 2)
			if (i % 400 == 0)
				byte0 = 29;
			else if (i % 100 == 0)
				byte0 = 28;
			else if (i % 4 == 0)
				byte0 = 29;
			else
				byte0 = 28;
		return k >= 1 && k <= byte0;
	}

	/**	是否身份证号码	*/
	public static boolean isIdentification(String cardid) {
		return CheckCID.verify(cardid);
	}

	/**
	 * 身份证号码是否合法验证
	 * @author lucky
	 *
	 */
	public static class CheckCID {

		private final static int[] ID_WI = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1};

		private final static String[] ID_VI = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};

		private static int[] ai = new int[18];

		protected static boolean verify(String cardid) {
			try {
				if (cardid.length() == 15) {
					if(isCardid15(cardid)){
						cardid = uptoEighteen(cardid);
					}else{
						return false;
					}
				}
				if(!isCardid18(cardid)){
					return false;
				}
				String verifyCode = cardid.substring(17, 18);	//取得身份证最后一位的校验码
				//验证最后一位的校验码是否正确一致
				if (verifyCode.equalsIgnoreCase(getVerifyCode(cardid))) {	//校验码验证，忽略大小写
					return true;
				} else{
					return false;
				}

			} catch(EmptyStackException e) {
				return false;
			}
		}

		/**	返回计算后的18位身份证号码的校验码，应该和号码最后一位一致	*/
		private static String getVerifyCode(String eighteencardid) {
			int remaining = 0;
			if (eighteencardid.length() == 18) {
				eighteencardid = eighteencardid.substring(0, 17);
			}
			if (eighteencardid.length() == 17)  {
				int sum = 0;
				for (int i = 0; i < 17; i++) {
					String k = eighteencardid.substring(i, i + 1);
					ai[i] = Integer.parseInt(k);
				}
				for (int i = 0; i < 17; i++) {
					sum = sum + ID_WI[i] * ai[i];
				}
				remaining = sum % 11;
			}
			return ID_VI[remaining];
		}

		private static boolean isCardid15(String cardid) {
			if (cardid == null || cardid.length() != 15) {
				return false;
			}
			String strTemp = "0123456789";
			for (int i = 0; i < cardid.length(); i++) {
				if (strTemp.indexOf(cardid.charAt(i)) == -1) {
					return false;
				}
			}
			return true;
		}

		private static boolean isCardid18(String cardid) {
			if (cardid == null || cardid.length() != 18) {
				return false;
			}
			String strTemp = "0123456789xX";
			for (int i = 0; i < cardid.length(); i++) {
				if (strTemp.indexOf(cardid.charAt(i)) == -1) {
					return false;
				}
			}
			return true;
		}

		/**	把15位身份证号码自动升级为18为	*/
		private static String uptoEighteen(String fifteencardid)
		{
			String eighteencardid = fifteencardid.substring(0,6);
			eighteencardid = eighteencardid + "19";
			eighteencardid = eighteencardid + fifteencardid.substring(6,15);
			eighteencardid = eighteencardid + getVerifyCode(eighteencardid);
			return eighteencardid;
		}

	}

}
