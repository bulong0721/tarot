package com.myee.tarot.wechat.handler;

import com.myee.tarot.cache.entity.MealsCache;
import com.myee.tarot.cache.uitl.RedissonUtil;
import com.myee.tarot.cache.view.WxWaitTokenView;
import com.myee.tarot.wechat.service.WechatService;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.common.util.StringUtils;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 用户关注公众号Handler
 * <p>
 * Created by FirenzesEagle on 2016/7/27 0027.
 * Email:liumingbo2008@gmail.com
 */
@Component
public class ProgressHandler implements WxMpMessageHandler {
    private static final Logger logger = LoggerFactory.getLogger(ProgressHandler.class);
    @Autowired
    protected WxMpConfigStorage configStorage;
    @Autowired
    protected WxMpService       wxMpService;
    @Autowired
    protected WechatService     coreService;
    @Autowired
    private RedissonClient redissonClient;


    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {
        WxWaitTokenView waitTokenView = null;
        MealsCache mealsCache = RedissonUtil.mealsCache(redissonClient);
        if(mealsCache!=null){
            Map<String,String> openIdInfo = mealsCache.getOpenIdInfo();
            if (openIdInfo!=null&& StringUtils.isNotBlank(openIdInfo.get(wxMessage.getFromUserName()))) {
                String key = openIdInfo.get(wxMessage.getFromUserName());
                String[] keys = key.split("&");
                List<WxWaitTokenView> wxWaitTokenViewList = mealsCache.getWxWaitTokenCache().get(keys[0]);
                for (WxWaitTokenView wxWaitTokenView : wxWaitTokenViewList) {
                    if(wxWaitTokenView.getToken().equals(keys[1])){
                       waitTokenView = wxWaitTokenView;
                        break;
                    }
                }
            }
        }
        WxMpXmlOutMessage m = WxMpXmlOutMessage.TEXT()
                .content(waitTokenView != null?String.format("您前面还有%d桌还在等待中，请耐心等待。。。。",waitTokenView.getWaitedCount()):"您现在暂无排队信息")
                .fromUser(wxMessage.getToUserName())
                .toUser(wxMessage.getFromUserName())
                .build();
        return m;
    }
};
