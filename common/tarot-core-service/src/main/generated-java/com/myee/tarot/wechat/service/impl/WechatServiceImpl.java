package com.myee.tarot.wechat.service.impl;

import com.myee.tarot.wechat.handler.LotteryHandler;
import com.myee.tarot.wechat.handler.ProgressHandler;
import com.myee.tarot.wechat.handler.SubscribeHandler;
import com.myee.tarot.wechat.service.WechatService;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

/**
 * Created by Martin on 2016/9/7.
 */
@Service
public class WechatServiceImpl implements WechatService {
    @Autowired
    private WxMpService      wxMpService;
    @Autowired
    private SubscribeHandler subscribeHandler;
    @Autowired
    private LotteryHandler   lotteryHandler;
    @Autowired
    private ProgressHandler  progressHandler;

    private WxMpMessageRouter router;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @PostConstruct
    public void init() {
        this.refreshRouter();
    }

    @Override
    public void requestGet(String urlWithParams) throws IOException {
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        HttpGet httpget = new HttpGet(urlWithParams);
        httpget.addHeader("Content-Type", "text/html;charset=UTF-8");
        //配置请求的超时设置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(50)
                .setConnectTimeout(50)
                .setSocketTimeout(50).build();
        httpget.setConfig(requestConfig);

        CloseableHttpResponse response = httpclient.execute(httpget);
        System.out.println("StatusCode -> " + response.getStatusLine().getStatusCode());

        HttpEntity entity = response.getEntity();
        String jsonStr = EntityUtils.toString(entity);//, "utf-8");
        System.out.println(jsonStr);

        httpget.releaseConnection();
    }

    @Override
    public void requestPost(String url, List<NameValuePair> params) throws ClientProtocolException, IOException {
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();

        HttpPost httppost = new HttpPost(url);
        httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

        CloseableHttpResponse response = httpclient.execute(httppost);
        System.out.println(response.toString());

        HttpEntity entity = response.getEntity();
        String jsonStr = EntityUtils.toString(entity, "utf-8");
        System.out.println(jsonStr);

        httppost.releaseConnection();
    }

    @Override
    public void refreshRouter() {
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(this.wxMpService);

        // 关注事件
        newRouter.rule().async(false)
                .msgType(WxConsts.XML_MSG_EVENT)
                .event(WxConsts.EVT_SUBSCRIBE)
                .handler(this.subscribeHandler)
                .end();

        //点击菜单按钮查询排队进展
      /* newRouter.rule().async(false)
                .msgType(WxConsts.XML_MSG_EVENT)
                .event(WxConsts.EVT_SCANCODE_PUSH).eventKey("V1001_QUERY_INPUT")
                .handler(this.progressHandler)
                .end();*/

        //扫码查询排队进展
        newRouter.rule().async(false)
                .msgType(WxConsts.XML_MSG_EVENT)
                .event(WxConsts.EVT_CLICK).eventKey("QUERY_LATEST_STATUS")
                .handler(this.progressHandler)
                .end();

        //微信扫描 需存在
        newRouter.rule().async(false)
                .msgType(WxConsts.XML_MSG_EVENT)
                .event(WxConsts.EVT_SCAN)
                .handler(this.lotteryHandler)
                .end();

        //扫码抽奖
        newRouter.rule().async(false)
                .msgType(WxConsts.XML_MSG_EVENT)
                .event(WxConsts.EVT_SCANCODE_PUSH).eventKey("QUERY_SCAN_QRCODE")
                .handler(this.lotteryHandler)
                .end();

        this.router = newRouter;
    }

    @Override
    public WxMpXmlOutMessage route(WxMpXmlMessage inMessage) {
        try {
            return this.router.route(inMessage);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public WxMpUser getUserInfo(String openid, String lang) {
        WxMpUser wxMpUser = null;
        try {
            wxMpUser = this.wxMpService.getUserService().userInfo(openid, lang);
        } catch (WxErrorException e) {
            logger.error(e.getError().toString());
        }
        return wxMpUser;
    }
}
