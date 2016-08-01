package com.myee.tarot.web.admin.controller.campaign;

import com.myee.tarot.campaign.domain.MerchantPrice;
import com.myee.tarot.campaign.domain.PriceInfo;
import com.myee.tarot.campaign.service.MerchantActivityService;
import com.myee.tarot.campaign.service.PriceInfoService;
import com.myee.tarot.campaign.service.impl.redis.RedisUtil;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.exception.ServiceException;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.merchant.domain.MerchantStore;
import me.chanjar.weixin.common.util.StringUtils;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Administrator on 2016/7/11.
 */
@Controller
public class PriceInfoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PriceInfo.class);

    @Autowired
    private PriceInfoService priceInfoService;
    @Autowired
    private MerchantActivityService merchantActivityService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private WxMpService wxMpService;

    /**
     * 保存一个用户的奖项记录
     * @param
     * @return
     */
    @RequestMapping(value = "api/info/savePriceInfo",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saveOrUpdatePriceInfo(@RequestParam("storeId")Long storeId,
                                              @RequestParam("keyId")String keyId){
        try {
           return priceInfoService.savePriceInfo(keyId,storeId);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);

    }

    /**
     * 根据keyId
     * @param code
     * @param status 0 已使用 1未使用 2过期
     * @return
     */
    @RequestMapping(value = "api/info/getInfoByStatusAndKeyId")
    @ResponseBody
    public AjaxResponse getPriceInfoByStatusAndKeyId(@RequestParam(value = "code",required = false)String code,
                                                     @RequestParam("status")int status,
                                                     @RequestParam(value = "keyId",required = false)String keyId){
        try {
            LOGGER.info("code为"+ code);
            AjaxResponse resp = new AjaxResponse();
            String openId = "";
            if(StringUtils.isNotBlank(keyId)){
                openId = keyId;
            }else{
                if(StringUtils.isNotBlank(code)){
                    WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxMpService.oauth2getAccessToken(code);
                    openId = wxMpOAuth2AccessToken.getOpenId();
                }else{
                    resp.setErrorString("请求参数code和keyId不能均为空");
                    resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
                    return resp;
                }
            }
            LOGGER.info("keyID为"+ openId);
            List<PriceInfo> infos = priceInfoService.findByStatusAndKeyId(openId, status);
            //每次请求未使用获取信息对后进行过期判定
            if(status == Constants.PRICEINFO_UNUSED){
                Iterator<PriceInfo> iter = infos.iterator();
                while(iter.hasNext()){
                    PriceInfo info = iter.next();
                    boolean result = compareTime(info.getPrice());
                    if(!result) {
                        info.setStatus(Constants.PRICEINFO_EXPIRE);
                        priceInfoService.update(info);
                        iter.remove();
                    }
                }
            }
            resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
            resp.addEntry("result", infos);
            resp.addEntry("keyId",openId);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);
    }


    /**
     * 获取本商户下当天已使用的奖券
     * @return
     */
    @RequestMapping(value = "api/info/findHistoryInfoByStoreToday",method = RequestMethod.GET )
    @ResponseBody
    public AjaxPageableResponse getHistoryInfoByStoreToday(HttpServletRequest request, PageRequest pageRequest){
        try {
            AjaxPageableResponse resp = new AjaxPageableResponse();
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp.setErrorString("当前无门店");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
            PageResult<PriceInfo> pageList = priceInfoService.pageList(merchantStore1.getId(), pageRequest);
            for (PriceInfo priceInfo : pageList.getList()) {
                resp.addDataEntry(objectToEntry(priceInfo));
            }
            resp.setRecordsTotal(pageList.getRecordsTotal());
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    //验证码验证
    @RequestMapping(value = "api/info/checkCode")
    @ResponseBody
    public AjaxResponse checkCodePrice(@RequestParam("checkCode")String checkCode,HttpServletRequest request){
        AjaxResponse resp = new AjaxResponse();
        try {
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp.setErrorString("当前无门店");
                resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
            PriceInfo priceInfo = priceInfoService.priceCheckCode(merchantStore1.getId(), checkCode);
            resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
            if(priceInfo!=null){
                //判断状态
                int status = priceInfo.getStatus();
                if(status==Constants.PRICEINFO_EXPIRE){
                    resp.setErrorString("该奖券已过期");
                }else if(status==Constants.PRICEINFO_USED){
                    resp.setErrorString("该奖券已被使用");
                }else{
                    //验证通过后
                    resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
                    resp.addDataEntry(objectToEntry(priceInfo));
                    priceInfo.setStatus(Constants.PRICEINFO_USED);
                    priceInfo.setCheckDate(new Date());
                    priceInfoService.update(priceInfo);
                }
            }else{
                resp.setErrorString("此验证码该店无效");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(PriceInfo priceInfo) {
        Map entry = new HashMap();
        entry.put("id", priceInfo.getId());
        entry.put("keyId", priceInfo.getKeyId());
        entry.put("checkCode", priceInfo.getCheckCode());
        entry.put("checkDate", priceInfo.getCheckDate());
        entry.put("status", priceInfo.getStatus());
        entry.put("price", priceInfo.getPrice());
        return entry;
    }

    private boolean compareTime(MerchantPrice price){
        Date startDate = price.getStartDate();
        Date endDate = price.getEndDate();
        Date now = new Date();
        int start = startDate.compareTo(now);
        int end = endDate.compareTo(now);
        if(start<=0 && end>=0){
            return true;
        }else{
            return false;
        }
    }


}
