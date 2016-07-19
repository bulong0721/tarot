package com.myee.tarot.web.admin.controller.pricedraw;

import com.myee.tarot.core.exception.ServiceException;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.pricedraw.domain.PriceInfo;
import com.myee.tarot.pricedraw.service.PriceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2016/7/11.
 */
@Controller
@RequestMapping(value = "api")
public class PriceInfoController {

    @Autowired
    private PriceInfoService priceInfoService;

    /**
     * 保存一个用户的奖项记录
     * @param priceInfo
     * @return
     */
    @RequestMapping(value = "price/savePriceInfo",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saveOrUpdatePriceInfo(@RequestBody PriceInfo priceInfo){
        try {
            AjaxResponse resp = new AjaxResponse();
            PriceInfo result = priceInfoService.update(priceInfo);
            resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
            resp.addEntry("result",resp);
            return resp;
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);

    }


}
