package com.myee.tarot.web.admin.controller.product;

import com.alibaba.fastjson.serializer.*;
import com.myee.tarot.catalog.domain.ProductUsed;
import com.myee.tarot.catalog.domain.ProductUsedAttribute;
import com.myee.tarot.catalog.type.ProductType;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.product.service.ProductUsedAttributeService;
import com.myee.tarot.product.service.ProductUsedService;
import com.myee.tarot.web.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
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

    @Autowired
    private ProductUsedAttributeService productUsedAttributeService;

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
                List<ProductUsedAttribute> attributes = new ArrayList<ProductUsedAttribute>();
                entry.put("id", productUsed.getCode());
                entry.put("code", productUsed.getCode());
                entry.put("name", productUsed.getName());
                entry.put("type", productUsed.getType());
                entry.put("productNum", productUsed.getProductNum());
                entry.put("description", productUsed.getDescription());
                entry.put("storeName", productUsed.getStore().getName());
                entry.put("storeId", productUsed.getStore().getId());
                entry.put("productTypeList", ProductType.getProductTypeList());
                for(ProductUsedAttribute attribute : productUsed.getProductUsedAttributeList()){
                    attribute.setProductUsed(null);
                    attributes.add(attribute);
                }
                entry.put("attributeList", attributes);
                resp.addDataEntry(entry);
            }
        } catch (Exception e) {
            LOGGER.error("Error while paging products", e);
        }
        return resp;
    }

    @RequestMapping(value = "/product/used/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saveUsedProduct(@Valid @RequestBody ProductUsed productUsed, HttpServletRequest request) throws Exception {
        productUsedService.update(productUsed);
        return AjaxResponse.success();
    }

    @RequestMapping(value = "/product/attribute/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public AjaxResponse deleteAttributeProduct(@RequestParam Long id, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            if (StringUtil.isNullOrEmpty(id.toString())) {
                resp.setErrorString("参数不能为空");
                return resp;
            }
            ProductUsedAttribute productUsedAttribute = productUsedAttributeService.getEntity(ProductUsedAttribute.class, id);
            productUsedAttributeService.delete(productUsedAttribute);
            return AjaxResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("删除产品属性异常");
        }
        return resp;

    }

    @RequestMapping(value = "/product/attribute/listByProductId", method = RequestMethod.GET)
    @ResponseBody
    public AjaxPageableResponse listByProductId(@RequestParam Long productId, HttpServletRequest request) throws Exception {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        try {
            if (StringUtil.isNullOrEmpty(productId.toString())) {
                resp.setErrorString("参数不能为空");
                return resp;
            }
            List<ProductUsedAttribute> attributeList = productUsedAttributeService.listByProductId(productId);
            for (ProductUsedAttribute attribute : attributeList) {
                Map entry = new HashMap();
                List<ProductUsedAttribute> attributes = new ArrayList<ProductUsedAttribute>();
                entry.put("id", attribute.getId());
                entry.put("name", attribute.getName());
                entry.put("value", attribute.getValue());
                resp.addDataEntry(entry);
            }
        } catch (Exception e) {
            LOGGER.error("Error while paging productAttributes", e);
        }
        return resp;

    }

    @RequestMapping(value = "/product/type/productOpts", method = RequestMethod.GET)
    public
    @ResponseBody
    List<ProductType> type(Model model, HttpServletRequest request) {
        return ProductType.getProductTypeList();
    }


}
