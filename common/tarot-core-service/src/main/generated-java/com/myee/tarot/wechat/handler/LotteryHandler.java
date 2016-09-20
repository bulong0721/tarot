package com.myee.tarot.wechat.handler;

import com.alibaba.druid.util.StringUtils;
import com.myee.tarot.campaign.domain.PriceInfo;
import com.myee.tarot.campaign.service.PriceInfoService;
import com.myee.tarot.core.Constants;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

import static me.chanjar.weixin.mp.bean.WxMpXmlOutNewsMessage.Item;

@Component
public class LotteryHandler implements WxMpMessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(LotteryHandler.class);
    @Autowired
    protected WxMpConfigStorage    configStorage;
    @Autowired
    private   PriceInfoService     priceInfoService;
    @Autowired
    private   MerchantStoreService merchantStoreService;
    @Autowired
    protected WechatService coreService;



    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        WxMpXmlOutMessage m = null;
        Long storeId = parseStoreId(wxMessage);
        if(storeId != null){
            Map<String,Object> aResp = priceInfoService.savePriceInfo(wxMessage.getFromUserName(), storeId);
            int status = (int)aResp.get("status");
            if(status == 0) {
                PriceInfo priceInfo = (PriceInfo) aResp.get("result");
                if (null != priceInfo) {
                    MerchantStore merchantStore = merchantStoreService.findById(storeId);
                    Date startDate = priceInfo.getPrice().getStartDate();
                    Date endDate = priceInfo.getPrice().getEndDate();
                    Item item = new Item();
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
        }

        return m;

    }

    private Long parseStoreId(WxMpXmlMessage wxMessage) {
        String storeId = wxMessage.getEventKey();
        if(StringUtils.isNumber(storeId)){
            logger.info("店面id为" + storeId);
            return Long.parseLong(storeId);
        }else {
            return null;
        }
    }
};
