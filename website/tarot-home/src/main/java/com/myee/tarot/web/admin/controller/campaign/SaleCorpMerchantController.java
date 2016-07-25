package com.myee.tarot.web.admin.controller.campaign;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.merchant.service.MerchantStoreService;
import com.myee.tarot.campaign.domain.SaleCorpMerchant;
import com.myee.tarot.campaign.service.SaleCorpMerchantService;
import me.chanjar.weixin.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/11.
 */
@Controller
public class SaleCorpMerchantController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaleCorpMerchant.class);

    @Autowired
    private SaleCorpMerchantService saleCorpMerchantService;
    @Autowired
    private MerchantStoreService merchantStoreService;

    /**
     * 添加引流关系
     * @param bindString
     * @param merchantId
     * @param request
     * @return
     */
    @RequestMapping(value = "api/sale/corp/bindShop", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saleCorpBindShop(@RequestParam(value = "bindString") String bindString,@RequestParam(value = "merchantId") Long merchantId, HttpServletRequest request) {
        try {
            AjaxResponse resp = new AjaxResponse();
            MerchantStore merchantStore = merchantStoreService.findById(merchantId);
            if (merchantStore == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("参数不正确");
                return resp;
            }
            SaleCorpMerchant corpMerchant = saleCorpMerchantService.findByMerchantId(merchantId);
            if(corpMerchant!=null){
                corpMerchant.setRelatedMerchants(bindString);
                saleCorpMerchantService.update(corpMerchant);
                resp.setStatusMessage("更新关系成功");
                resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
            }else{
                SaleCorpMerchant saleCorpMerchant = new SaleCorpMerchant();
                saleCorpMerchant.setMerchantId(merchantId);
                saleCorpMerchant.setRelatedMerchants(bindString);
                saleCorpMerchantService.save(saleCorpMerchant);
                resp.setStatusMessage("新建关系成功");
                resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
            }
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);
    }

    /**
     * 根据商户id 获取其关联的引流商户
     * 优化只传id和name
     * @param shopId
     * @return
     */
    @RequestMapping(value = "api/sale/corp/getBindShopsByShopId",method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse getBindShopsByShopId(@RequestParam("shopId")Long shopId){
        try {
            AjaxResponse resp = new AjaxResponse();
            SaleCorpMerchant saleCorpMerchant = saleCorpMerchantService.findByMerchantId(shopId);
            if(saleCorpMerchant!=null && StringUtils.isNotBlank(saleCorpMerchant.getRelatedMerchants())){
                List<Long> bindStoreIds = JSON.parseArray(saleCorpMerchant.getRelatedMerchants(), Long.class);
                for (Long bindStoreId : bindStoreIds) {
                    MerchantStore store = merchantStoreService.findById(bindStoreId);
                    resp.addDataEntry(objectToEntry(store));
                }
                resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
            }
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);
    }

    private Map objectToEntry(MerchantStore merchantStore) {
        Map entry = new HashMap();
        entry.put("id", merchantStore.getId());
        entry.put("name", merchantStore.getName());
        return entry;
    }


}
