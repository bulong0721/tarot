package com.myee.tarot.web.wechat.controller;

import com.google.gson.Gson;
import com.myee.tarot.core.Constants;
import com.myee.tarot.web.ClientAjaxResult;
import com.myee.tarot.wechat.service.WechatService;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.util.StringUtils;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpQrCodeTicket;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Martin on 2016/9/7.
 */
@Controller
@RequestMapping("weixin/wxmp")
public class WechatController {
    protected static final Logger logger = LoggerFactory.getLogger(WechatController.class);
    @Autowired
    protected WxMpConfigStorage configStorage;
    @Autowired
    protected WxMpService       wxMpService;
    @Autowired
    protected WechatService     coreService;


    @RequestMapping(value = "service")
    public void wechatCore(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String signature = request.getParameter("signature");
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");

        if (!this.wxMpService.checkSignature(timestamp, nonce, signature)) {
            // 消息签名不正确，说明不是公众平台发过来的消息
            response.getWriter().println("非法请求");
            return;
        }

        String echostr = request.getParameter("echostr");
        if (StringUtils.isNotBlank(echostr)) {
            // 说明是一个仅仅用来验证的请求，回显echostr
            response.getWriter().println(echostr);
            return;
        }

        String encryptType = StringUtils.isBlank(request.getParameter("encrypt_type")) ?
                "raw" :
                request.getParameter("encrypt_type");

        if ("raw".equals(encryptType)) {
            // 明文传输的消息
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(request.getInputStream());
            WxMpXmlOutMessage outMessage = this.coreService.route(inMessage);
            if (null != outMessage) {
                response.getWriter().write(outMessage.toXml());
            }
            return;
        }

        if ("aes".equals(encryptType)) {
            // 是aes加密的消息
            String msgSignature = request.getParameter("msg_signature");
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(
                    request.getInputStream(), this.configStorage, timestamp, nonce,
                    msgSignature);
            logger.debug("\n消息解密后内容为：\n{} ", inMessage.toString());
            WxMpXmlOutMessage outMessage = this.coreService.route(inMessage);
            logger.info(response.toString());
            response.getWriter()
                    .write(outMessage.toEncryptedXml(this.configStorage));
            return;
        }

        response.getWriter().println("不可识别的加密类型");
    }

    private static final int QRCODE_EXPIRE_SECONDS = 2592000;

    @RequestMapping(value = "generate_qrTmpPic", method = {RequestMethod.POST})
    @ResponseBody
    public WxMpQrCodeTicket generateQrCodePic(@RequestParam("shopId")Long shopId) {
        WxMpQrCodeTicket ticket = null;
        try {
            Long startTime = System.currentTimeMillis();
            ticket = wxMpService.getQrcodeService().qrCodeCreateTmpTicket(shopId.intValue(), QRCODE_EXPIRE_SECONDS);
            Long endTime = System.currentTimeMillis();
            Long time = endTime - startTime;
            logger.info("生成二维码接口时间消耗:{}毫秒", time);
        } catch (WxErrorException e) {
            logger.error(e.getMessage(),e);
        }
        return ticket;
    }

    @RequestMapping(value = "generate_qrTmpPic2", method = {RequestMethod.POST})
    @ResponseBody
    public WxMpQrCodeTicket generateQrCodePic(@RequestParam("param")String param) {
        WxMpQrCodeTicket ticket = null;
        try {
            Long startTime = System.currentTimeMillis();
            ticket = wxMpService.getQrcodeService().qrCodeCreateLastTicket(param);
            Long endTime = System.currentTimeMillis();
            Long time = endTime - startTime;
            logger.info("生成二维码接口时间消耗:{}毫秒", time);
        } catch (WxErrorException e) {
            logger.error(e.getMessage(),e);
        }
        return ticket;
    }

    @RequestMapping(value = "createMenu")
    @ResponseBody
    public ClientAjaxResult createMenu() {
        try {
            WxMenu menu = new WxMenu();
            WxMenuButton button1 = new WxMenuButton();
            button1.setType(WxConsts.BUTTON_SCANCODE_PUSH);
            button1.setName("扫二维码");
            button1.setKey("QUERY_SCAN_QRCODE");

            WxMenuButton button2 = new WxMenuButton();
            button2.setType(WxConsts.BUTTON_CLICK);
            button2.setName("查询进展");
            button2.setKey("QUERY_LATEST_STATUS");

            WxMenuButton button3 = new WxMenuButton();
            button3.setType(WxConsts.BUTTON_VIEW);
            button3.setName("我的奖券");
            button3.setUrl(buildAuthorizationUrl(1, null, null));

            menu.getButtons().add(button1);
            menu.getButtons().add(button2);
            menu.getButtons().add(button3);

            wxMpService.getMenuService().menuCreate(menu);
        } catch (WxErrorException e) {

        }
        return ClientAjaxResult.success("菜单创建成功！");
    }

    private String buildAuthorizationUrl(int type, String param1, String param2) {
        String redirectURI = Constants.MY_LOTTERY_LIST_URL;
        return wxMpService.oauth2buildAuthorizationUrl(redirectURI, WxConsts.OAUTH2_SCOPE_BASE, "123");
    }

    /**
     * 通过openid获得基本用户信息
     * 详情请见: http://mp.weixin.qq.com/wiki/14/bb5031008f1494a59c6f71fa0f319c66.html
     *
     * @param response
     * @param openid   openid
     * @param lang     zh_CN, zh_TW, en
     */
    @RequestMapping(value = "getUserInfo")
    public WxMpUser getUserInfo(HttpServletResponse response, @RequestParam(value = "openid") String openid, @RequestParam(value = "lang") String lang) {
        WXAjaxResult returnModel = new WXAjaxResult();
        WxMpUser wxMpUser = null;
        try {
            wxMpUser = this.wxMpService.getUserService().userInfo(openid, lang);
            returnModel.setResult(true);
            returnModel.setDatum(wxMpUser);
            renderString(response, returnModel);
        } catch (WxErrorException e) {
            returnModel.setResult(false);
            returnModel.setReason(e.getError().toString());
            renderString(response, returnModel);
            logger.error(e.getError().toString());
        }
        return wxMpUser;
    }

    /**
     * 通过code获得基本用户信息
     * 详情请见: http://mp.weixin.qq.com/wiki/14/bb5031008f1494a59c6f71fa0f319c66.html
     *
     * @param response
     * @param code     code
     * @param lang     zh_CN, zh_TW, en
     */
    @RequestMapping(value = "getOAuth2UserInfo")
    public void getOAuth2UserInfo(HttpServletResponse response, @RequestParam(value = "code") String code, @RequestParam(value = "lang") String lang) {
        WXAjaxResult returnModel = new WXAjaxResult();
        WxMpOAuth2AccessToken accessToken;
        WxMpUser wxMpUser;
        try {
            accessToken = this.wxMpService.oauth2getAccessToken(code);
            wxMpUser = this.wxMpService.getUserService()
                    .userInfo(accessToken.getOpenId(), lang);
            returnModel.setResult(true);
            returnModel.setDatum(wxMpUser);
            renderString(response, returnModel);
        } catch (WxErrorException e) {
            returnModel.setResult(false);
            returnModel.setReason(e.getError().toString());
            renderString(response, returnModel);
            logger.error(e.getError().toString());
        }
    }

    /**
     * 用code换取oauth2的openid
     * 详情请见: http://mp.weixin.qq.com/wiki/1/8a5ce6257f1d3b2afb20f83e72b72ce9.html
     *
     * @param response
     * @param code     code
     */
    @RequestMapping(value = "getOpenid")
    public void getOpenid(HttpServletResponse response, @RequestParam(value = "code") String code) {
        WXAjaxResult returnModel = new WXAjaxResult();
        WxMpOAuth2AccessToken accessToken;
        try {
            accessToken = this.wxMpService.oauth2getAccessToken(code);
            returnModel.setResult(true);
            returnModel.setDatum(accessToken.getOpenId());
            renderString(response, returnModel);
        } catch (WxErrorException e) {
            returnModel.setResult(false);
            returnModel.setReason(e.getError().toString());
            renderString(response, returnModel);
            logger.error(e.getError().toString());
        }
    }

    protected String renderString(HttpServletResponse response, Object object) {
        return renderString(response, new Gson().toJson(object), "application/json");
    }

    protected String renderString(HttpServletResponse response, String string, String type) {
        try {
            response.reset();
            response.setContentType(type);
            response.setCharacterEncoding("utf-8");
            //解决跨域问题
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.getWriter().print(string);
            return null;
        } catch (IOException e) {
            return null;
        }
    }
}
