package com.myee.tarot.wechat.handler;

import com.alibaba.druid.util.StringUtils;
import com.google.common.collect.Maps;
import com.myee.tarot.cache.entity.MealsCache;
import com.myee.tarot.cache.uitl.RedissonUtil;
import com.myee.tarot.cache.view.WxWaitTokenView;
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
import me.chanjar.weixin.mp.bean.WxMpXmlOutNewsMessage;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private RedissonClient redissonClient;



    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        WxMpXmlOutMessage m = null;
        String param = wxMessage.getEventKey();
        String[] splitParam = param.split("_");
        if(StringUtils.isNumber(splitParam[0])){
            int type = Integer.parseInt(splitParam[0]);  //获取业务类型  暂定1为抽奖，2为查看排队进展
            // 抽奖  例如：1_100
            if(type == Constants.WEIXIN_PRIZEDRAW){
                Long storeId = Long.parseLong(splitParam[1]);
                if(storeId != null){
                    Map<String,Object> aResp = priceInfoService.savePriceInfo(wxMessage.getFromUserName(), storeId);
                    int status = (int)aResp.get("status");
                    if(status == 0) {
                        PriceInfo priceInfo = (PriceInfo) aResp.get("result");
                        if (null != priceInfo) {
                            MerchantStore merchantStore = merchantStoreService.findById(storeId);
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
                }
                //排队进展 通过扫描  例如：2_100_A_A10
            }else if(type == Constants.WEIXIN_LINEPROCESS){
                String shopId = splitParam[1];
                String tableType = splitParam[2];
                String token = splitParam[3];
                String key = shopId + "_" + tableType;
                MealsCache mealCache = RedissonUtil.mealsCache(redissonClient);
                WxWaitTokenView waitTokenView = null;
                if(mealCache!=null){
                    List<WxWaitTokenView> wxWaitTokenViewList = mealCache.getWxWaitTokenCache().get(key);
                    for (WxWaitTokenView wxWaitTokenView : wxWaitTokenViewList) {
                        if(wxWaitTokenView.getToken().equals(token)){
                            waitTokenView = wxWaitTokenView;
                            //添加openId 至 redis
                            wxWaitTokenView.setOpenId(wxMessage.getFromUserName());
                            Map<String,String> openIdInfo = mealCache.getOpenIdInfo();
                            if(openIdInfo!=null){
                                openIdInfo.put(wxMessage.getFromUserName(),key +"&" + wxWaitTokenView.getToken());
                            }else {
                                Map<String,String> newOpenIdInfo = Maps.newHashMap();
                                newOpenIdInfo.put(wxMessage.getFromUserName(),key +"&" + wxWaitTokenView.getToken());
                                mealCache.setOpenIdInfo(newOpenIdInfo);
                            }
                            break;
                        }
                    }
                }
                m = WxMpXmlOutMessage.TEXT()
                        .content(String.format("您前面还有%d桌还在等待中，请耐心等待。。。。",waitTokenView.getWaitedCount()))
                        .fromUser(wxMessage.getToUserName())
                        .toUser(wxMessage.getFromUserName())
                        .build();

            }
        }

        return m;

    }

    /*private String parseStoreId(WxMpXmlMessage wxMessage) {
        String param = wxMessage.getEventKey();
        String[] splitParam = param.split("_");
        int type = Integer.parseInt(splitParam[0]);  //获取业务类型  暂定1为抽奖，2为查看排队进展
        if(StringUtils.isNumber(storeId)){
            logger.info("店面id为" + storeId);
            return Long.parseLong(storeId);
        }else {
            return null;
        }
    }*/
};
