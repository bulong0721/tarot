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
    private MerchantStoreService merchantStoreService;

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
                resp.addDataEntry(entry);
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
                resp.addDataEntry(entry);
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
    public AjaxResponse saveUsedProduct(@Valid @RequestBody ProductUsedView productUsedView, HttpServletRequest request) throws Exception {
//        MerchantStore merchantStore = merchantStoreService.getEntity(MerchantStore.class, productUsedView.getStoreId());
        AjaxResponse resp = new AjaxResponse();
        if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("请先切换门店");
            return resp;
        }
        MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);

        ProductUsed productUsed = new ProductUsed(productUsedView);
        productUsed.setStore(merchantStore1);
        productUsedService.update(productUsed);
        return AjaxResponse.success();
    }

    @RequestMapping(value = "/product/attribute/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saveAttribute(@ModelAttribute ProductUsed product, @Valid @RequestBody ProductUsedAttribute attribute, HttpServletRequest request) throws Exception {
        ProductUsedAttribute entity = attribute;
        if (null != attribute.getId()) {
            entity = productUsedAttributeService.findById(attribute.getId());
            entity.setName(attribute.getName());
            entity.setValue(attribute.getValue());
        } else {
            entity.setProductUsed(product);
        }
        productUsedAttributeService.update(entity);
        return AjaxResponse.success();
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

    @RequestMapping(value = "/product/attribute/listByProductId", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse listByProductId(@RequestParam Long parentId, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            if (StringUtil.isNullOrEmpty(parentId.toString())) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("参数不能为空");
                return resp;
            }
            List<ProductUsedAttribute> attributeList = productUsedAttributeService.listByProductId(parentId);
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
