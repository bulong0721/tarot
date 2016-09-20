package com.myee.tarot.wechat.handler;

import com.myee.tarot.campaign.domain.PriceInfo;
import com.myee.tarot.campaign.service.PriceInfoService;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.StringUtil;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.merchant.service.MerchantStoreService;
import com.myee.tarot.wechat.service.WechatService;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutNewsMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户关注公众号Handler
 * <p>
 * Created by FirenzesEagle on 2016/7/27 0027.
 * Email:liumingbo2008@gmail.com
 */
@Component
public class SubscribeHandler implements WxMpMessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(SubscribeHandler.class);
    @Autowired
    protected WxMpConfigStorage configStorage;
    @Autowired
    protected WxMpService       wxMpService;
    @Autowired
    protected WechatService     coreService;
    @Autowired
    private PriceInfoService priceInfoService;
    @Autowired
    private MerchantStoreService merchantStoreService;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        WxMpUser wxMpUser = coreService.getUserInfo(wxMessage.getFromUserName(), "zh_CN");
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("openId", wxMpUser.getOpenId()));
        params.add(new BasicNameValuePair("nickname", wxMpUser.getNickname()));
        params.add(new BasicNameValuePair("headImgUrl", wxMpUser.getHeadImgUrl()));

        //TODO 在这里可以进行用户关注时对业务系统的相关操作（比如新增用户）
        WxMpXmlOutMessage m = null;
        String eventKey = wxMessage.getEventKey(); //切割字符串 获取shopId
        String storeId = "";
        if(!StringUtil.isBlank(eventKey)){
            storeId = eventKey.split("_")[1];
        }
        //若为数字 即中奖信息
        if(StringUtil.isNumeric(storeId)){
            long shopId = Long.parseLong(storeId);
            Map<String,Object> aResp = priceInfoService.savePriceInfo(wxMessage.getFromUserName(), shopId);
            int status = (int)aResp.get("status");
            if(status == 0) {
                PriceInfo priceInfo = (PriceInfo) aResp.get("result");
                if (null != priceInfo) {
                    MerchantStore merchantStore = merchantStoreService.findById(shopId);
                    Date startDate = priceInfo.getPrice().getStartDate();
                    Date endDate = priceInfo.getPrice().getEndDate();
                    WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
                    item.setTitle(String.format("%s奖券一张", merchantStore.getName()));
                    item.setDescription(String.format("有效期:%tF ~ %tF", startDate, endDate));
                    //item.setPicUrl("http://pic4.zhongsou.com/img?id=52258c09287e360e89e"); //测试图片
                    item.setUrl(String.format("%s%d/%s", Constants.MY_LOTTERY_DETAIL_URL, priceInfo.getId(), wxMessage.getFromUserName()));
                    m = WxMpXmlOutMessage.NEWS()
                            .fromUser(wxMessage.getToUserName())
                            .toUser(wxMessage.getFromUserName())
                            .addArticle(item)
                            .build();
                }
            } else {
                m = WxMpXmlOutMessage.TEXT()
                        .content(aResp.get("errorString").toString())
                        .fromUser(wxMessage.getToUserName())
                        .toUser(wxMessage.getFromUserName())
                        .build();
            }
        } else {
            m = WxMpXmlOutMessage.TEXT()
                    .content("尊敬的" + wxMpUser.getNickname() + "，您好！")
                    .fromUser(wxMessage.getToUserName())
                    .toUser(wxMessage.getFromUserName())
                    .build();
        }
        return m;


    }
};
