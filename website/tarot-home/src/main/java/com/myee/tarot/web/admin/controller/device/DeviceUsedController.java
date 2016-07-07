package com.myee.tarot.web.admin.controller.device;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.myee.tarot.catalog.domain.*;
import com.myee.tarot.catalog.view.ProductUsedAttributeView;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.exception.ServiceException;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.device.service.DeviceAttributeService;
import com.myee.tarot.device.service.DeviceUsedAttributeService;
import com.myee.tarot.device.service.DeviceUsedService;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.product.service.ProductUsedService;
import com.myee.tarot.web.admin.controller.ControllerConstants;
import com.myee.tarot.web.admin.controller.product.AttributeDTO;
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
 * Created by Administrator on 2016/6/2.
 */
@Controller
public class DeviceUsedController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    private DeviceUsedService deviceUsedService;
    @Autowired
    private DeviceUsedAttributeService deviceUsedAttributeService;
    @Autowired
    private DeviceAttributeService deviceAttributeService;
    @Autowired
    private ProductUsedService productUsedService;

    @RequestMapping(value = "/deviceUsed/paging", method = RequestMethod.GET)
    @ResponseBody
    public AjaxPageableResponse pagedeviceUsed(Model model, HttpServletRequest request, PageRequest pageRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        try {
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp.setErrorString("请先切换门店");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);

            PageResult<DeviceUsed> pageResult = deviceUsedService.pageListByStore(merchantStore1.getId(),pageRequest );
            List<DeviceUsed> deviceUsedList = pageResult.getList();
            for (DeviceUsed deviceUsed : deviceUsedList) {
                resp.addDataEntry(objectToEntry(deviceUsed));
            }
            resp.setRecordsTotal(pageResult.getRecordsTotal());
        } catch (Exception e) {
            LOGGER.error("Error while paging products", e);
        }
        return resp;
    }

    @RequestMapping(value = "/deviceUsed/update", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saveUsedProduct(@Valid @RequestBody DeviceUsed deviceUsed,@RequestParam(value = "autoStart")Long autoStart,@RequestParam(value = "autoEnd")Long autoEnd, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            if(autoEnd != null && autoStart !=null && autoEnd < autoStart){
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("结束编号不能小于开始编号");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);

            List<Object> updateResult = new ArrayList<Object>();
            deviceUsed.setStore(merchantStore1);
            if(autoEnd != null && autoStart !=null &&  autoEnd >= 0 && autoStart >= 0 ){//批量新增
                String commonName = deviceUsed.getName();
                for(Long i=autoStart;i < autoEnd+1;i++){
                    DeviceUsed deviceUsedResult = new DeviceUsed();
                    deviceUsed.setName(commonName + i);
                    deviceUsedResult = deviceUsedService.update(deviceUsed);
                    updateResult.add(objectToEntry(deviceUsedResult));
                }
            }
            else {//单个新增或修改
                deviceUsed = deviceUsedService.update(deviceUsed);
                updateResult.add(objectToEntry(deviceUsed));
            }

            resp = AjaxResponse.success();
            resp.addEntry("updateResult", updateResult);
        } catch (ServiceException e) {
            e.printStackTrace();
            resp = AjaxResponse.failed(-1);
        }
        return resp;
    }

    @RequestMapping(value = "/deviceUsed/bindProductUsed", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deviceUsedBindProductUsed(@RequestParam(value = "bindString") String bindString,@RequestParam(value = "deviceUsedId") Long deviceUsedId, HttpServletRequest request) {
        try {
            AjaxResponse resp = new AjaxResponse();
            List<Long> bindList = JSON.parseArray(bindString,Long.class);
            DeviceUsed deviceUsed = deviceUsedService.findById(deviceUsedId);
            if (deviceUsed == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("参数不正确");
                return resp;
            }
            List<ProductUsed> productUsedList = productUsedService.listByIDs(bindList);
            deviceUsed.setProductUsed(productUsedList);

            deviceUsed = deviceUsedService.update(deviceUsed);
            resp = AjaxResponse.success();
            resp.addEntry("updateResult", objectToEntry(deviceUsed));
            return resp;
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);
    }

    @RequestMapping(value = "/deviceUsed/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteDeviceUsed(@Valid @RequestBody DeviceUsed deviceUsed, HttpServletRequest request) {
        try {
            AjaxResponse resp = new AjaxResponse();
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            //先手动删除所有该对象关联的属性，再删除该对象。因为关联关系是属性多对一该对象，关联字段放在属性表里，不能通过删对象级联删除属性。
            deviceUsedAttributeService.deleteByDeviceUsedId(deviceUsed.getId());

            DeviceUsed deviceUsed1 = deviceUsedService.findById(deviceUsed.getId());
            deviceUsedService.delete(deviceUsed1);
            return AjaxResponse.success();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(DeviceUsed deviceUsed) {
        Map entry = new HashMap();
        entry.put("id",deviceUsed.getId());
        entry.put("name",deviceUsed.getName());
        entry.put("heartbeat",deviceUsed.getHeartbeat());
        entry.put("boardNo",deviceUsed.getBoardNo());
        entry.put("deviceNum",deviceUsed.getDeviceNum());
        entry.put("description",deviceUsed.getDescription());
        entry.put("store",deviceUsed.getStore());
        deviceUsed.getDevice().setAttributes(null);
        entry.put("device",deviceUsed.getDevice());
        if(deviceUsed.getProductUsed() != null ){
            for(ProductUsed productUsed : deviceUsed.getProductUsed()){
                productUsed.setDeviceUsed(null);
                productUsed.setAttributes(null);
            }
        }
        entry.put("productUsedList",deviceUsed.getProductUsed());
        List<AttributeDTO> attributeDTOs = Lists.transform(deviceUsed.getAttributes(), new Function<DeviceUsedAttribute, AttributeDTO>() {
            @Nullable
            @Override
            public AttributeDTO apply(DeviceUsedAttribute input) {
                return new AttributeDTO(input);
            }
        });
        entry.put("attributes", attributeDTOs);
        return entry;
    }

    @RequestMapping(value = "/deviceUsed/attribute/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saveAttribute(@ModelAttribute DeviceUsed deviceUsed, @Valid @RequestBody DeviceUsedAttribute attribute, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        DeviceUsedAttribute entity = attribute;
        if (null != attribute.getId()) {
            entity = deviceUsedAttributeService.findById(attribute.getId());
            entity.setName(attribute.getName());
            entity.setValue(attribute.getValue());
        } else {
            entity.setDeviceUsed(deviceUsed);
        }
        entity = deviceUsedAttributeService.update(entity);

        entity.setDeviceUsed(null);
        resp = AjaxResponse.success();
        resp.addEntry("updateResult", entity);
        return resp;
    }

    @RequestMapping(value = "/deviceUsed/attribute/delete", method = RequestMethod.POST)
    @ResponseBody
//    public AjaxResponse deleteAttributeDevice(@RequestParam Long id, HttpServletRequest request) throws Exception {
//        AjaxResponse resp = new AjaxResponse();
//        try {
//            if (StringUtil.isNullOrEmpty(id.toString())) {
//                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
//                resp.setErrorString("参数不能为空");
//                return resp;
//            }
//            DeviceUsedAttribute deviceAttribute = deviceUsedAttributeService.findById(id);
//            deviceUsedAttributeService.delete(deviceAttribute);
//            return AjaxResponse.success();
//        } catch (Exception e) {
//            e.printStackTrace();
//            resp.setErrorString("删除产品属性异常");
//            LOGGER.error("Error delete productAttributes", e);
//        }
//        return resp;
//
//    }
    public AjaxResponse deleteAttribute(@Valid @RequestBody DeviceUsedAttribute attribute,  HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            if (null != attribute.getId()) {
                DeviceUsedAttribute entity = deviceUsedAttributeService.findById(attribute.getId());
                deviceUsedAttributeService.delete(entity);
            }
            return AjaxResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("删除设备属性异常");
            LOGGER.error("Error delete productAttributes", e);
        }
        return resp;

    }


    @RequestMapping(value = "/deviceUsed/attribute/listByProductId", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse listByDeviceId(@RequestParam Long parentId, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            if (StringUtil.isNullOrEmpty(parentId.toString())) {
                resp.setErrorString("参数不能为空");
                return resp;
            }
            //查询设备实例的设备类型的公共属性
            DeviceUsed deviceUsed = deviceUsedService.findById(parentId);
            List<DeviceAttribute> commonAttributeList = deviceAttributeService.listByDeviceId(deviceUsed.getDevice().getId());
            for(DeviceAttribute commonAttribute :commonAttributeList){
                Map entry = new HashMap();
                entry.put("id", 0);//公共属性的id设为0，前端不显示编辑和删除按钮
                entry.put("name", commonAttribute.getName());
                entry.put("value", commonAttribute.getValue());
                resp.addDataEntry(entry);
            }

            //查询设备实例的私有属性
            List<DeviceUsedAttribute> attributeList = deviceUsedAttributeService.listByDeviceUsedId(parentId);
            for (DeviceUsedAttribute attribute : attributeList) {
                Map entry = new HashMap();
//                List<DeviceAttribute> attributes = new ArrayList<DeviceAttribute>();
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
}
