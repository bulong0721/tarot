package com.myee.tarot.web.apiold.controller.v10;

import com.google.gson.Gson;
import com.myee.tarot.apiold.bean.DeadAuthInfo;
import com.myee.tarot.apiold.domain.AkSk;
import com.myee.tarot.apiold.service.AkSkService;
import com.myee.tarot.apiold.service.SendRecordService;
import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.catering.domain.Table;
import com.myee.tarot.catering.service.TableService;
import com.myee.tarot.web.apiold.controller.BaseController;
import com.myee.tarot.apiold.eum.TemplateSMSType;
import com.myee.tarot.web.apiold.util.*;
import com.myee.tarot.web.util.DateUtil;
import com.myee.tarot.weixin.domain.ClientAjaxResult;
import me.chanjar.weixin.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Info: 第三方合作方接口
 * User: enva.liang@clever-m.com
 * Date: 2016-03-29
 * Time: 15:29
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */
@RestController
public class PartnerApiManageController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(PartnerApiManageController.class);

    @Autowired
    private AkSkService akSkManageService;

    @Autowired
    private SendRecordService sendRecordManageService;

    @Autowired
    private TableService tableManageService;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    /**
     * 点点笔发送短信接口
     * @return
     */
    @RequestMapping(value = "/api/v10/sms/smartPenSend")
    public ClientAjaxResult smartPenSendSMS(@RequestParam(value = "sendToken") String sendToken,
                                            @RequestParam(value = "tableId") Long tableId,
                                            @RequestParam(value = "templateId") int templateId) {
        logger.info("木爷发送短信,tableId:" + tableId + ",短信发送模板, template:" +templateId + ",发送sendToken:" + sendToken);
        try {
            if (!paraJude(templateId)) {
                return ClientAjaxResult.failed("参数出错...");
            }
            Table table = tableManageService.findById(tableId);
            if(table == null){
                return ClientAjaxResult.failed("参数出错...");
            }
            String templateNum = TemplateSMSType.getName(templateId);
            String mobiles = "";
            List<DeviceUsed> deviceUsedList = table.getDeviceUsed();
            if(deviceUsedList != null && deviceUsedList.size() > 0){
                for(DeviceUsed deviceUsed : deviceUsedList){
                    if(StringUtils.isBlank(deviceUsed.getPhone())){
                        mobiles += deviceUsed.getPhone();
                    }
                }
            }
            ClientAjaxResult result =  this.checkArgsMyee(sendToken, mobiles, templateId, getRequest());
            if(result.getCode() != null && !result.getCode().equals("1000")){
                return result;
            }

            Runnable task = AlidayuSmsClient.sendSMS(
                    IPUtils.getIpAddr(getRequest()),
                    table,
                    sendRecordManageService,
                    templateNum,
                    null,//签名,为null默认是美味点点笔
                    getCommConfig(),//从commonApi.properties获取默认配置
                    System.currentTimeMillis()
            );//发送短信

            taskExecutor.submit(task);
            return ClientAjaxResult.success();
        }  catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return ClientAjaxResult.failed("糟了...系统出错了...");
        }
    }

    /**
     * 点点笔判断传入参数,如果合法返回true
     * @param templateId
     * @return
     */
    public boolean paraJude(int templateId){
        if(templateId < TemplateSMSType.ADDFOOD.getIndex()
                || templateId > TemplateSMSType.OUT_OF_CHARGING.getIndex()
                || (templateId > TemplateSMSType.BATTERIES.getIndex()
                && templateId < TemplateSMSType.PENPULLOUT.getIndex())
                || templateId <= 0){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 第三方发送短信接口
     * @param mobiles：短信号码，多个号码以逗号分隔
     * @param template:短信模板
     * @return
     */
    @RequestMapping(value = "/api/partner/sms/send")
    public ClientAjaxResult queryVideoList(@RequestParam(value = "sendToken") String sendToken,
                                           @RequestParam(value = "mobiles") String mobiles,
                                              @RequestParam(value = "template") int template) {
        logger.info("第三方发送短信,mobiles:" + mobiles + "短信发送模板, template:" +template + "发送sendToken:" + sendToken);
        try {
            ClientAjaxResult result =  this.checkArgs(sendToken, mobiles, template, getRequest());
            if(result.getCode() != null && !result.getCode().equals("1000")){
                return result;
            }
            Runnable task = AlidayuSmsClient.sendSMS(
                    IPUtils.getIpAddr(getRequest()),
                    sendRecordManageService,
                    mobiles.split(","),
                    "短信内容",//短信模板
                    TemplateSMSType.getName(template),
                    "酷奇",//签名
                    getCommConfig(),
                    System.currentTimeMillis());//发送短信
            taskExecutor.submit(task);
            return ClientAjaxResult.success();
        }  catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            return ClientAjaxResult.failed("糟了...系统出错了...");
        }
    }

    /**
     * 第三方发短信验证
     * @param sendToken
     * @param mobiles
     * @param template
     * @param request
     * @return
     */
    private ClientAjaxResult checkArgs(String sendToken, String mobiles, int template, HttpServletRequest request){
        if(StringUtils.isBlank(sendToken) || StringUtils.isBlank(mobiles) || template <= 0){
            return ClientAjaxResult.failed("args is null", "999");
        }
        if(TemplateSMSType.PARTNERGAME.getIndex() != template){
            return ClientAjaxResult.failed("template error", "999");
        }
        String[] token = sendToken.split(":");
        if(token == null || (token != null && token.length < 3)){
            return ClientAjaxResult.failed("token is empty", "999");
        }
        if(StringUtils.isBlank(token[0])){
            return ClientAjaxResult.failed("key is empty", "999");
        }
        AkSk akSk = akSkManageService.getByToken(token[0]);
        if(akSk == null){
            return ClientAjaxResult.failed("key Not exist", "999");
        }
        if(akSk.getCompanyIp() == null){
            return ClientAjaxResult.failed("data exception", "999");
        }
        if(IPUtils.getIpAddr(request) == null){
            return ClientAjaxResult.failed("ip Not correct", "999");
        }
        if(!IPUtils.getIpAddr(request).equals(akSk.getCompanyIp())){
            return ClientAjaxResult.failed("ip no bindings", "999");
        }
        if(StringUtils.isBlank(token[2])){
            return ClientAjaxResult.failed("token Not correct", "999");
        }
        String deadStr = new String(Base64.decode(token[2]));
        DeadAuthInfo deadAuthInfo = null;
        try {
            deadAuthInfo = (new Gson()).fromJson(deadStr, DeadAuthInfo.class);
        }catch (Exception e){
        }
        if(deadAuthInfo == null){
            return ClientAjaxResult.failed("dead error", "999");
        }
        if(deadAuthInfo.getDeadline() < DateUtil.getSecondsOfCurrentMillis()){
            return ClientAjaxResult.failed("dead Not correct", "999");
        }
        Auth auth = Auth.create(akSk.getAccessKey(), akSk.getSecretKey());
        String authToken = auth.sendTokenWithDeadline(deadAuthInfo.getDeadline());
        if(!StringUtils.isBlank(authToken) && authToken.equals(sendToken)){
            return ClientAjaxResult.success();
        }else{
            return ClientAjaxResult.failed("token exception", "999");
        }
    }

    /**
     * 点点笔发短信权限验证
     * @param sendToken
     * @param mobiles
     * @param template
     * @param request
     * @return
     */
    private ClientAjaxResult checkArgsMyee(String sendToken, String mobiles, int template, HttpServletRequest request){
        if(StringUtils.isBlank(sendToken) || StringUtils.isBlank(mobiles) || template <= 0){
            return ClientAjaxResult.failed("args is null", "999");
        }
        String[] token = sendToken.split(":");
        if(token == null || (token != null && token.length < 3)){
            return ClientAjaxResult.failed("token is empty", "999");
        }
        if(StringUtils.isBlank(token[0])){
            return ClientAjaxResult.failed("key is empty", "999");
        }
        AkSk akSk = akSkManageService.getByToken(token[0]);
        if(akSk == null){
            return ClientAjaxResult.failed("key Not exist", "999");
        }
        if(StringUtils.isBlank(token[2])){
            return ClientAjaxResult.failed("token Not correct", "999");
        }
        String deadStr = new String(Base64.decode(token[2]));
        DeadAuthInfo deadAuthInfo = null;
        try {
            deadAuthInfo = (new Gson()).fromJson(deadStr, DeadAuthInfo.class);
        }catch (Exception e){
        }
        if(deadAuthInfo == null){
            return ClientAjaxResult.failed("dead error", "999");
        }
        if(deadAuthInfo.getDeadline() < DateUtil.getSecondsOfCurrentMillis()){
            return ClientAjaxResult.failed("dead Not correct", "999");
        }
        Auth auth = Auth.create(akSk.getAccessKey(), akSk.getSecretKey());
        String authToken = auth.sendTokenWithDeadline(deadAuthInfo.getDeadline());
        if(!StringUtils.isBlank(authToken) && authToken.equals(sendToken)){
            return ClientAjaxResult.success();
        }else{
            return ClientAjaxResult.failed("token exception", "999");
        }
    }

    //测试产生token
    public static void main(String[] args) {
        String ACCESSKEY = "QpN9RAOpo1OK54JsXoYPPK0gpdAZzKRfm2TzUqZX";//accessKey，测试机
        String SECRETKEY = "cpB95knP5djLOvCSkqkqqmulMxTfYaUg1GAXlxhm";//secretKey，测试机
        Auth auth = Auth.create(ACCESSKEY, SECRETKEY);
        System.out.println("dddddddddddddd="+auth.sendToken(999990));//设置token的有效期，是ms数
    }

}