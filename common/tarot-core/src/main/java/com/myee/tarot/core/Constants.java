package com.myee.tarot.core;

import java.util.Currency;
import java.util.Locale;

public class Constants {

	public final static String TEST_ENVIRONMENT       = "TEST";
	public final static String PRODUCTION_ENVIRONMENT = "PRODUCTION";
	public final static String SHOP_URI               = "/shop";

	public static final String ALL_REGIONS = "*";


	public final static String DEFAULT_DATE_FORMAT      = "yyyy-MM-dd";
	public final static String DEFAULT_DATE_FORMAT_YEAR = "yyyy";

	public final static String ADMIN_STORE = "ADMIN_STORE";
	public final static String ADMIN_USER = "ADMIN_USER";
	public final static String ADMIN_MERCHANT = "ADMIN_MERCHANT";//管理员切换商户

	public final static String CUSTOMER_STORE = "CUSTOMER_STORE";
	public final static String CUSTOMER_USER = "CUSTOMER_USER";
	public final static String CUSTOMER_MERCHANT = "CUSTOMER_MERCHANT";//管理员切换商户

	public final static String EMAIL_CONFIG    = "EMAIL_CONFIG";
	public final static String MERCHANT_CONFIG = "MERCHANT_CONFIG";

	public final static String UNDERSCORE                = "_";
	public final static String SLASH                     = "/";
	public final static String BACKSLASH                 = "\\";
	public final static String TRUE                      = "true";
	public final static String FALSE                     = "false";
	public final static String OT_ITEM_PRICE_MODULE_CODE = "itemprice";
	public final static String OT_SUBTOTAL_MODULE_CODE   = "subtotal";
	public final static String OT_TOTAL_MODULE_CODE      = "total";
	public final static String OT_SHIPPING_MODULE_CODE   = "shipping";
	public final static String OT_HANDLING_MODULE_CODE   = "handling";
	public final static String OT_TAX_MODULE_CODE        = "tax";
	public final static String OT_REFUND_MODULE_CODE     = "refund";

	public final static Locale   DEFAULT_LOCALE   = Locale.US;
	public final static Currency DEFAULT_CURRENCY = Currency.getInstance(Locale.US);

	public final static int NOPAGING = 1;//不分页
	public final static int PAGING = 0;//分页

	//抽奖 奖项状态
	public final static int PRICEINFO_USED = 0; //已使用
	public final static int PRICEINFO_UNUSED = 1; //未使用
	public final static int PRICEINFO_EXPIRE= 2; //EXPIRE过期

	//启用奖券状态
	public final static int PRICE_START = 1; //启用
 	public final static int PRICE_END = 0; //未启用

	//启用活动状态
	public final static int ACITIVITY_START = 0; //活动存在，但未激活
	public final static int ACITIVITY_END = 1; //活动激活，但已结束
	public final static int ACTIVITY_ACTIVE =2; //活动激活

	//活动或奖券逻辑删除
	public final static int DELETE_NO = 0; //启用
	public final static int DELETE_YES = 1; //删除

	//微信抽奖是否开启
	public final static int WECHAT_OPEN = 0; //开启
	public final static int WECHAT_CLOSE = 1;  //关闭

	public final static String PRICEDRAW = "PRICEDRAW";

	public final static String VOICELOGBAK = "voicelogbak"; //存放语音日志备份

	public final static String VOICELOG = "voicelog"; //存放所有语音日志

}
