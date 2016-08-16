package com.myee.tarot.web.apiold.api.util;

import com.myee.tarot.apiold.domain.SendRecord;
import com.myee.tarot.apiold.service.SendRecordService;
import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.catering.domain.Table;
import com.myee.tarot.catering.service.TableService;
import com.myee.tarot.core.exception.ServiceException;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.merchant.service.MerchantService;
import com.myee.tarot.web.util.ValidatorUtil;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import me.chanjar.weixin.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class AlidayuSmsClient {

	private final static Logger logger = LoggerFactory.getLogger(AlidayuSmsClient.class);

	private static String HTTP_API_URL = "http://gw.api.taobao.com/router/rest";//通用接口
	private static String API_APPKEY = "23294088";//key
	private static String API_SECRET = "d74e55c6f848475eef5ca6cc867c8ec6";//secret
	private static String SIGN_NAME = "美味点点笔";//签名,在签名中声明的签名名称
	private static String SMS_TYPE = "normal";//短信类型,短信固定不需要变化
	private static String SEND_PARAM = "table";//短信类型,在模板里声明的参数名称

	public static final int SEND_SUCCESS = 0;//成功
	public static final int PARA_ERROR = 1;//参数错误
	public static final int SEND_FAIL = 2;//发送失败
	public static final int SEND_EXCEPTION = 3;//发送出现异常

	private AlidayuSmsClient() {
	}

	/**
	 * 内部点点笔异步发短消息
	 * @param tb
	 * @return
	 */
	public static Runnable sendSMS(final String ipAddress,
							  final Table tb,
							  final SendRecordService sendRecordManageService,
							  final String templateNum,
							  final String sign,
							  final CommConfig commConfig,
							  final long st) {
		return new Runnable() {
			@Override
			public void run() {
				try {
					List<DeviceUsed> deviceUsedList = tb.getDeviceUsed();
					if(deviceUsedList == null || deviceUsedList.size() <= 0){
						return;
					}
					List list = new ArrayList();
					for(DeviceUsed deviceUsed : deviceUsedList){
						if(!StringUtils.isBlank(deviceUsed.getPhone()) && ValidatorUtil.isMobile(deviceUsed.getPhone())){//不为空且是手机号才添加
							list.add(deviceUsed.getPhone());
						}
					}
					if(list.size() <= 0){
						return;
					}

					String[] smsNum = (String[])list.toArray(new String[list.size()]);
					final long et = System.currentTimeMillis();
					final Date dt = new Date();

//					int i=0;//测试用
					int i = send(smsNum, tb.getName(), templateNum,sign,commConfig);
					//往数据库记录发送结果
					SendRecord record = new SendRecord();
					record.setStore(tb.getStore());
					record.setTable(tb);
					record.setFlag(i);
					record.setPartner(0);//设置为0，非第三方发送
					record.setTemplateNum(templateNum);
					record.setDescription(MyArrayUtils.merge(smsNum, ",") + ",ip=" + ipAddress + ",tableId=" + tb.getId() + ",templateID=" + templateNum + ",st=" + st + ",et=" + et);
					record.setCreated(dt);
					sendRecordManageService.update(record);
				} catch (Exception e) {
					logger.error("发送短信出错："+e.getMessage(), e);
					e.printStackTrace();
				}
			}
		};
	}

	/**
	 * 第三方合作方异步发短消息
	 * @param smsNum, 号码数组
	 * @param argName, 动态参数
	 * @param templateNum, 模板
	 * @param sign, 签名
	 * @param st, 开始时间
	 * @return
	 */
	public static Runnable sendSMS(final String ip,
							  final SendRecordService sendRecordManageService,
							  final String[] smsNum,
							  final String argName,
							  final String templateNum,
							  final String sign,
							  final CommConfig commConfig,
							  final long st) {
		return new Runnable() {
			@Override
			public void run() {
				try {
					final long et = System.currentTimeMillis();
					final Date dt = new Date();
					int i = send(smsNum, argName, templateNum, sign, commConfig);
					//往数据库记录发送结果
					SendRecord record = new SendRecord();
					record.setStore(null);
					record.setTable(null);
					record.setFlag(i);
					record.setPartner(1);//设置为1，第三方发送
					record.setTemplateNum(templateNum);
					record.setDescription(MyArrayUtils.merge(smsNum, ",") + ",ip=" + ip + ",templateID=" + templateNum + ",st=" + st + ",et=" + et);
					record.setCreated(dt);
					sendRecordManageService.update(record);
				} catch (Exception e) {
					logger.error("发送短信出错："+e.getMessage(), e);
					e.printStackTrace();
				}
			}
		};
	}



	/**
	 * 如果发送失败则尝试重发，一共尝试3次
	 * @param smsNum
	 * @param argName
	 * @param templateNum
	 * @return
	 */
	public static int send(String[] smsNum, String argName, String templateNum, String sign, CommConfig commConfig){
		int retResult = 4;
		for(int i = 0; i < 3; i++){
			int result = AlidayuSmsClient.sendHttpSMS(smsNum, argName, templateNum, sign, commConfig);
			if(result == 0){
				retResult = result;
				break;
			}else{
				retResult = result;
			}
		}
		return retResult;
	}

	/**
	 * 发送短信
	 * @param mobiles 电话号码数组,逗号分割
	 * @param argName 参数名称
	 * @param templateNum 短信模板
	 * @param sign 短信签名
	 * @return
	 */
	public static int sendHttpSMS(String[] mobiles, String argName, String templateNum, String sign, CommConfig commConfig){
		if (mobiles == null || templateNum == null){
			return PARA_ERROR;
		}
		String mobileStr = MyArrayUtils.merge(mobiles, ",");
		TaobaoClient client = new DefaultTaobaoClient(commConfig.getAlidayuHttpApiUrl(), commConfig.getAlidayuApiAppkey(), commConfig.getAlidayuApiSecret());
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setSmsType(commConfig.getAlidayuSmsType());
		req.setSmsFreeSignName(commConfig.getAlidayuSignNameActionPen());
		if(!StringUtils.isBlank(sign)){
			req.setSmsFreeSignName(sign);
		}
		if(!StringUtils.isBlank(argName)){
			req.setSmsParamString("{"+commConfig.getAlidayuSendParam()+":'"+argName+"'}");
		}
		req.setRecNum(mobileStr);
		req.setSmsTemplateCode(templateNum);
		AlibabaAliqinFcSmsNumSendResponse rsp;
		try {
			rsp = client.execute(req);
			logger.info("第三方发送短信,rspBody:" + rsp.getBody());
			if(rsp != null && rsp.isSuccess() == true){
				return SEND_SUCCESS;
			}else{
				return SEND_FAIL;
			}
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("发送短信异常", e);
			return SEND_EXCEPTION;
		}

	}

}
