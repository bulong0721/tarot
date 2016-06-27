package com.myee.tarot.web.admin.controller.device;

import com.alibaba.fastjson.JSON;
import com.myee.tarot.catalog.domain.DeviceAttribute;
import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.catalog.domain.DeviceUsedAttribute;
import com.myee.tarot.catalog.domain.ProductUsed;
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
import com.myee.tarot.web.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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
                Map entry = new HashMap();
                entry.put("id",deviceUsed.getId());
                entry.put("name",deviceUsed.getName());
                entry.put("heartbeat",deviceUsed.getHeartbeat());
                entry.put("boardNo",deviceUsed.getBoardNo());
                entry.put("deviceNum",deviceUsed.getDeviceNum());
                entry.put("description",deviceUsed.getDescription());
                entry.put("store",deviceUsed.getStore());
                entry.put("device",deviceUsed.getDevice());
                for(int i=0;i<deviceUsed.getProductUsed().size();i++){
                    deviceUsed.getProductUsed().get(i).setDeviceUsed(null);
                }
                entry.put("productUsedList",deviceUsed.getProductUsed());
                resp.addDataEntry(entry);
            }
            resp.setRecordsTotal(pageResult.getRecordsTotal());
            resp.setRecordsFiltered(pageResult.getRecordsFiltered());
        } catch (Exception e) {
            LOGGER.error("Error while paging products", e);
        }
        return resp;
    }

    @RequestMapping(value = "/deviceUsed/update", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse updateDeviceUsed(@Valid @RequestBody DeviceUsed deviceUsed, HttpServletRequest request) {
        try {
            AjaxResponse resp = new AjaxResponse();
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            MerchantStore merchantStore1 = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
            deviceUsed.setStore(merchantStore1);

            //测试无实体关系表用的--------
//            List<ProductUsed> productUsedList = productUsedService.list();
//            deviceUsed.setProductUsed(productUsedList);
            //------------------------

            deviceUsedService.update(deviceUsed);
            return AjaxResponse.success();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);
    }

    @RequestMapping(value = "/deviceUsed/bindProductUsed", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deviceUsedBindProductUsed(@RequestParam(value = "bindString") String bindString,@RequestParam(value = "deviceUsedId") Long deviceUsedId, HttpServletRequest request) {
        try {
            AjaxResponse resp = new AjaxResponse();
            List<Long> bindList = JSON.parseArray(bindString,Long.class);
            DeviceUsed deviceUsed = deviceUsedService.getEntity(DeviceUsed.class,deviceUsedId);
            if (deviceUsed == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("参数不正确");
                return resp;
            }
            List<ProductUsed> productUsedList = productUsedService.listByIDs(bindList);
            deviceUsed.setProductUsed(productUsedList);

            deviceUsedService.update(deviceUsed);
            return AjaxResponse.success();
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

            DeviceUsed deviceUsed1 = deviceUsedService.getEntity(DeviceUsed.class,deviceUsed.getId());
            deviceUsedService.delete(deviceUsed1);
            return AjaxResponse.success();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);
    }

    @RequestMapping(value = "/deviceUsed/attribute/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saveAttribute(@Valid @RequestBody ProductUsedAttributeView productUsedAttributeView, HttpServletRequest request) throws Exception {
        DeviceUsed deviceUsed = deviceUsedService.getEntity(DeviceUsed.class, productUsedAttributeView.getParentId());//查出来为空，不知道为什么
        //看后台查询语句，发现是inner join ProductUsed时出的问题。
        DeviceUsedAttribute deviceUsedAttribute = new DeviceUsedAttribute();
        deviceUsedAttribute.setId(productUsedAttributeView.getId());
        deviceUsedAttribute.setName(productUsedAttributeView.getName());
        deviceUsedAttribute.setValue(productUsedAttributeView.getValue());
        deviceUsedAttribute.setDeviceUsed(deviceUsed);

        deviceUsedAttributeService.update(deviceUsedAttribute);
        return AjaxResponse.success();
    }

    @RequestMapping(value = "/deviceUsed/attribute/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public AjaxResponse deleteAttributeDevice(@RequestParam Long id, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            if (StringUtil.isNullOrEmpty(id.toString())) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("参数不能为空");
                return resp;
            }
            DeviceUsedAttribute deviceAttribute = deviceUsedAttributeService.getEntity(DeviceUsedAttribute.class, id);
            deviceUsedAttributeService.delete(deviceAttribute);
            return AjaxResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("删除产品属性异常");
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
