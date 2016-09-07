package com.myee.tarot.web.weixin.controller;

import com.google.common.collect.Maps;
import com.myee.djinn.dto.WaitTokenState;
import com.myee.tarot.campaign.domain.PriceInfo;
import com.myee.tarot.campaign.service.PriceInfoService;
import com.myee.tarot.core.util.DateTimeUtils;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.StringUtil;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.merchant.service.MerchantStoreService;
import com.myee.tarot.uitl.CacheUtil;
import com.myee.tarot.weixin.domain.ClientAjaxResult;
import com.myee.tarot.weixin.domain.WxWaitToken;
import com.myee.tarot.weixin.service.WeixinService;
import com.myee.tarot.weixin.service.impl.OperationsServiceImpl;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.bean.WxMenu;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * Created by Martin on 2016/1/8.
 */
@Controller
@RequestMapping("weixin/wxmp")
public class WebMpController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebMpController.class);
    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private WeixinService wxService;
    @Autowired
    protected WxMpConfigStorage wxMpConfigStorage;
    @Autowired
    private WxMpMessageRouter wxMpMessageRouter;
    @Autowired
    private OperationsServiceImpl manager;
    @Autowired
    private PriceInfoService priceInfoService;
    @Autowired
    private MerchantStoreService merchantStoreService;

    @Value("${cleverm.push.dirs}")
    private String DOWNLOAD_HOME;

    private static final int QRCODE_EXPIRE_SECONDS = 2592000; //微信公众号二维码有效时间，以秒为单位。 最大不超过2592000（即30天）

    private static final String QUERY_PROGRESS = "1";

    private static final String DRAW_LOTTERY = "2";

    @RequestMapping(value = "service")
    @ResponseBody
    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        LOGGER.info("微信公众号请求信息");
        req.setCharacterEncoding("utf-8");
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        resp.setStatus(HttpServletResponse.SC_OK);

        PrintWriter out = resp.getWriter();
        String signature = req.getParameter("signature");
        String nonce = req.getParameter("nonce");
        String timestamp = req.getParameter("timestamp");
        String echostr = req.getParameter("echostr");

        // 默认返回的文本消息内容
        String respContent = "请求处理异常，请稍候尝试！";
        Map map = Maps.newHashMap();
        try {
            if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
                out.print("非法请求");
            } else if (StringUtil.isNotBlankWeiXin(echostr)) {
                out.write(echostr);
            }
            String encryptType = StringUtil.isBlankWeiXin(req.getParameter("encrypt_type")) ? "raw" : req.getParameter("encrypt_type");
            if ("raw".equals(encryptType)) {
                WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(req.getInputStream());
                LOGGER.info("inMessage:" + inMessage.toString());
                //微信自带的扫二维码，已关注(查询进展用-代号1)
                if (inMessage.getMsgType() != null && inMessage.getMsgType().equals(WxConsts.XML_MSG_EVENT) && inMessage.getEvent() != null && inMessage.getEvent().equals(WxConsts.EVT_SCAN) && inMessage.getEventKey().substring(0, 1).equals("1")) {
                    String sceneId = inMessage.getEventKey().substring(1, inMessage.getEventKey().length());
                    String sceneIdToIdentityCode = CacheUtil.getIdentityCode(Long.valueOf(sceneId));
                    String identityCode = manager.getIdentityCode(sceneIdToIdentityCode);
                    //将该二维码绑定扫码的微信OpenId
                    Map<String, Object> msgMap = Maps.newHashMap();
                    if (wxService.bondQrCodeByScan(identityCode, inMessage.getFromUserName())) {
                        msgMap = checkProgressByIdentityCode(identityCode);
                        int i = wxService.modifyWaitingInfo(Long.parseLong(msgMap.get("waitedTableCount").toString()), identityCode, Long.valueOf(msgMap.get("timeTook").toString()), Long.parseLong(msgMap.get("predictWaitingTime").toString()), Long.parseLong(msgMap.get("tableTypeId").toString()));
                        if (i != 1) {
                            LOGGER.error("修改排号信息失败!");
                        }
                    } else {
                        msgMap.put("valid", "该唯一码已过期或无效，查询进度失败!");
                    }
                    inMessage.setMap(msgMap);
                }
                //微信自带的扫二维码，已关注(获取绑定抽奖信息-代号2)
                if (inMessage.getMsgType() != null && inMessage.getMsgType().equals(WxConsts.XML_MSG_EVENT) && inMessage.getEvent() != null && inMessage.getEvent().equals(WxConsts.EVT_SCAN) && inMessage.getEventKey().substring(0, 1).equals("2")) {
                    Long storeId = Long.valueOf(inMessage.getEventKey().substring(1, inMessage.getEventKey().length()));
                    AjaxResponse aResp = priceInfoService.savePriceInfo(inMessage.getFromUserName(), storeId);
                    PriceInfo priceInfo = (PriceInfo) aResp.getDataMap().get("result");
                    if (priceInfo != null) {
                        Date startDate = priceInfo.getPrice().getStartDate();
                        Date endDate = priceInfo.getPrice().getEndDate();
                        //根据店铺ID查店铺名称
                        MerchantStore merchantStore = merchantStoreService.findById(storeId);
                        map.put("storeName", merchantStore.getName());
                        map.put("prizeStartDate", DateTimeUtils.getDateString(startDate, DateTimeUtils.DEFAULT_DATE_FORMAT_PATTERN_SHORT));
                        map.put("prizeEndDate", DateTimeUtils.getDateString(endDate, DateTimeUtils.DEFAULT_DATE_FORMAT_PATTERN_SHORT));
                        map.put("prizeUrl", Constants.MY_LOTTERY_DETAIL_URL + priceInfo.getId().toString() + "/" + inMessage.getFromUserName());
                        inMessage.setMap(map);
                    } else {
                        map.put("prizeNotAvailable", aResp.getStatusMessage());
                        inMessage.setMap(map);
                    }
                }

                //进公众号里面的扫码(获取绑定抽奖信息-代号2)
                if (inMessage.getMsgType() != null && inMessage.getMsgType().equals(WxConsts.XML_MSG_EVENT) && inMessage.getEvent() != null && inMessage.getEvent().equals(WxConsts.BUTTON_SCANCODE_PUSH) && inMessage.getEventKey().substring(0, 1).equals("2")) {
                    Long storeId = Long.valueOf(inMessage.getEventKey().substring(1, inMessage.getEventKey().length()));
                    AjaxResponse aResp = priceInfoService.savePriceInfo(inMessage.getFromUserName(), storeId);
                    PriceInfo priceInfo = (PriceInfo) aResp.getDataMap().get("result");
                    if (priceInfo != null) {
                        Date startDate = priceInfo.getPrice().getStartDate();
                        Date endDate = priceInfo.getPrice().getEndDate();
                        //根据店铺ID查店铺名称
                        MerchantStore merchantStore = merchantStoreService.findById(storeId);
                        map.put("storeName", merchantStore.getName());
                        map.put("prizeStartDate", DateTimeUtils.getDateString(startDate, DateTimeUtils.DEFAULT_DATE_FORMAT_PATTERN_SHORT));
                        map.put("prizeEndDate", DateTimeUtils.getDateString(endDate, DateTimeUtils.DEFAULT_DATE_FORMAT_PATTERN_SHORT));
                        map.put("prizeUrl", Constants.MY_LOTTERY_DETAIL_URL + priceInfo.getId().toString() + "/" + inMessage.getFromUserName());
                        inMessage.setMap(map);
                    } else {
                        map.put("prizeNotAvailable", aResp.getStatusMessage());
                        inMessage.setMap(map);
                    }
                }

                //点击事件
                if (inMessage.getMsgType() != null && inMessage.getMsgType().equals(WxConsts.XML_MSG_EVENT) && inMessage.getEvent() != null && inMessage.getEvent().equals(WxConsts.EVT_CLICK)) {
                    map = checkProgressByOpenIdState(inMessage.getFromUserName(), WaitTokenState.WAITING.getValue());
                    inMessage.setMap(map);
                }

                //微信自带的扫二维码，未关注
                if (inMessage.getMsgType() != null && inMessage.getMsgType().equals(WxConsts.XML_MSG_EVENT) && inMessage.getEvent() != null && inMessage.getEvent().equals(WxConsts.EVT_SUBSCRIBE) && inMessage.getTicket() != null) {
                    if (inMessage.getEventKey().substring(8, 9).equals(QUERY_PROGRESS)) {
                        String sceneId = inMessage.getEventKey().substring(9, inMessage.getEventKey().length());
                        String sceneIdToIdentityCode = CacheUtil.getIdentityCode(Long.valueOf(sceneId));
                        String identityCode = manager.getIdentityCode(sceneIdToIdentityCode);
                        //将该二维码绑定扫码的微信OpenId
                        if (wxService.bondQrCodeByScan(identityCode, inMessage.getFromUserName())) {
                            map = checkProgressByIdentityCode(identityCode);
                            int i = wxService.modifyWaitingInfo(Long.parseLong(map.get("waitedTableCount").toString()), identityCode, Long.valueOf(map.get("timeTook").toString()), Long.parseLong(map.get("predictWaitingTime").toString()), Long.parseLong(map.get("tableTypeId").toString()));
                            if (i != 1) {
                                LOGGER.error("修改排号信息失败!");
                            }
                        } else {
                            map.put("vaild", "该唯一码已过期或无效，查询进度失败!");
                        }
                        inMessage.setMap(map);
                    } else {
                        Long storeId = Long.valueOf(inMessage.getEventKey().substring(9, inMessage.getEventKey().length()));
                        AjaxResponse aResp = priceInfoService.savePriceInfo(inMessage.getFromUserName(), storeId);
                        PriceInfo priceInfo = (PriceInfo) aResp.getDataMap().get("result");
                        if (priceInfo != null) {
                            Date startDate = priceInfo.getPrice().getStartDate();
                            Date endDate = priceInfo.getPrice().getEndDate();
                            //根据店铺ID查店铺名称
                            MerchantStore merchantStore = merchantStoreService.findById(storeId);
                            map.put("storeName", merchantStore.getName());
                            map.put("prizeStartDate", DateTimeUtils.getDateString(startDate, DateTimeUtils.DEFAULT_DATE_FORMAT_PATTERN_SHORT));
                            map.put("prizeEndDate", DateTimeUtils.getDateString(endDate, DateTimeUtils.DEFAULT_DATE_FORMAT_PATTERN_SHORT));
                            map.put("prizeUrl", Constants.MY_LOTTERY_DETAIL_URL + priceInfo.getId().toString() + "/" + inMessage.getFromUserName());
                            inMessage.setMap(map);
                        } else {
                            map.put("prizeNotAvailable", aResp.getStatusMessage());
                            inMessage.setMap(map);
                        }
                    }

                }
                //正则表达式判断是否包含字母数字(6位)
//                String pattern = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6}$";
                String pattern = "[0-9A-Za-z]{4,6}$";//测试用

                //直接输唯一码
                if (inMessage.getMsgType() != null && inMessage.getMsgType().equals(WxConsts.CUSTOM_MSG_TEXT) && inMessage.getContent() != null && inMessage.getContent().matches(pattern)) {
                    Map<String, Object> msgMap = checkProgressByIdentityCode(inMessage.getContent());
                    if (msgMap == null) {
                        msgMap = Maps.newHashMap();
                    }
                    inMessage.setMap(msgMap);
                }
                WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
                String str = outMessage.toXml();
                out.print(str);
            } else if ("aes".equals(encryptType)) {
                String msgSignature = req.getParameter("msg_signature");
                WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(req.getInputStream(), wxMpConfigStorage, timestamp, nonce, msgSignature);
                WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
                out.write(outMessage.toEncryptedXml(wxMpConfigStorage));
            } else {
                LOGGER.error("不可识别的加密类型");
                out.write("不可识别的加密类型");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    @RequestMapping(value = "oauth2buildAuthorizationUrl")
    @ResponseBody
    public String oauth2buildAuthorizationUrl(String redirectURI) {
        return wxMpService.oauth2buildAuthorizationUrl(redirectURI, WxConsts.OAUTH2_SCOPE_BASE, null);
    }

    @RequestMapping(value = "oauth2getAccessToken")
    @ResponseBody
    public WxMpOAuth2AccessToken oauth2getAccessToken(String code) throws WxErrorException {
        return wxMpService.oauth2getAccessToken(code);
    }

    @RequestMapping(value = "jsapiSignature")
    @ResponseBody
    public WxJsapiSignature createJsapiSignature(String url) throws WxErrorException {
        return wxMpService.createJsapiSignature(url);
    }

    /**
     * 微信网页授权跳转页面url封装
     *
     * @param type
     * @param param1 prizeId
     * @param param2 openId
     * @return
     */
    private String buildAuthorizationUrl(int type, String param1, String param2) {
        String redirectURI = null;
        if (type == Integer.parseInt(QUERY_PROGRESS)) {
            //我的奖券
            redirectURI = Constants.MY_LOTTERY_LIST_URL;
        }
        if (type == Integer.parseInt(DRAW_LOTTERY)) {
            //图文消息点击打开
            redirectURI = Constants.MY_LOTTERY_DETAIL_URL + param1 + "/" + param2;
        }
        return wxMpService.oauth2buildAuthorizationUrl(redirectURI, WxConsts.OAUTH2_SCOPE_BASE, "123");
    }

    /*
     * 点击查询进展按钮，判断是否微信领号，或者是否扫过二维码，否则无法通过该方式查询进展
     * @param openId
     * @param state
     * @return
     **/
    private Map<String, Object> checkProgressByOpenIdState(String openId, Integer state) {
        //按openId和状态去数据库里查找最新的扫码绑定排位号
        WxWaitToken myWt = wxService.getByOpenId(openId, state);
        //如果找到了，说明是可以通过点击按钮方式直接查询
        Map<String, Object> msgMap = Maps.newHashMap();
        if (myWt != null) {
            msgMap = wxService.listProgressByOpenId(openId, myWt.getStore().getId(), myWt.getTableId());
        } else {
            msgMap.put("valid", "您尚未绑定二维码，请扫码绑定!");
        }
        return msgMap;
    }

    /**
     * 通过用户发送6位标识码去查询进展
     *
     * @param identityCode
     * @return
     */
    private Map<String, Object> checkProgressByIdentityCode(String identityCode) {
        Map<String, Object> progressInfo = wxService.listProgressByIdentityCode(identityCode);
        return progressInfo;
    }

    @RequestMapping(value = "create")
    @ResponseBody
    public ClientAjaxResult menuCreate() {
        try {
            WxMenu menu = new WxMenu();
            WxMenu.WxMenuButton button1 = new WxMenu.WxMenuButton();
            button1.setType(WxConsts.BUTTON_SCANCODE_PUSH);
            button1.setName("扫二维码");
            button1.setKey("QUERY_SCAN_QRCODE");

            WxMenu.WxMenuButton button2 = new WxMenu.WxMenuButton();
            button2.setType(WxConsts.BUTTON_CLICK);
            button2.setName("查询进展");
            button2.setKey("QUERY_LATEST_STATUS");

            WxMenu.WxMenuButton button3 = new WxMenu.WxMenuButton();
            button3.setType(WxConsts.BUTTON_VIEW);
            button3.setName("我的奖券");
            button3.setUrl(buildAuthorizationUrl(1, null, null));

            menu.getButtons().add(button1);
            menu.getButtons().add(button2);
            menu.getButtons().add(button3);

            wxMpService.menuCreate(menu);
        } catch (WxErrorException e) {
            LOGGER.error(e.toString());
        }
        return ClientAjaxResult.success("菜单创建成功！");
    }

    @RequestMapping(value = "generate_qrTmpPic", method = {RequestMethod.POST})
    @ResponseBody
    public WxMpQrCodeTicket generateQrCodePic(Long shopId) {
        WxMpQrCodeTicket ticket = null;
        try {
            Long startTime = System.currentTimeMillis();
            ticket = wxMpService.qrCodeCreateTmpTicket(shopId.intValue(), QRCODE_EXPIRE_SECONDS);
            Long endTime = System.currentTimeMillis();
            Long time = endTime - startTime;
            LOGGER.info("生成二维码接口时间消耗:" + time + "毫秒");
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return ticket;
    }
}