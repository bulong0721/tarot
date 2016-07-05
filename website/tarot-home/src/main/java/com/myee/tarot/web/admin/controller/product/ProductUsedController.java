package com.myee.tarot.web.admin.controller.product;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.myee.tarot.catalog.domain.ProductUsed;
import com.myee.tarot.catalog.domain.ProductUsedAttribute;
import com.myee.tarot.catalog.type.ProductType;
import com.myee.tarot.catalog.view.ProductUsedAttributeView;
import com.myee.tarot.catalog.view.ProductUsedView;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.merchant.service.MerchantStoreService;
import com.myee.tarot.product.service.ProductUsedAttributeService;
import com.myee.tarot.product.service.ProductUsedService;
import com.myee.tarot.web.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
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
    AjaxPageableResponse pageUsers(Model model, HttpServletRequest request, PageRequest pageRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        String currentUser = request.getRemoteUser();
        try {
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp.setErrorString("请先切换门店");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);

            PageResult<ProductUsed> pageList = productUsedService.pageByStore(merchantStore1.getId(), pageRequest);
            List<ProductUsed> productUsedList = pageList.getList();
            for (ProductUsed productUsed : productUsedList) {
                resp.addDataEntry(objectToEntry(productUsed));
            }
            resp.setRecordsTotal(pageList.getRecordsTotal());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error while paging products", e);
        }
        return resp;
    }

    @RequestMapping(value = "/product/used/listByStoreId", method = RequestMethod.GET)
    public
    @ResponseBody
    AjaxPageableResponse listByStoreId( HttpServletRequest request, PageRequest pageRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        String currentUser = request.getRemoteUser();
        try {
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp.setErrorString("请先切换门店");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);

            PageResult<ProductUsed> pageList = productUsedService.pageByStore(merchantStore1.getId(), pageRequest);
            List<ProductUsed> productUsedList = pageList.getList();
            for (ProductUsed productUsed : productUsedList) {
                resp.addDataEntry(objectToEntry(productUsed));
            }
            resp.setRecordsTotal(pageList.getRecordsTotal());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("Error while paging products", e);
        }
        return resp;
    }

    @RequestMapping(value = "/product/used/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saveUsedProduct(@Valid @RequestBody ProductUsed productUsed, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("请先切换门店");
            return resp;
        }
        MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);

        productUsed.setStore(merchantStore1);
        productUsed = productUsedService.update(productUsed);
        resp = AjaxResponse.success();
        resp.addEntry("updateResult", objectToEntry(productUsed));
        return resp;
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(ProductUsed productUsed) {
        Map entry = new HashMap();
        entry.put("id", productUsed.getId());
        entry.put("code", productUsed.getCode());
        entry.put("name", productUsed.getName());
        entry.put("type", productUsed.getType());
        entry.put("productNum", productUsed.getProductNum());
        entry.put("description", productUsed.getDescription());
        List<AttributeDTO> attributeDTOs = Lists.transform(productUsed.getAttributes(), new Function<ProductUsedAttribute, AttributeDTO>() {
            @Nullable
            @Override
            public AttributeDTO apply(ProductUsedAttribute input) {
                return new AttributeDTO(input);
            }
        });
        entry.put("attributes", attributeDTOs);
        return entry;
    }

    @RequestMapping(value = "/product/attribute/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saveAttribute(@ModelAttribute ProductUsed product, @Valid @RequestBody ProductUsedAttribute attribute, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        ProductUsedAttribute entity = attribute;
        if (null != attribute.getId()) {
            entity = productUsedAttributeService.findById(attribute.getId());
            entity.setName(attribute.getName());
            entity.setValue(attribute.getValue());
        } else {
            entity.setProductUsed(product);
        }
        entity = productUsedAttributeService.update(entity);

//        entity.getProductUsed().setAttributes(null);
        entity.setProductUsed(null);
        resp = AjaxResponse.success();
        resp.addEntry("updateResult", entity);
        return resp;
    }

    @RequestMapping(value = "/product/attribute/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteAttributeProduct(@Valid @RequestBody ProductUsedAttribute attribute,  HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            if (null != attribute.getId()) {
                ProductUsedAttribute entity = productUsedAttributeService.findById(attribute.getId());
                productUsedAttributeService.delete(entity);
            }
            return AjaxResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("删除产品属性异常");
            LOGGER.error("Error delete productAttributes", e);
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
