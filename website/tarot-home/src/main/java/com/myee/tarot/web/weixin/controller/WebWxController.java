package com.myee.tarot.web.weixin.controller;

import com.myee.djinn.dto.*;
import com.myee.tarot.web.util.PageResult;
import com.myee.tarot.weixin.domain.RWaitToken;
import com.myee.tarot.weixin.domain.SingleResult;
//import com.myee.tarot.weixin.service.WeixinService;
import com.myee.tarot.weixin.service.WeixinService;
import com.myee.tarot.weixin.util.TimeUtil;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by Martin on 2016/1/8.
 */
@Controller
@RequestMapping("weixin/wxitf")
public class WebWxController {
    private Logger logger = LoggerFactory.getLogger(WebWxController.class);
    private static final String wpSite = "http://www.myee7.com/biplus";
    @Autowired
    private WeixinService weixinService;

    @Autowired
    private WxMpService wxMpService;

    @RequestMapping("shop_list")
    @ResponseBody
    public PageResult<ShopSummary> shopList(String secret, Long orgId, String ifPaged, Integer pageNo, Integer pageSize) {
        Long clientId = getClientId(secret);
        Collection<ShopDetail> storeDetails = weixinService.allStoreOfClient(clientId, orgId);
        List<ShopDetail> list = new ArrayList<ShopDetail>();
        Iterator<ShopDetail> it = storeDetails.iterator();
        while (it.hasNext()) {
            list.add(it.next());
        }
        if("1".equals(ifPaged)) {
            try {
                list = page(pageNo,pageSize,list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new PageResult(1L, list);
    }

    private Long getClientId(String secret) {
        return 104L;
    }

    @RequestMapping("shop_detail")
    @ResponseBody
    public SingleResult<ShopDetail> shopDetail(String secret, long storeId) {
        Long clientId = getClientId(secret);
        ShopDetail storeDetail = weixinService.storeOfClient(clientId, storeId);
        return new SingleResult(storeDetail);
    }

    @RequestMapping("enqueue")
    @ResponseBody
    public SingleResult<RWaitToken> enqueueAccess(String secret, long shopId, int dinerCount, String openId, HttpServletRequest req, HttpServletResponse resp) {
        openId = (String)req.getSession().getAttribute("openId");
        RWaitToken waitToken = weixinService.enqueue(shopId, dinerCount, openId);
        return new SingleResult(waitToken);
    }

    private String buildAuthorizationUrl(String page, String args) {
        String redirectURI = String.format("%s/wxmp/fromWxAuth?page=%s&args=%s", wpSite, page, args);
        return wxMpService.oauth2buildAuthorizationUrl(redirectURI, WxConsts.OAUTH2_SCOPE_BASE, null);
    }

    @RequestMapping("dequeue")
    @ResponseBody
    public void dequeue(String secret, long tableTypeId, String token) {
        weixinService.dequeue(tableTypeId, token, 111L,111L);
    }

    /**
     * 微信点餐查看进展
     * @param secret
     * @param tableTypeId
     * @param token
     * @return
     */
    @RequestMapping("view_progress")
    @ResponseBody
    public SingleResult<WaitToken> viewProgress(String secret, long tableTypeId, String token) {
        WaitToken waitToken = weixinService.waitOfTableType(tableTypeId, token, 111L);
        return new SingleResult(waitToken);
    }

    @RequestMapping("wait_list")
    @ResponseBody
    public PageResult<RWaitToken> waitList(String secret, String openId, String ifPaged, Integer pageNo, Integer pageSize) {
        Map<String,Date> map = TimeUtil.getAfterDate(new Date());
        List<RWaitToken> list = weixinService.selectWaitList(openId, map);
        return new PageResult(1L, list);
    }

    @RequestMapping("draw")
    @ResponseBody
    public SingleResult<DrawResult> draw(String secret, long storeId, String token) {
        /*DrawToken drawToken = weixinService.drawOfStore(storeId, token);
        return new SingleResult(drawToken);*/
        return null;
    }

    @RequestMapping("draw_list")
    @ResponseBody
    public PageResult<DrawToken> drawList(String secret) {
        return null;
    }

    @ExceptionHandler({Exception.class})
    public void databaseError(Exception ex, HttpServletResponse response) {
        try {
            response.addHeader("Error", ex.getMessage());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送意见
     */
    @RequestMapping("send_feedback")
    @ResponseBody
    public void sendFeedback(String context, String openId) {
        weixinService.sendFeedback(context,openId);
    }

    /**
     *
     * @param pageNo 当前页码
     * @param pageSize 页数
     * @param list  所有集合
     * @return
     * @throws Exception
     */
    private static List<ShopDetail> page(int pageNo,int pageSize,List<ShopDetail> list) throws Exception{
        List<ShopDetail> result = new ArrayList<ShopDetail>();
        if(list != null && list.size() > 0){
            int allCount = list.size();
            int pageCount = (allCount + pageSize-1) / pageSize;
            if(pageNo >= pageCount){
                pageNo = pageCount;
            }
            int start = (pageNo-1) * pageSize;
            int end = pageNo * pageSize;
            if(end >= allCount){
                end = allCount;
            }
            for(int i = start; i < end; i ++){
                result.add(list.get(i));
            }
        }
        return (result != null && result.size() > 0) ? result : null;
    }

    /**
     * 取消排队
     */
    @RequestMapping("cancel_Queue")
    @ResponseBody
    public void cancelQueue(long waitQueueId) {
        //删除Redis中的相应key下的对象
        try {
            String redisKey = weixinService.hGetWaitTokenRedisKey(waitQueueId);
            if(redisKey != null) {
                weixinService.hDelWaitTokenRedisRecord(waitQueueId);
                //修改mysql的记录的状态
                weixinService.modifyWaitingStatus(WaitTokenState.CANCEL.getValue(), waitQueueId);
            } else {
                //修改mysql的记录的状态
                weixinService.modifyWaitingStatus(WaitTokenState.CANCEL.getValue(), waitQueueId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 计算距离
     */
    @RequestMapping("calc_distance")
    @ResponseBody
    public void calcDistance() {
//        Double a1 = 31.1947540000;
//        Double a2 = 121.3075550000;
//        Double b1 = 31.2180757249;
//        Double b2 = 121.3592081556;
//        Double d = weixinService.calcDistance(a1,a2,b1,b2);
//        System.out.println("Distance: "+d);
    }
}
