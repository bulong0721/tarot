package com.myee.tarot.web.weixin.controller;

import com.myee.djinn.dto.WaitTokenState;
import com.myee.tarot.weixin.domain.ClientAjaxResult;
import com.myee.tarot.weixin.domain.WxWaitToken;
import com.myee.tarot.weixin.service.WeixinService;
import com.myee.tarot.weixin.service.impl.OperationsManager;
import com.myee.tarot.weixin.service.impl.RedisKeys;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.bean.WxMenu;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.util.StringUtils;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Martin on 2016/1/8.
 */
@Controller
@RequestMapping("weixin/wxmp")
public class WebMpController {
    private static final Logger logger = LoggerFactory.getLogger(WebMpController.class);
    private static final String wpSite = "http://www.myee7.com/biplus";
    @Autowired
    private   WxMpService       wxMpService;
    @Autowired
    private WeixinService wxService;
    @Autowired
    protected WxMpConfigStorage wxMpConfigStorage;
    @Autowired
    private   WxMpMessageRouter wxMpMessageRouter;
    @Autowired
    private OperationsManager manager;

    @RequestMapping(value = "service")
    @ResponseBody
    public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        logger.info("微信公众号请求信息");
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

        WxMpXmlMessage inMessage1 = WxMpXmlMessage.fromXml(req.getInputStream());
        System.out.println(inMessage1.toString());

//        try {
//            String shortUrl = wxMpService.shortUrl("https://mp.weixin.qq.com/misc/getqrcode?fakeid=3012749102%26token=367458522%26style=1");
//            System.out.println("shortUrl->" + shortUrl);
//        } catch (WxErrorException e) {
//            e.printStackTrace();
//        }
        try {
            if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
                out.print("非法请求");
            } else if (StringUtils.isNotBlank(echostr)) {
                out.write(echostr);
            }
            String encryptType = StringUtils.isBlank(req.getParameter("encrypt_type")) ? "raw" : req.getParameter("encrypt_type");
            if ("raw".equals(encryptType)) {
                System.out.println("req.getInputStream()->" + req.getInputStream());
                System.out.println("WxMpXmlMessage.fromXml(req.getInputStream())->" + WxMpXmlMessage.fromXml(req.getInputStream()));
                WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(req.getInputStream());
                String sceneId = inMessage.getEventKey();
                String redisKeyOfUcdScId = RedisKeys.getIdentityCode(Long.valueOf(sceneId));
                String identityCode = manager.getIdentityCode(redisKeyOfUcdScId);
                //微信自带的扫二维码，已关注
                if(inMessage.getMsgType() != null && inMessage.getMsgType().equals(WxConsts.XML_MSG_EVENT) && inMessage.getEvent() != null && inMessage.getEvent().equals(WxConsts.EVT_SCAN)) {
                    //将该二维码绑定扫码的微信OpenId
                    Map<String,Object> msgMap = new HashMap<String,Object>();
                    if(wxService.bondQrCodeByScan(identityCode, inMessage.getFromUserName())) {
                        System.out.println("coding-here->2");
                        msgMap = checkLatestDevelopments(identityCode);
                        int i = wxService.modifyWaitingInfo(Long.parseLong(msgMap.get("waitedTableCount").toString()),identityCode,Long.valueOf(msgMap.get("timeTook").toString()), Long.parseLong(msgMap.get("predictWaitingTime").toString()));
                        if(i != 1) {
                            logger.error("修改排号信息失败!");
                        }
                    } else {
                        System.out.println("coding-here->3");
                        msgMap.put("valid", "该唯一码已过期或无效，查询进度失败!");
                    }
                    System.out.println("coding-here->4");
                    inMessage.setMap(msgMap);
                    System.out.println("coding-here->5");
                }
                //点击事件
                if(inMessage.getMsgType() != null && inMessage.getMsgType().equals(WxConsts.XML_MSG_EVENT) && inMessage.getEvent()!= null && inMessage.getEvent().equals(WxConsts.EVT_CLICK)) {
                    Map<String,Object> msgMap = checkLatestDevelopments(inMessage.getFromUserName(), WaitTokenState.WAITING.getValue());
                    inMessage.setMap(msgMap);
                }
                //微信自带的扫二维码，未关注
                if(inMessage.getMsgType() != null && inMessage.getMsgType().equals(WxConsts.XML_MSG_EVENT) && inMessage.getEvent() != null && inMessage.getEvent().equals(WxConsts.EVT_SUBSCRIBE) && inMessage.getTicket() != null) {
                    //将该二维码绑定扫码的微信OpenId
                    Map<String,Object> msgMap = new HashMap<String,Object>();
                    if(wxService.bondQrCodeByScan(identityCode, inMessage.getFromUserName())) {
                        msgMap = checkLatestDevelopments(identityCode);
                        int i = wxService.modifyWaitingInfo(Long.parseLong(msgMap.get("waitedTableCount").toString()),identityCode,Long.valueOf(msgMap.get("timeTook").toString()), Long.parseLong(msgMap.get("predictWaitingTime").toString()));
                        if(i != 1) {
                            logger.error("修改排号信息失败!");
                        }
                    } else {
                        msgMap.put("vaild","该唯一码已过期或无效，查询进度失败!");
                    }
                    inMessage.setMap(msgMap);
                }
            //正则表达式判断是否包含字母数字(6位)
            String pattern = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6}$";

            //直接输唯一码
            if(inMessage.getMsgType() != null && inMessage.getMsgType().equals("text") && inMessage.getContent() != null && inMessage.getContent().matches(pattern)) {
                Map<String,Object> msgMap = checkLatestDevelopments(inMessage.getContent());
                inMessage.setMap(msgMap);
            }
                System.out.println("coding-here->6");
                WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
                System.out.println("coding-here->7");
                String str = outMessage.toXml();
                System.out.println("coding-here->8");
                out.print(str);
            } else if ("aes".equals(encryptType)) {
                String msgSignature = req.getParameter("msg_signature");
                WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(req.getInputStream(), wxMpConfigStorage, timestamp, nonce, msgSignature);
                WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
                out.write(outMessage.toEncryptedXml(wxMpConfigStorage));
            } else {
                logger.error("不可识别的加密类型");
                out.write("不可识别的加密类型");
            }
            System.out.println("coding-here->9");
        } catch (Exception e) {
            System.out.println("coding-here->10");
            System.out.println(e.getMessage());
        } finally {
            System.out.println("coding-here->11");
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

    @RequestMapping(value = "fromWxAuth")
    public String fromWxAuth(String code, String page, String args) {
        if ("account".equalsIgnoreCase(page)) {
            return "redirect:/weixin.html#/account?code=" + code;
        } else if ("contact".equalsIgnoreCase(page)) {
            return "redirect:/weixin.html#/contact?code=" + code;
        }
        return "redirect:/weixin.html#/shop_list?code=" + code;
    }

    private String buildAuthorizationUrl(String page, String args) {
        String redirectURI = String.format("%s/wxmp/fromWxAuth?page=%s&args=%s", wpSite, page, args);
        return wxMpService.oauth2buildAuthorizationUrl(redirectURI, WxConsts.OAUTH2_SCOPE_BASE, null);
    }

    /*
     * 点击查询进展按钮，判断是否微信领号，或者是否扫过二维码，否则无法通过该方式查询进展
     * @param openId
     * @param state
     * @return
     **/

    private Map<String,Object> checkLatestDevelopments(String openId, Integer state) {
        //按openId和状态去数据库里查找
        List<WxWaitToken> myWt = wxService.selectTokensByOpenIdState(openId,state);
        //如果找到了，说明是可以通过点击按钮方式直接查询
        Map<String,Object> msgMap = new HashMap<String,Object>();
        if (myWt != null && myWt.size() > 0) {
            for (WxWaitToken w : myWt) {
                msgMap = wxService.selectTokensByInfo(openId, w.getMerchantStoreId() ,w.getTableId());
            }
        } else {
            msgMap.put("valid","您尚未绑定二维码，请扫码绑定!");
        }
        return msgMap;
    }

    /**
     * 通过用户发送6位标识码去查询进展
     * @param identityCode
     * @return
     * */

    private Map<String,Object> checkLatestDevelopments(String identityCode) {
        Map<String,Object> latestDevInfo = wxService.selectLatestDevelopmentsByIc(identityCode);
        return latestDevInfo;
    }

    @RequestMapping(value = "create")
    @ResponseBody
    public ClientAjaxResult menuCreate() {
        try {
            WxMenu menu = new WxMenu();
            WxMenu.WxMenuButton button1 = new WxMenu.WxMenuButton();
            button1.setType(WxConsts.BUTTON_SCANCODE_WAITMSG);
            button1.setName("扫码查询");
            button1.setKey("QUERY_SCAN_LOTTERY");

            WxMenu.WxMenuButton button2 = new WxMenu.WxMenuButton();
            button2.setType(WxConsts.BUTTON_CLICK);
            button2.setName("菜单2");
            button2.setKey("V1001_QUERY_CODE");

            WxMenu.WxMenuButton button3 = new WxMenu.WxMenuButton();
            button3.setType(WxConsts.BUTTON_VIEW);
            button3.setName("我的奖券");
            button3.setUrl("http://www.baidu.com");

            menu.getButtons().add(button1);
            menu.getButtons().add(button2);
            menu.getButtons().add(button3);

            wxMpService.menuCreate(menu);

        } catch (WxErrorException e) {
            logger.error(e.toString());
        }
        return ClientAjaxResult.success("菜单创建成功！");
    }

    @RequestMapping(value = "generate_qrPic", method = {RequestMethod.POST})
    @ResponseBody
    public WxMpQrCodeTicket generateQrCodePic(Long shopId) {
        WxMpQrCodeTicket ticket = null;
        try {
            Long startTime = System.currentTimeMillis();
            ticket = wxMpService.qrCodeCreateTmpTicket(shopId.intValue(),null);
            Long endTime = System.currentTimeMillis();
            Long time = endTime - startTime;
            System.out.println("生成二维码接口时间消耗:" + time + "毫秒");
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return ticket;
    }

    public void setWxMpMessageRouter(WxMpMessageRouter wxMpMessageRouter) {
        this.wxMpMessageRouter = wxMpMessageRouter;
    }
}
