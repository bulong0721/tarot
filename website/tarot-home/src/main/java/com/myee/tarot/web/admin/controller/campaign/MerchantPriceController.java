package com.myee.tarot.web.admin.controller.campaign;

import com.myee.tarot.core.Constants;
import com.myee.tarot.core.exception.ServiceException;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.campaign.domain.MerchantPrice;
import com.myee.tarot.campaign.service.MerchantPriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Administrator on 2016/7/11.
 */
@Controller
public class MerchantPriceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MerchantPrice.class);

    @Autowired
    private MerchantPriceService merchantPriceService;

    /**
     * 添加个奖项
     * @param merchantPrice
     * @return
     */
    @RequestMapping(value = "api/price/saveOrUpdate",method = RequestMethod.POST)
    @ResponseBody
    private AjaxResponse priceSave(@RequestBody MerchantPrice merchantPrice){
        try {
            AjaxResponse resp = new AjaxResponse();
            if(merchantPrice.getActivity()==null|| merchantPrice.getActivity().getId()==null){
                resp.setErrorString("添加活动ID不能为空");
                resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
                return resp;
            }
            MerchantPrice price = merchantPriceService.update(merchantPrice);
            resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
            resp.addEntry("result",price);
            return resp;
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);
    }


    /**
     * 删除 奖项
     * @param id
     * @return
     */
    @RequestMapping(value = "api/price/deleteById",method = RequestMethod.POST)
    @ResponseBody
    private AjaxResponse priceDelete(@RequestParam("id") Long id){
        try {
            MerchantPrice price = merchantPriceService.findById(id);
            if(price!=null){
                price.setDeleteStatus(Constants.DELETE_YES);
                merchantPriceService.update(price);
                return AjaxResponse.success();
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);
    }


}
