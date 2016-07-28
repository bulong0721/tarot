package com.myee.tarot.web.admin.controller.campaign;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.myee.tarot.campaign.domain.MerchantActivity;
import com.myee.tarot.campaign.domain.MerchantPrice;
import com.myee.tarot.campaign.service.MerchantActivityService;
import com.myee.tarot.campaign.service.MerchantPriceService;
import com.myee.tarot.campaign.service.redis.RedisUtil;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.exception.ServiceException;
import com.myee.tarot.core.util.TimeUtil;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/7/11.
 */
@Controller
public class MerchantActivityController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MerchantActivity.class);

    @Autowired
    private MerchantActivityService merchantActivityService;

    @Autowired
    private MerchantPriceService merchantPriceService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 添加个新的奖券活动 传过来
     * @param
     * @return
     */
    @RequestMapping(value = "api/activity/saveOrUpdate",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saveActivity(@RequestParam("activityJson")String activityJson){
        try {
            //json转化为对象
            MerchantActivity merchantActivity = JSON.parseObject(activityJson, MerchantActivity.class);
            AjaxResponse resp = new AjaxResponse();
            if(merchantActivity.getStore()==null|| merchantActivity.getStore().getId()==null){
                resp.setErrorString("发起商户的ID不能为空");
                resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
                return resp;
            }
            boolean exist = false;
            MerchantActivity existActivity = merchantActivityService.findStoreActivity(merchantActivity.getStore().getId());
            if(existActivity!=null){
                exist = true;
            }
            List<MerchantPrice> prices = merchantActivity.getPrices();
            for (MerchantPrice price : prices) {
                price.setStoreId(existActivity.getStore().getId());
                if(exist){
                    price.setActivity(existActivity);
                }else{
                    price.setActivity(merchantActivity);
                }
            }
            MerchantActivity activity = new MerchantActivity();
            if(exist){
                //id存在
                Long priceId = merchantActivity.getPrices().get(0).getId();
                if(priceId!= null){
                    MerchantPrice price = merchantPriceService.findById(priceId);
                    if(price!=null){
                        //修改
                        price.setLevel(prices.get(0).getLevel());
                        price.setName(prices.get(0).getName());
                        price.setDescription(prices.get(0).getDescription());
                        price.setStartDate(prices.get(0).getStartDate());
                        price.setEndDate(prices.get(0).getEndDate());
                        price.setTotal(prices.get(0).getTotal());
                        List<MerchantPrice> showPrices = Lists.newArrayList();
                        MerchantPrice updatePrice = merchantPriceService.update(price);
                        showPrices.add(updatePrice);
                        merchantActivity.setPrices(showPrices);
                        activity = merchantActivity;
                    }else{
                        //去除id
                        prices.get(0).setId(null);
                        existActivity.setPrices(merchantActivity.getPrices());
                        activity = merchantActivityService.update(existActivity);
                    }
                }else{
                    //id不存在情况
                    existActivity.setPrices(merchantActivity.getPrices());
                    activity = merchantActivityService.update(existActivity);
                }
            }else{
                activity = merchantActivityService.update(merchantActivity);
            }
            resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
            resp.addEntry("result", activity);
            return resp;
        } catch (ServiceException e){
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);
    }

    /**
     * 删除个奖券活动  暂时可不用 暂存
     * @param activityId
     * @return
     */
    @RequestMapping(value = "api/activity/delete",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteActivity(@RequestParam("activityId")Long activityId){
        try {
            MerchantActivity activity = merchantActivityService.findById(activityId);
            if(activity!=null){
                activity.setDeleteStatus(Constants.DELETE_YES);
                for (MerchantPrice merchantPrice : activity.getPrices()) {
                    merchantPrice.setDeleteStatus(Constants.DELETE_YES);
                }
                merchantActivityService.update(activity);
                return AjaxResponse.success();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);
    }

    /**
     * 根据商户id寻找所有奖券
     * @param storeId
     * @return
     */
    @RequestMapping(value = "api/activity/findStoreActivity",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse findStoreActivity(@RequestParam("storeId")Long storeId){
        try {
            AjaxResponse response = new AjaxResponse();
            MerchantActivity result = merchantActivityService.findStoreActivity(storeId);
            response.addEntry("result", result);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);
    }

    /**
     * 修改商户激活的奖券
     * @param storeId
     * @param priceIds     * @return
     */
    @RequestMapping(value = "api/activity/openActivity",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse openActivity(@RequestParam("storeId")Long storeId,@RequestParam("priceIds")Long[] priceIds){
        try {
            AjaxResponse resp = new AjaxResponse();
            MerchantActivity activity = merchantActivityService.findStoreActivity(storeId);
            //先开放活动
            activity.setActivityStatus(Constants.ACITIVITY_START);
            merchantActivityService.update(activity);
            List<MerchantPrice> activePrice = Lists.newArrayList();
            //修改状态
            for (MerchantPrice merchantPrice : activity.getPrices()) {
                if(Arrays.asList(priceIds).contains(merchantPrice.getId())){
                    merchantPrice.setActiveStatus(Constants.PRICE_START);
                    activePrice.add(merchantPrice);
                }else{
                    merchantPrice.setActiveStatus(Constants.PRICE_END);
                }
                merchantPriceService.update(merchantPrice);
            }
            //生成抽奖的List
            List<Integer> drawList = getPriceCountList(activePrice);
            //放置于redis中
            redisUtil.set(Constants.PRICEDRAW + "_" + storeId, drawList, 365, TimeUnit.DAYS);
            resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);
    }

    /**
     * 根据活动ID 获取所有奖项设置  可以暂时不用
     * @param activityId
     * @return
     */
    @RequestMapping(value = "api/activity/findPriceByActivityId",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse findPriceByActivityId(@RequestParam("activityId")Long activityId){
        try {
            AjaxResponse response = new AjaxResponse();
            MerchantActivity activity = merchantActivityService.findById(activityId);
            response.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
            response.addEntry("activity", activity);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);
    }

    //重新分配奖券list
    public List<Integer> getPriceCountList(List<MerchantPrice> prices){;
        List<Integer> priceList = Lists.newArrayList();
        int totalAll = 0;
        for (MerchantPrice price : prices) {
            int total = price.getTotal();
            totalAll += total;
        }
        for (int i = 1; i <= totalAll; i++) {
            priceList.add(i);
        }
        return priceList;
    }


}
