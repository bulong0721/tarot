package com.myee.tarot.web.admin.controller.product;

import com.myee.tarot.catalog.domain.ProductUsed;
import com.myee.tarot.catalog.type.ProductType;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.product.service.ProductUsedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Enva on 2016/5/31.
 */
@Controller
public class ProductUsedController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductUsedController.class);

    @Autowired
    private ProductUsedService productUsedService;

    @RequestMapping(value = "/product/used/paging", method = RequestMethod.GET)
    public
    @ResponseBody
    AjaxPageableResponse pageUsers(Model model, HttpServletRequest request) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        String currentUser = request.getRemoteUser();
        try {
            List<ProductUsed> productUsedList = productUsedService.list();
            for (ProductUsed productUsed : productUsedList) {
                Map entry = new HashMap();
                entry.put("id", productUsed.getCode());
                entry.put("code", productUsed.getCode());
                entry.put("name", productUsed.getName());
                entry.put("type", productUsed.getType());
                entry.put("productNum", productUsed.getProductNum());
                entry.put("description", productUsed.getDescription());
                entry.put("storeName", productUsed.getStore().getName());
                entry.put("storeId", productUsed.getStore().getId());
                entry.put("productTypeList", ProductType.getProductTypeList());
                entry.put("attributeList", productUsed.getProductUsedAttributeList());
//                entry.put("attributeList", productUsed.getProductUsedAttribute());
                resp.addDataEntry(entry);
            }
        } catch (Exception e) {
            LOGGER.error("Error while paging products", e);
        }
        return resp;
    }

    @RequestMapping(value = "/product/used/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse mergeRole(@Valid @RequestBody ProductUsed productUsed, HttpServletRequest request) throws Exception {
        productUsedService.update(productUsed);
        return AjaxResponse.success();
    }

    @RequestMapping(value = "/product/used/type", method = RequestMethod.GET)
    public
    @ResponseBody
    List<ProductType> type(Model model, HttpServletRequest request) {
        return ProductType.getProductTypeList();
    }

//    @RequestMapping(value = "/product/used/type", method = RequestMethod.GET)
//    public
//    @ResponseBody
//    AjaxResponse type(Model model, HttpServletRequest request) {
//        AjaxResponse resp = new AjaxResponse();
//        Map entry = new HashMap();
//        entry.put("productTypeList", ProductType.getProductTypeList());
//        resp.addDataEntry(entry);
//        return resp;
//    }

}
