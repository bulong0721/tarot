package com.myee.tarot.web.configuration.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.myee.tarot.catalog.domain.ProductUsed;
import com.myee.tarot.catalog.service.ProductUsedService;
import com.myee.tarot.configuration.ReceiptPrintedItemService;
import com.myee.tarot.configuration.domain.ReceiptPrinted;
import com.myee.tarot.configuration.domain.ReceiptPrintedItem;
import com.myee.tarot.configuration.service.ReceiptPrintedService;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.exception.ServiceException;
import com.myee.tarot.core.util.DateUtil;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.merchant.domain.MerchantStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * Created by Ray.Fu on 2016/12/20.
 */
@Controller
public class ConfigurationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationController.class);

    @Autowired
    private ReceiptPrintedService receiptPrintedService;
    @Autowired
    private ReceiptPrintedItemService receiptPrintedItemService;

    @Autowired
    private ProductUsedService productUsedService;

    @RequestMapping(value = {"admin/configuration/receiptPrinted/paging"}, method = RequestMethod.GET)
    @ResponseBody
    public AjaxPageableResponse displayDashboard(HttpServletRequest request) {
        AjaxPageableResponse ajaxPageableResponse = new AjaxPageableResponse();
        try {
            Object o = request.getSession().getAttribute(Constants.ADMIN_STORE);
            if (o == null) {
                ajaxPageableResponse.setErrorString("请先切换门店");
                return ajaxPageableResponse;
            }
            MerchantStore merchantStore1 = (MerchantStore) o;

//            List<ProductUsed> productUsedList = productUsedService.listByIDs(Lists.newArrayList(merchantStore1.getId()));
            PageResult<ReceiptPrinted> receiptPrintedPageResult = receiptPrintedService.listByMerchantStoreId(merchantStore1.getId());
            List<ReceiptPrinted> receiptPrintedList = receiptPrintedPageResult.getList();
            for (ReceiptPrinted receiptPrinted : receiptPrintedList) {
                ajaxPageableResponse.addDataEntry(objectToEntry(receiptPrinted));
            }
            ajaxPageableResponse.setRecordsTotal(receiptPrintedPageResult.getRecordsTotal());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return ajaxPageableResponse;
    }

    @RequestMapping(value = {"admin/configuration/receiptPrinted/update"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse updateReceiptPrinted(@Valid @RequestBody ReceiptPrinted receiptPrinted, @RequestParam(value = "items") String items, HttpServletRequest request) {
        AjaxResponse ajaxResponse = new AjaxResponse();
        try {
            Object o = request.getSession().getAttribute(Constants.ADMIN_STORE);
            if (o == null) {
                ajaxResponse.setErrorString("请先切换门店");
                return ajaxResponse;
            }
            MerchantStore merchantStore1 = (MerchantStore) o;
            ReceiptPrinted target;
            if (receiptPrinted.getId() != null) {
                target = receiptPrintedService.findById(receiptPrinted.getId());
                List<ReceiptPrintedItem> itemsTemp = target.getItems();
                List<Long> ids = Lists.newArrayList();
                if (itemsTemp != null && itemsTemp.size() > 0) {
                    for (ReceiptPrintedItem item : itemsTemp) {
                        ids.add(item.getId());
                    }
                }
                receiptPrintedItemService.deleteByIds(ids);
            } else {
                target = new ReceiptPrinted();
            }
            target.setStore(merchantStore1);
            target.setUpdateTime(new Date());
            target.setModuleName(receiptPrinted.getModuleName());
            target.setReceiptType(receiptPrinted.getReceiptType());
            target.setDescription(receiptPrinted.getDescription());
            target = receiptPrintedService.update(target);

            List<ReceiptPrintedItem> receiptPrintedItemList = JSON.parseArray(items, ReceiptPrintedItem.class);
            List<ReceiptPrintedItem> tempReceiptPrintedItemList = Lists.newArrayList();
            if (receiptPrintedItemList != null && receiptPrintedItemList.size() > 0) {
                for (ReceiptPrintedItem receiptPrintedItem : receiptPrintedItemList) {
                    receiptPrintedItem.setReceiptPrinted(target);
                    ReceiptPrintedItem receiptPrintedItem1 = receiptPrintedItemService.update(receiptPrintedItem);
                    tempReceiptPrintedItemList.add(receiptPrintedItem1);
                }
            }
            ajaxResponse = AjaxResponse.success("添加成功");
            ajaxResponse.addEntry("updateResult", objectToEntry(target));
            ajaxResponse.addEntry("updateReceiptPrintedItems", tempReceiptPrintedItemList);
    } catch (Exception e) {
        LOGGER.error(e.getMessage(),e);
        ajaxResponse = AjaxResponse.failed(-1, "失败");
    }
        return ajaxResponse;
    }



    @RequestMapping(value = {"admin/configuration/receiptPrinted/delete"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteReceiptPrinted(@Valid @RequestBody ReceiptPrinted receiptPrinted, HttpServletRequest request) {
        AjaxResponse resp;
        try {
            Object o = request.getSession().getAttribute(Constants.ADMIN_STORE);
            if (o == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            ReceiptPrinted receiptPrinted1 = receiptPrintedService.findById(receiptPrinted.getId());
            if (receiptPrinted1 == null) {
                return AjaxResponse.failed(-1, "小票模板不存在。");
            }
            List<ReceiptPrintedItem> list = receiptPrinted1.getItems();
            if (list != null && list.size() > 0) {
                for (ReceiptPrintedItem receiptPrintedItem : list) {
                    receiptPrintedItemService.delete(receiptPrintedItem);
                }
            }
            receiptPrintedService.delete(receiptPrinted1);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return AjaxResponse.failed(-1, "删除失败");
        }
        return AjaxResponse.success();
    }

    @RequestMapping(value = {"admin/configuration/receiptPrintedItem/list"}, method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse listItemsByReceiptPrintedId (@RequestParam("receiptPrintedId") String receiptPrintedId, HttpServletRequest request) {
        List<ReceiptPrintedItem> list = receiptPrintedItemService.listAllByReceiptPrintedId(Long.valueOf(receiptPrintedId));
        AjaxResponse ajaxResponse = new AjaxResponse();
        if (list != null && list.size() > 0) {
            for (ReceiptPrintedItem receiptPrintedItem : list) {
                receiptPrintedItem.setReceiptPrinted(null);
            }
        }
        ajaxResponse.addEntry("itemList", list);
        return ajaxResponse;
    }

    private Map<String, Object> objectToEntry(ReceiptPrinted receiptPrinted) {
        Map entry = new HashMap();
        entry.put("id", receiptPrinted.getId());
        entry.put("moduleName", receiptPrinted.getModuleName());
        entry.put("description", receiptPrinted.getDescription());
        entry.put("receiptType", receiptPrinted.getReceiptType());
        entry.put("updateTime", DateUtil.formatDateTime(receiptPrinted.getUpdateTime()));
        List<ReceiptPrintedItem> itemList = receiptPrinted.getItems();
        if (itemList != null && itemList.size() > 0) {
            for (ReceiptPrintedItem rpI : itemList) {
                rpI.setReceiptPrinted(null);
            }
        }
        entry.put("itemList", itemList);
        List<ProductUsed> list = receiptPrinted.getProductUsed();
        if (list != null && list.size() > 0) {
            for (ProductUsed productUsed : list) {
                productUsed.setReceiptPrinted(null);
            }
        }
        entry.put("productUsed", list);
        return entry;
    }
}
