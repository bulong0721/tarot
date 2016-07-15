package com.myee.tarot.web.admin.controller.pricedraw;

import com.myee.tarot.core.exception.ServiceException;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.pricedraw.domain.MerchantActivity;
import com.myee.tarot.pricedraw.domain.MerchantPrice;
import com.myee.tarot.pricedraw.service.MerchantActivityService;
import com.myee.tarot.pricedraw.service.MerchantPriceService;
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
    private MerchantPriceService merchantPriceService;

    /**
     * 添加个新的奖券活动
     * @param merchantActivity
     * @return
     */
    @RequestMapping(value = "activity/saveOrUpdate",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saveActivity(@RequestBody MerchantActivity merchantActivity){
        try {
            AjaxResponse resp = new AjaxResponse();
            MerchantActivity activity = merchantActivityService.update(merchantActivity);
            resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
            resp.addEntry("result",activity);
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
    @RequestMapping(value = "activity/delete",method = RequestMethod.POST)
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
    @RequestMapping(value = "activity/findStoreActivity",method = RequestMethod.POST)
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
     * 根据活动ID 获取所有奖项设置
     * @param activityId
     * @return
     */
    @RequestMapping(value = "activity/findPriceByActivityId",method = RequestMethod.POST)
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
