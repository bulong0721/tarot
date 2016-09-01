package com.myee.tarot.web.campaign.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.myee.tarot.campaign.domain.MerchantActivity;
import com.myee.tarot.campaign.domain.MerchantPrice;
import com.myee.tarot.campaign.domain.ModeSwitch;
import com.myee.tarot.campaign.service.MerchantActivityService;
import com.myee.tarot.campaign.service.MerchantPriceService;
import com.myee.tarot.campaign.service.ModeSwitchService;
import com.myee.tarot.campaign.service.impl.redis.DateTimeUtils;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.merchant.service.MerchantStoreService;
import com.myee.tarot.uitl.CacheUtil;
import org.apache.ignite.Ignite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.cache.Cache;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/7/11.
 */
@Controller
public class MerchantActivityController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MerchantActivityController.class);

    @Autowired
    private MerchantActivityService merchantActivityService;
    @Autowired
    private MerchantPriceService merchantPriceService;
    @Autowired
    private ModeSwitchService modeSwitchService;
    @Autowired
    private MerchantStoreService merchantStoreService;

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
            //先判断商户的ID是否存在
            if(merchantActivity.getStore()==null|| merchantActivity.getStore().getId()==null){
                resp.setErrorString("发起商户的ID不能为空");
                resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
                return resp;
            }
            //再判断 奖券使用有效期  前台必定只会传一个price
            MerchantPrice checkPrice = merchantActivity.getPrices().get(0);
            Date startDate = checkPrice.getStartDate();
            Date endDate = checkPrice.getEndDate();
            Date startToday = DateTimeUtils.startToday();
            if(startDate.compareTo(startToday) < 0){   //开始时间小于当天开始时间
                resp.setErrorString("有效期开始日期不得小于当天日期");
                resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
                return resp;
            }else {
                if(endDate.compareTo(startDate) < 0){  //有效期结束时间不能小于开始时间
                    resp.setErrorString("有效期结束时间不能小于开始时间");
                    resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
                    return resp;
                }
            }
            //前台再次判断 奖券名字是否重复
            String checkPriceName = checkPrice.getName();
            boolean isOnlyPriceName = merchantPriceService.isOnlyPriceName(checkPrice.getId(),checkPriceName,merchantActivity.getStore().getId());
            if(isOnlyPriceName ==  false){
                resp.setErrorString("该奖券名称重复");
                resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
                return resp;
            }
            //多种情况的添加或修改逻辑
            boolean exist = false;
            Long storeId = merchantActivity.getStore().getId();
            MerchantActivity existActivity =  merchantActivityService.findStoreActivity(storeId);
            List<MerchantPrice> existPrices = null;
            if(existActivity != null){
                exist = true;
                existPrices = existActivity.getPrices();
            }
            List<MerchantPrice> prices = merchantActivity.getPrices();
            for (MerchantPrice price : prices) {
                if(exist){
                    price.setLogoUrl(existActivity.getStore().getMerchant().getLogo());
                    price.setStoreId(existActivity.getStore().getId());
                    price.setActivity(existActivity);
                }else{
                    MerchantStore ms = merchantStoreService.findById(merchantActivity.getStore().getId());
                    price.setLogoUrl(ms.getMerchant().getLogo());
                    price.setStoreId(merchantActivity.getStore().getId());
                    price.setActivity(merchantActivity);
                }
            }
            MerchantActivity activity = null;
            if(exist){
                //id存在
                Long priceId = merchantActivity.getPrices().get(0).getId();
                if(priceId!= null){
                    MerchantPrice findPrice = merchantPriceService.findById(priceId);
                    if(findPrice!=null){
                        //标记是否有已激活的奖券被修改
                        boolean flag = false;
                        //修改
                        for (MerchantPrice price : existPrices) {
                            if(price.getId() == priceId){
                                if(price.getActiveStatus() == Constants.PRICE_START){
                                    flag = true;
                                }
                                //redis放缓存
                                MerchantPrice getPrice = prices.get(0);
                                /*price.setName(getPrice.getName());
                                price.setDescription(getPrice.getDescription());
                                price.setStartDate(getPrice.getStartDate());
                                price.setEndDate(getPrice.getEndDate());
                                price.setTotal(getPrice.getTotal());*/
                                //修改参数
                                findPrice.setName(getPrice.getName());
                                findPrice.setDescription(getPrice.getDescription());
                                findPrice.setStartDate(getPrice.getStartDate());
                                findPrice.setEndDate(getPrice.getEndDate());
                                findPrice.setTotal(getPrice.getTotal());
                                MerchantPrice updatePrice = merchantPriceService.update(findPrice);
                                break;
                            }
                        }
                        existActivity.setPrices(existPrices);
                        activity = existActivity;
                        //修改重置抽奖规则表
                        if(flag){
                            List<MerchantPrice> activePrice = Lists.newArrayList();
                            for (MerchantPrice existPrice : existPrices) {
                                if(existPrice.getActiveStatus() == Constants.PRICE_START){
                                    activePrice.add(existPrice);
                                }
                            }
                            List<Integer> newPriceList = getPriceCountList(activePrice);
                            existActivity.setActivityStatus(Constants.ACTIVITY_ACTIVE);
                            existActivity.setPriceList(JSON.toJSONString(newPriceList));
                            merchantActivityService.update(existActivity);
                        }
                    }else{
                        //去除id
                        prices.get(0).setId(null);
                        existPrices.add(prices.get(0));
                        existActivity.setPrices(existPrices);
                        activity = merchantActivityService.update(existActivity);
                    }
                }else{
                    //id不存在情况
                    existPrices.add(merchantActivity.getPrices().get(0));
                    existActivity.setPrices(existPrices);
                    activity = merchantActivityService.update(existActivity);
                }
            }else{
                activity = merchantActivityService.update(merchantActivity);
            }
            //添加或更新的时候，修改redis对应的商户的活动List
            //CacheUtil.activityCache(ignite).put(storeId, activity);
            resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
            resp.addEntry("result", activity);
            return resp;
        } catch (Exception e){
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);
    }

    /**
     * 修改奖券活动状态  暂时可不用 暂存
     * @param storeId
     * @param status  0为不
     * @return
     */
    @RequestMapping(value = "api/activity/delete",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteActivity(@RequestParam("storeId")Long storeId,@RequestParam("status")int status){
        try {
            MerchantActivity activity = merchantActivityService.findStoreActivity(storeId);
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
            //先对需要启动的奖券信息进行过期验证
            for (Long priceId : priceIds) {
                MerchantPrice openPrice = merchantPriceService.findById(priceId);
                //添加时间判断
                Date endDate = openPrice.getEndDate();
                Date startToday = DateTimeUtils.startToday();
                if (endDate.compareTo(startToday) < 0) {
                    resp.setErrorString("启动的奖券有效期不能已过期");
                    resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
                    return resp;
                }
            }
            //先开放活动
            activity.setActivityStatus(Constants.ACITIVITY_START);
            List<MerchantPrice> activePrice = Lists.newArrayList();
            //修改状态
            boolean active = false;
            for (MerchantPrice merchantPrice : activity.getPrices()) {
                if(Arrays.asList(priceIds).contains(merchantPrice.getId())){
                    merchantPrice.setActiveStatus(Constants.PRICE_START);
                    activePrice.add(merchantPrice);
                    active = true;
                }else{
                    merchantPrice.setActiveStatus(Constants.PRICE_END);
                }
                merchantPriceService.update(merchantPrice);
            }
            if(active){
                activity.setActivityStatus(Constants.ACTIVITY_ACTIVE);
            }
            //生成抽奖的List
            List<Integer> drawList = getPriceCountList(activePrice);
            activity.setPriceList(JSON.toJSONString(drawList));
            merchantActivityService.update(activity);
            //放置于redis中
            /*if(drawList==null||drawList.size()==0){
                redisUtil.delete(Constants.PRICEDRAW + "_" + storeId);
            }else{
                redisUtil.set(Constants.PRICEDRAW + "_" + storeId, drawList, 365, TimeUnit.DAYS);
            }*/
            //改存放数据库
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

    /**
     * 微信抽奖是否开启
     * @param storeId
     * @param status  0为开启 1为关闭
     * @return
     */
    @RequestMapping(value = "api/activity/modeSwitch")
    @ResponseBody
    public AjaxResponse modeSwitch(@RequestParam("storeId")Long storeId,@RequestParam("status")int status){
        try {
            AjaxResponse resp = new AjaxResponse();
            ModeSwitch modeSwitch = modeSwitchService.findByStoreId(storeId);
            if(modeSwitch!= null){
                modeSwitch.setStatus(status);
                modeSwitchService.update(modeSwitch);
                if(Constants.WECHAT_OPEN == status){
                    resp.setErrorString("开启成功");
                }else{
                    resp.setErrorString("关闭成功");
                }
            }else{
                ModeSwitch ms = new ModeSwitch();
                ms.setStatus(status);
                ms.setStoreId(storeId);
                modeSwitchService.save(ms);
                resp.setErrorString("开启成功");
            }
            resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
            return resp;
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
