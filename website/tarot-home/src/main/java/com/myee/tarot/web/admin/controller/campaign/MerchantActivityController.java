package com.myee.tarot.web.admin.controller.campaign;

import com.alibaba.fastjson.JSON;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.exception.ServiceException;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.merchant.service.MerchantStoreService;
import com.myee.tarot.campaign.domain.MerchantActivity;
import com.myee.tarot.campaign.domain.MerchantPrice;
import com.myee.tarot.campaign.service.MerchantActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Administrator on 2016/7/11.
 */
@Controller
public class MerchantActivityController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MerchantActivity.class);

    @Autowired
    private MerchantActivityService merchantActivityService;
    @Autowired
    private MerchantStoreService merchantStoreService;

    /**
     * 添加个新的奖券活动
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
            List<MerchantPrice> activities = merchantActivity.getPrices();
            for (MerchantPrice activity : activities) {
                activity.setActivity(merchantActivity);
            }
            merchantActivity.setActiveStatus(Constants.ACTIVITY_END); //先默认关闭
            MerchantActivity activity = merchantActivityService.update(merchantActivity);
            resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
            resp.addEntry("result", activity);
            return resp;
        } catch (ServiceException e){
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);
    }

    /**
     * 删除个奖券活动
     * @param activityId
     * @return
     */
    @RequestMapping(value = "api/activity/delete",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteActivity(@RequestParam("activityId")Long activityId){
        try {
            MerchantActivity activity = merchantActivityService.findById(activityId);
            if(activity!=null){
                merchantActivityService.delete(activity);
                return AjaxResponse.success();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);
    }

    /**
     * 根据商户id寻找所有活动
     * @param storeId
     * @return
     */
    @RequestMapping(value = "api/activity/findStoreActivity",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse findStoreActivity(@RequestParam("storeId")Long storeId){
        try {
            AjaxResponse response = new AjaxResponse();
            List<MerchantActivity> result = merchantActivityService.findStoreActivity(storeId);
            response.addEntry("result", result);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);
    }

    /**
     * 修改商户激活的活动
     * @param storeId
     * @param activityId
     * @return
     */
    @RequestMapping(value = "api/activity/openActivity",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse openActivity(@RequestParam("storeId")Long storeId,@RequestParam("activityId")Long activityId){
        try {
            AjaxResponse resp = new AjaxResponse();
            List<MerchantActivity> activities = merchantActivityService.findStoreActivity(storeId);
            //先设定所有活动关闭
            for (MerchantActivity activity : activities) {
                if(activity.getId()==activityId){
                    activity.setActiveStatus(Constants.ACTIVITY_START);
                    resp.setErrorString("修改成功");
                }else {
                    activity.setActiveStatus(Constants.ACTIVITY_END);
                }
                merchantActivityService.update(activity);
            }
            resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);
    }

    /**
     * 根据活动ID 获取所有奖项设置
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
            response.addEntry("activity",activity);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);
    }


}
