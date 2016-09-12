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

	//ES查询模式 0:should  1:must
	public final static int ES_QUERY_PATTERN_SHOULD = 0;
	public final static int ES_QUERY_PATTERN_MUST = 1;

	//小超人 抽奖券开启状态
	public final static Boolean CLIENT_PRIZE_ACTIVE_YES = true;
	public final static Boolean CLIENT_PRIZE_ACTIVE_NO = false;

	public final static Boolean CLIENT_PRIZE_DELETE_YES = true;
	public final static Boolean CLIENT_PRIZE_DELETE_NO = false;

	public final static Boolean CLIENT_PRIZE_TICKET_YES = true;
	public final static Boolean CLIENT_PRIZE_TICKET_NO = false;

	public final static int CLIENT_PRIZE_TYPE_PHONE = 0;
	public final static int CLIENT_PRIZE_TYPE_SCANCODE = 1;
	public final static int CLIENT_PRIZE_TYPE_THANKYOU = 2;

	public final static int CLIENT_PRIZEINFO_STATUS_UNGET = 0;
	public final static int CLIENT_PRIZEINFO_STATUS_GET = 1;
	public final static int CLIENT_PRIZEINFO_STATUS_USED = 2;
	public final static int CLIENT_PRIZEINFO_STATUS_EXPIRED = 3;

	public final static int CLIENT_PRICE_PHONE_OBJECT = 0; //实物
	public final static int CLIENT_PRICE_PHONE_CINEMA = 1; //电影票

	public final static String REQUEST_INFO_SESSION = "sessionName";
	public final static String REQUEST_INFO_USERTYPE = "userType";
	public final static String REQUEST_INFO_USERID = "userId";//根据userType，从adminUser表取Id还是从Custom表取Id

	public final static String PRICEDRAW = "PRICEDRAW";

	public final static String VOICELOG_BAK = "voicelogbak"; //存放语音日志备份

	public final static String VOICELOG = "voicelog"; //存放所有语音日志

	public final static String ADMIN_PACK = "100";//店铺100

	public final static String WAITTOKEN = "waittoken";//存放排号数据文件

	public final static String WAITTOKEN_BAK = "waittokenbak";//存放排号数据备份

	public static final String ALLOW_EDITOR_TEXT = "((txt)|(csv)|(log)|(xml)|(html)|(htm)|(js)|(css))";	//只允许编辑的文本格式（正则）

	public final static String MY_LOTTERY_LIST_URL = "http://www.myee7.com/tarot_test/customerClient/index.html";

	public final static String MY_LOTTERY_DETAIL_URL = "http://www.myee7.com/tarot_test/customerClient/index.html#!/myCouponView/";

	public static final String OFF_ALLOW_EXCEL = "((doc)|(wps)|(vsd)|(docx))";	//只允许符合此规则的excel（正则）
	public static final String OFF_ALLOW_IMAGE = "((jpg)|(png)|(bmp))";	//只允许符合此规则的图片（正则）
	public static final String OFF_ALLOW_VIDEO = "((rmvb)|(mp4)|(avi)|(wav))";	//只允许符合此规则的视频（正则）
	public static final String OFF_ALLOW_AUDIO = "((mp3)|(vmv)|(wav))";	//只允许符合此规则的音频（正则）

	//本店活动,普通用户账号
	public final static int API_OLD_TYPE_SHOP = 0;
	//木爷活动，木爷管理员账号
	public final static int API_OLD_TYPE_MUYE = 1;
	//木爷活动最大发布数量
	public final static int ROLL_MAIN_PUBLISH_MAX = 5;
	//本店活动最大发布数量
	public final static int ROLL_MAIN_SHOP_MAX = 10;
	//木爷视频最大发布数量
	public final static int VIDEO_PUBLISH_MAX = 30;
	//木爷素材最大发布数量
	public final static int MATERIAL_PUBLISH_MAX = 30;
	//木爷广告最大发布数量
	public final static int ADVERTISEMENT_PUBLISH_MAX = 5;

	//文件校验类型
	public final static int FILE_VALID_TYPE_DEFAULT = 0;
	public final static int FILE_VALID_TYPE_APK = 1;
	public final static int FILE_VALID_TYPE_TEXT = 2;
	public final static int FILE_VALID_TYPE_AUDIO = 3;
	public final static int FILE_VALID_TYPE_VIDEO = 4;
	public final static int FILE_VALID_TYPE_IMAGE = 5;
	public final static int FILE_VALID_TYPE_BIN = 6;//二进制文件

	public static final String UPLOAD_IMAGE_PATH = "picture/";
	public static final String UPLOAD_VIDEO_PATH = "video/";
	public static final String UPLOAD_AUDIO_PATH = "audio/";
	public static final String UPLOAD_MATERIAL_PATH = "material/";
	public static final String UPLOAD_DEFAULT_PATH = "default/";

	public static final int SUPERMAN_EVALUATION_AVG = 1; //服务评价的类型-查平均值
	public static final int SUPERMAN_EVALUATION_DETAIL_LIST = 2; //服务评价的类型-查详情列表
}
