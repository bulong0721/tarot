package com.myee.tarot.web.configuration.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.myee.djinn.endpoint.TrunkingInterface;
import com.myee.djinn.rpc.bootstrap.ServerBootstrap;
import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.catalog.domain.ProductUsed;
import com.myee.tarot.catalog.service.ProductUsedService;
import com.myee.tarot.configuration.domain.ReceiptProductUsedXref;
import com.myee.tarot.configuration.service.ReceiptPrintedItemService;
import com.myee.tarot.configuration.domain.ReceiptPrinted;
import com.myee.tarot.configuration.domain.ReceiptPrintedItem;
import com.myee.tarot.configuration.service.ReceiptPrintedService;
import com.myee.tarot.configuration.service.ReceiptProductUsedXrefService;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.exception.ServiceException;
import com.myee.tarot.core.util.DateUtil;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.StringUtil;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.resource.domain.Notification;
import com.myee.tarot.resource.service.NotificationService;
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

    @Autowired
    private ServerBootstrap serverBootstrap;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ReceiptProductUsedXrefService receiptProductUsedXrefService;

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
                receiptPrintedItemService.deleteByReceiptPrintedId(receiptPrinted.getId());
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
            ajaxResponse.addEntry(Constants.RESPONSE_UPDATE_RESULT, objectToEntry(target));
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

    @RequestMapping(value = {"admin/configuration/receiptPrinted/push2ProductUsed"}, method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deviceUsedBindProductUsed(@RequestParam(value = "pushString") String pushString, @RequestParam(value = "receiptPrintedId") Long receiptPrintedId, HttpServletRequest request) {
        TrunkingInterface endpointInterface = null;
        AjaxResponse ajaxResponse = new AjaxResponse();
        ReceiptPrinted receiptPrinted = receiptPrintedService.findById(receiptPrintedId);
        List<ReceiptPrintedItem> list = receiptPrinted.getItems();
        String moduleContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        moduleContent += "<" + (receiptPrinted.getReceiptType() == "1" ? "lottery" : "waittoken") + ">\n";
        for (int i = 0; i < list.size(); i++) {
            moduleContent += "<" + (list.get(i).isItemType() ? "variable" : "text") +
                    (list.get(i).isNewline() ? " isnewline=\"true\"" : " isnewline=\"false\"") +
                    (StringUtil.isNullOrEmpty(list.get(i).getFont()) ? "" : " font=" + list.get(i).getFont()) +
                    (list.get(i).isBold() ? " isbold=\"true\"" : " isbold=\"false\"") +
                    (list.get(i).isUnderline() ? " isunderline=\"true\"" : " isunderline=\"false\"") +
                    (StringUtil.isNullOrEmpty(list.get(i).getAlign()) ? "" : " align=" + list.get(i).getAlign()) +
                    (StringUtil.isNullOrEmpty(list.get(i).getSize()) ? "" : " size=" + list.get(i).getSize()) + ">" +
                    list.get(i).getContent() +
                    "</" + (list.get(i).isItemType() ? "variable" : "text") + ">\n";
        }
        moduleContent += "</" + (receiptPrinted.getReceiptType() == "1" ? "lottery" : "waittoken") + ">";
        List<Long> ids = JSON.parseArray(pushString, Long.class);
        List<ProductUsed> productUsedList = productUsedService.listByIDs(ids);
        receiptPrinted.setProductUsed(productUsedList);
        try {
            //手动更新关联关系表
            ReceiptProductUsedXref receiptProductUsedXref = null;
            String type = receiptPrinted.getReceiptType();
            for (ProductUsed productUsed : productUsedList) {
                ReceiptProductUsedXref receiptProductUsedXref_DB = null;
//                    //同一类型下同一设备的绑定关系只能有一条记录，自研平板类型除外
                receiptProductUsedXref_DB = receiptProductUsedXrefService.getByTypeAndProductUsedId(type, productUsed.getId());
                if (receiptProductUsedXref_DB != null) {
                    receiptProductUsedXref = receiptProductUsedXref_DB;
                } else {
                    receiptProductUsedXref = new ReceiptProductUsedXref();
                }
                receiptProductUsedXref.setProductUsedId(productUsed.getId());
                receiptProductUsedXref.setReceiptId(receiptPrinted.getId());
                receiptProductUsedXref.setType(receiptPrinted.getReceiptType());
                receiptProductUsedXrefService.update(receiptProductUsedXref);
            }
        } catch (Exception e) {
            return AjaxResponse.failed(-2, "入库错误");
        }
        try {
            if (productUsedList != null && productUsedList.size() > 0) {
                for (int i = 0; i < productUsedList.size(); i++) {
                    if (moduleContent == null || StringUtil.isNullOrEmpty(moduleContent)) {
                        return AjaxResponse.failed(-1, "数据有误");
                    }
                    for (DeviceUsed deviceUsed : productUsedList.get(i).getDeviceUsed()) {
                        if (deviceUsed.getDevice().getName().equals(Constants.DEVICE_NAME_BREAST)) {
                            endpointInterface = serverBootstrap.getClient(TrunkingInterface.class, deviceUsed.getBoardNo());
                        }
                    }
                }
            }
        } catch (Exception e) {
            return AjaxResponse.failed(-2, "连接客户端错误");
        }
        if (endpointInterface == null) {
            return AjaxResponse.failed(-3, "获取接口出错");
        }
        if (StringUtil.isNullOrEmpty(moduleContent)) {
            return AjaxResponse.failed(-4, "推送内容为空或格式错误");
        }
        boolean isSuccess = false;
        Notification notification = new Notification();
        try {
            //入库
            String notificationUUID = UUID.randomUUID().toString();
            notification.setUuid(notificationUUID);
            isSuccess = endpointInterface.receiveReceiptModule(moduleContent);
            if (isSuccess) {
                notification.setSuccess(true);
                notification.setComment("推送成功");
                notificationService.update(notification);
                return AjaxResponse.success("推送成功");
            } else {
                notification.setSuccess(false);
                notification.setComment("发送失败，客户端出错");
                notificationService.update(notification);
                return AjaxResponse.failed(-6, "发送失败，客户端出错");
            }
        } catch (ServiceException e) {
                LOGGER.error(e.getMessage(), e);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                return AjaxResponse.failed(-5, "客户端不存在或网络无法连接");
            }
            return null;
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
        List<ReceiptProductUsedXref> listXref = receiptProductUsedXrefService.listByReceiptPrintedId(receiptPrinted.getId());
        List<ProductUsed> productUsedList = Lists.newArrayList();
        for (ReceiptProductUsedXref receiptProductUsedXref : listXref) {
            ProductUsed p = new ProductUsed();
            p.setId(receiptProductUsedXref.getProductUsedId());
            p.setDeviceUsed(null);
            p.setAttributes(null);
            productUsedList.add(p);
        }
        entry.put("productUsedList", productUsedList);
        return entry;
    }
}
