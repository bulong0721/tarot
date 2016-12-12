package com.myee.tarot.core;

import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class Constants {

	public static final String SESSION_SECURITY_CODE = "sessionSecurityCode";
	public static final String REQUEST_SECURITY_CODE = "securityCode";
	public final static String TEST_ENVIRONMENT       = "TEST";
	public final static String PRODUCTION_ENVIRONMENT = "PRODUCTION";
	public final static String SHOP_URI               = "/shop";
	public final static String ACCESS_DENI_MESSAGE    = "detailMessage";

	public static final String ALL_REGIONS = "*";

	public final static String DEFAULT_DATE_FORMAT      = "yyyy-MM-dd";
	public final static String DEFAULT_DATE_FORMAT_YEAR = "yyyy";

	public final static String ADMIN_STORE = "ADMIN_STORE";
	public final static String ADMIN_USER = "ADMIN_USER";
	public final static String ADMIN_MERCHANT = "ADMIN_MERCHANT";//管理员切换商户

	public final static String CUSTOMER_STORE = "CUSTOMER_STORE";
	public final static String CUSTOMER_USER = "CUSTOMER_USER";
	public final static String CUSTOMER_MERCHANT = "CUSTOMER_MERCHANT";//管理员切换商户

	public final static String FILE_IS_EXIST = "fileIsExist";//文件是否存在

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

	public final static int COUNT_NOPAGING = -1;//不分页
	public final static int COUNT_PAGING_MARK = 0;//分页标准

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

	public final static Boolean PERMISSION_ONLY_ADMIN = true;
	public final static Boolean PERMISSION_ALL = false;
	public final static String PERMISSION_TYPE_ALL = "1";
    public final static int PERMISSION_TREE_LEAF = 1;
    public final static int PERMISSION_TREE_PARENT = 0;



	public final static int CLIENT_PRIZE_TICKET_YES = 1;
	public final static int CLIENT_PRIZE_TICKET_NO = 0;
	public final static int CLIENT_PRIZE_TICKET_EXPIRE = 2;

	public final static int CLIENT_PRIZE_TYPE_PHONE = 0;
	public final static int CLIENT_PRIZE_TYPE_SCANCODE = 1;
	public final static int CLIENT_PRIZE_TYPE_THANKYOU = 2;

	public final static int CLIENT_PRIZEINFO_STATUS_UNGET = 0;
	public final static int CLIENT_PRIZEINFO_STATUS_GET = 1;
	public final static int CLIENT_PRIZEINFO_STATUS_USED = 2;
	public final static int CLIENT_PRIZEINFO_STATUS_EXPIRED = 3;

	public final static int CLIENT_PRICE_PHONE_OBJECT = 0; //实物
	public final static int CLIENT_PRICE_PHONE_CINEMA = 1; //电影票
	public final static int CLIENT_PRICE_PHONE_CMB_CREDIT_CARD = 2;//招商银行信用卡

	public final static String REQUEST_INFO_SESSION = "sessionName";
	public final static String REQUEST_INFO_USERTYPE = "userType";
	public final static String REQUEST_INFO_USERID = "userId";//根据userType，从adminUser表取Id还是从Custom表取Id

	public final static String PRICEDRAW = "PRICEDRAW";

	public final static String VOICELOG_BAK = "voicelogbak"; //存放语音日志备份

	public final static String METRICS_BAK = "metricsbak"; //存放采集日志备份

	public final static String METRICS_REOCRDS = "metricsrecords"; //存放采集日志备份

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


    public static final String BOARD_UPDATE_BASEPATH = "100/version/boardUpdate/";//自研主板升级基础路径
    public static final String MODULE_UPDATE_BASEPATH = "100/version/moduleUpdate/";//模块升级基础路径
    public static final String APP_UPDATE_BASEPATH = "100/version/appUpdate/";//应用升级基础路径

    public static final int SUPERMAN_EVALUATION_AVG = 1; //服务评价的类型-查平均值
    public static final int SUPERMAN_EVALUATION_DETAIL_LIST = 2; //服务评价的类型-查详情列表

	public static final int SUPERMAN_ACTION_ORDER                    = 1;//点餐加菜
	public static final int SUPERMAN_ACTION_WATER                    = 2;//添加茶水
	public static final int SUPERMAN_ACTION_PAPER                    = 3;//湿巾纸巾
	public static final int SUPERMAN_ACTION_CALL_PAY                 = 4;//呼叫结账
	public static final int SUPERMAN_ACTION_OTHER                    = 5;//其他服务
	public static final int SUPERMAN_ACTION_DEMO                     = 6;//DEMO引导
	public static final int SUPERMAN_ACTION_EVALUATE                 = 7;//服务评价
	public static final int SUPERMAN_ACTION_PAY                      = 8;//自助买单
	public static final int SUPERMAN_ACTION_DISCOUNT                 = 9;//优惠专区
	public static final int SUPERMAN_ACTION_DRIVING                  = 10;//代驾服务
	public static final int SUPERMAN_ACTION_PROMOTION                = 11;//本店推介
	public static final int SUPERMAN_ACTION_WEATHER                  = 12;//今日天气
	public static final int SUPERMAN_ACTION_NEWS                     = 13;//今日头条
	public static final int SUPERMAN_ACTION_SURROUND_ENTERTAINMENT   = 14;//周边玩乐
	public static final int SUPERMAN_ACTION_STORE_ONLINE             = 15;//在线商场
	public static final int SUPERMAN_ACTION_SURROUND_DISCOUNT        = 16;//周边优惠
	public static final int SUPERMAN_ACTION_VIDEO_FUN                = 17;//视频娱乐
	public static final int SUPERMAN_ACTION_MAGAZINE                 = 18;//电子杂志
	public static final int SUPERMAN_ACTION_GAME                     = 19;//手游试玩
	public static final int SUPERMAN_ACTION_SETTING                  = 20;//设置
	public static final int SUPERMAN_ACTION_ACTIVITY_DISCOUNT        = 21;//优惠专区活动
	public static final int SUPERMAN_ACTION_ACTIVITY_STORE           = 22;//本店推介活动
	public static final int SUPERMAN_ACTION_DEVICE_START             = 23;//开机时间
	public static final int SUPERMAN_ACTION_DEVICE_CLOSE             = 24;//关机时间
	public static final int SUPERMAN_ACTION_EVALUATE_SUBMIT          = 25;//评价提交
	public static final int SUPERMAN_ACTION_REWARD                   = 26;//打赏
	public static final int SUPERMAN_ACTION_VIDEO_PLAY               = 27;//视频界面
	public static final int SUPERMAN_ACTION_TIDY                     = 28;//收拾桌面
	public static final int SUPERMAN_ACTION_ADD_SOUP                 = 29;//火锅加汤
	public static final int SUPERMAN_ACTION_CHANGE_DISHWARE          = 30;//更换餐具
	public static final int SUPERMAN_ACTION_VIDEO_AD_CLICK           = 31;//视频广告详情
	public static final int SUPERMAN_ACTION_PRIZE_GOIN               = 32;//抽奖活动
	public static final int SUPERMAN_ACTION_PRIZE_CLICKDRAW          = 33;//点击抽奖
	public static final int SUPERMAN_ACTION_PRIZE_INPUTPHONE         = 34;//输入手机号
	public static final int SUPERMAN_ACTION_PRIZE_RECEIVEGOODS       = 35;//领取奖品
	public static final int SUPERMAN_ACTION_PRIZE_SERVER_LUCKYID     = 36;//云端奖券
	public static final int SUPERMAN_ACTION_RESERVE                  = 100;//预留功能
	public static final int SUPERMAN_ACTION_DRIVING_URL              = 1000;//代驾跳转

	public static final String VOICE_LOG_TYPE_CHAT = "聊天";
	public static final String VOICE_LOG_TYPE_TALKSHOW = "脱口秀";

	public static final String ES_QUERY_INDEX = "log";
	public static final String ES_QUERY_TYPE = "voiceLog";

	public static final int WEIXIN_PRIZEDRAW = 1;  //抽奖
	public static final int WEIXIN_LINEPROCESS= 2;   //排队进展

	public static final int APPINFO_TYPE_SERVICE = 1;  //服务
	public static final int APPINFO_TYPE_PROCESS = 2;  //服务
	public static final int APPINFO_TYPE_APP= 3;   //进程

	public static final int METRIC_STATE_OK = 0;  //指标状态正常
	public static final int METRIC_STATE_WARN = 1;  //指标状态警告
	public static final int METRIC_STATE_ALERT = 2;  //指标状态报警

	public static final int METRIC_DETAIL_VALUE_TYPE_NUM = 0;  //数值不带报警
	public static final int METRIC_DETAIL_VALUE_TYPE_STRING = 1;  //字符串
	public static final int METRIC_DETAIL_VALUE_TYPE_NUM_ALERT = 2;  //数值带报警

	public static final int METRIC_QUERY_TYPE_ALL = 0;  //0:表示是总览详细指标，查询出相关的其他服务、进程等参数；
	public static final int METRIC_QUERY_TYPE_SINGLE = 1;  //1:表示是单个指标的详细，不查询出其他服务、进程等相关参数

	public static final long PERIOD_1_HOUR = 3600000L;  //1小时毫秒数

	public static final List<String> METRICS_4_SUMMARY_KEY_LIST = Arrays.asList(//给总览指标用的动态指标，比如cpuUsed/cpuTotal
			com.myee.djinn.constants.Constants.METRIC_cpuUsed,
			com.myee.djinn.constants.Constants.METRIC_productPower,
			com.myee.djinn.constants.Constants.METRIC_bluetoothState,
			com.myee.djinn.constants.Constants.METRIC_wifiStatus,
			com.myee.djinn.constants.Constants.METRIC_volume,
			com.myee.djinn.constants.Constants.METRIC_romUsed,
			com.myee.djinn.constants.Constants.METRIC_ramUsed,
			com.myee.djinn.constants.Constants.METRIC_charging,
			com.myee.djinn.constants.Constants.METRIC_SSID,
			com.myee.djinn.constants.Constants.METRIC_productLocalIP,
			com.myee.djinn.constants.Constants.SUMMARY_productGlobalIP
	);

	public static final String METRICS_NEED_TIME_KEY_LIST_NAME = "metricsNeedTimeKeyList";
	public static final List<String> METRICS_NEED_TIME_KEY_LIST = Arrays.asList(
			com.myee.djinn.constants.Constants.METRIC_cpuUsed,
			com.myee.djinn.constants.Constants.METRIC_productPower,
			com.myee.djinn.constants.Constants.METRIC_romUsed,
			com.myee.djinn.constants.Constants.METRIC_ramUsed,
			com.myee.djinn.constants.Constants.METRIC_volume,
			com.myee.djinn.constants.Constants.METRIC_wifiRssi
	);

	public static final String METRICS_NO_TIME_KEY_LIST_NAME = "metricsNoTimeKeyList";
	public static final List<String> METRICS_NO_TIME_KEY_LIST = Arrays.asList(
			com.myee.djinn.constants.Constants.METRIC_bluetoothState,
			com.myee.djinn.constants.Constants.METRIC_charging,
			com.myee.djinn.constants.Constants.METRIC_productLocalIP,
			com.myee.djinn.constants.Constants.METRIC_SSID,
			com.myee.djinn.constants.Constants.METRIC_wifiStatus
	);

	public static final List<String> SUMMARY_KEY_LIST = Arrays.asList(
			com.myee.djinn.constants.Constants.SUMMARY_brand,
			com.myee.djinn.constants.Constants.SUMMARY_computerType,
			com.myee.djinn.constants.Constants.SUMMARY_cpuName,
			com.myee.djinn.constants.Constants.SUMMARY_deviceMode,
			com.myee.djinn.constants.Constants.SUMMARY_language,
			com.myee.djinn.constants.Constants.SUMMARY_macAddress,
			com.myee.djinn.constants.Constants.SUMMARY_manufacturer,
			com.myee.djinn.constants.Constants.SUMMARY_maxFrequency,
			com.myee.djinn.constants.Constants.SUMMARY_minFrequency,
			com.myee.djinn.constants.Constants.SUMMARY_osKernelVersion,
			com.myee.djinn.constants.Constants.SUMMARY_osUUID,
			com.myee.djinn.constants.Constants.SUMMARY_osVersion,
			com.myee.djinn.constants.Constants.SUMMARY_ramTotal,
			com.myee.djinn.constants.Constants.SUMMARY_resolution,
			com.myee.djinn.constants.Constants.SUMMARY_romTotal,
			com.myee.djinn.constants.Constants.SUMMARY_productGlobalIP,
			com.myee.djinn.constants.Constants.SUMMARY_serial
	);

	public static final List<Long> METRICS_SELECT_RANGE_LIST = Arrays.asList(
			3600000L,7200000L,14400000L,43200000L,86400000L,604800000L,2592000000L,31536000000L
	);

	//1小时范围监控用30秒的频率
	public static final int ONE_HOUR_POINT_COUNT = 60 * 60 / 30;
	//1小时的间隔毫秒数
	public static final int INTERVAL_ONE_HOUR = 30 * 1000;

	//2小时范围监控用1分30秒的频率
	public static final int TWO_HOUR_POINT_COUNT = 2 * 60 * 60 / 90;
	//2小时的间隔毫秒数
	public static final int INTERVAL_TWO_HOUR = 90 * 1000;

	//4小时范围监控用5分钟的频率
	public static final int FOUR_HOUR_POINT_COUNT = 4 * 60 * 60 / 300;
	//4小时的间隔毫秒数
	public static final int INTERVAL_FOUR_HOUR = 300 * 1000;

	//12小时范围监控用5分钟的频率
	public static final int HALF_DAY_POINT_COUNT = 12 * 60 * 60 / 300;
	//12小时的间隔毫秒数
	public static final int INTERVAL_HALF_DAY = 300 * 1000;

	//24小时范围监控用5分钟的频率
	public static final int ONE_DAY_POINT_COUNT = 24 * 60 * 60 / 300;
	//24小时的间隔毫秒数
	public static final int INTERVAL_ONE_DAY = 300 * 1000;

	//1周范围监控用5分钟的频率
	public static final int ONE_WEEK_POINT_COUNT = 7 * 24 * 60 * 60 / 3600;
	//1周的间隔毫秒数
	public static final int INTERVAL_ONE_WEEK = 3600 * 1000;

	//1个月范围监控用5分钟的频率
	public static final int ONE_MONTH_POINT_COUNT = 30 * 24 * 60 * 60 / 3600;
	//1个月的间隔毫秒数
	public static final int INTERVAL_ONE_MONTH = 3600 * 1000;

	//1年范围监控用5分钟的频率
	public static final int ONE_YEAR_POINT_COUNT = 365 * 24 * 60 * 60 / 3600 * 24;
	//1年的间隔毫秒数
	public static final int INTERVAL_ONE_YEAR = 3600 * 24 * 1000;

	public static final int METRIC_EXPORT_BUFFER_SIZE = 1000;

	public static final String SEARCH_OPTION_NAME = "name";

	public static final String SEARCH_OPTION_CODE = "code";

	public static final String SEARCH_OPTION_PHONE = "phone";

	public static final String SEARCH_OPTION_ADDRESS = "address.address";

	public static final String SEARCH_OPTION_ADDRESS_PROVINCE = "address.province.id";

	public static final String SEARCH_OPTION_ADDRESS_CITY = "address.city.id";

	public static final String SEARCH_OPTION_BUSINESS_TYPE = "businessType";

	public static final String SEARCH_OPTION_CUISINE_TYPE = "cuisineType";

	public static final String SEARCH_OPTION_DESCRIPTION = "description";

	public static final String SEARCH_OPTION_TYPE = "type";

	public static final String SEARCH_OPTION_PHONE_PRIZE_TYPE = "phonePrizeType";

	public static final String SEARCH_OPTION_TABLE_TYPE = "tableType.id";

	public static final String SEARCH_OPTION_TABLE_ZONE = "tableZone.id";

	public static final String SEARCH_OPTION_SCAN_CODE = "scanCode";

	public static final String SEARCH_OPTION_TEXT_ID = "textId";

	public static final String SEARCH_OPTION_VERSION_NUM = "versionNum";

	public static final String SEARCH_OPTION_STORE_NAME = "store.name";

	public static final String SEARCH_OPTION_BOARD_NO = "boardNo";

	public static final String SEARCH_OPTION_DEVICE_NUM = "deviceNum";

	public static final String SEARCH_OPTION_PRODUCT_NUM = "productNum";

	public static final String SEARCH_OPTION_LOGIN = "login";

	public static final String SEARCH_OPTION_PHONE_NUMBER = "phoneNumber";

	public static final String SEARCH_OPTION_EMAIL = "email";

	public static final String SEARCH_USER_NAME = "username";

	public static final String SEARCH_EMAIL_ADDRESS = "emailAddress";

	public static final String STATIC_METRICS = "staticMetrics";
	public static final String SEARCH_CREATE_TIME = "createTime";

	public static final String SUMMARY_NODE = "/monitor/summary/";
	public static final String SEARCH_NOTICE_TYPE = "noticeType";

	public static final String SEARCH_CONTENT = "content";

	public static final String SEARCH_BEGIN_DATE = "beginDate";

	public static final String SEARCH_END_DATE = "endDate";

}
