package com.myee.tarot.web.admin.controller.device;

import com.myee.tarot.catalog.domain.Device;
import com.myee.tarot.catalog.domain.DeviceAttribute;
import com.myee.tarot.catalog.view.ProductUsedAttributeView;
import com.myee.tarot.core.exception.ServiceException;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.device.service.DeviceAttributeService;
import com.myee.tarot.device.service.DeviceService;
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
 * Created by Administrator on 2016/5/31.
 */

@Controller
public class DeviceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceAttributeService deviceAttributeService;

    @RequestMapping(value = "/device/paging", method = RequestMethod.GET)
    @ResponseBody
    public AjaxPageableResponse pagedevice(Model model, HttpServletRequest request, PageRequest pageRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        try {
            PageResult<Device> pageList = deviceService.pageList(pageRequest);
            List<Device> deviceList = pageList.getList();
            for (Device device : deviceList) {
                Map entry = new HashMap();
                entry.put("id",device.getId());
                entry.put("name",device.getName());
                entry.put("versionNum",device.getVersionNum());
                entry.put("description",device.getDescription());
                resp.addDataEntry(entry);
            }
            resp.setRecordsTotal(pageList.getRecordsTotal());
            resp.setRecordsFiltered(pageList.getRecordsFiltered());
        } catch (Exception e) {
            LOGGER.error("Error while paging products", e);
        }
        return resp;
    }

    @RequestMapping(value = "/device/update", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse updateDevice(@Valid @RequestBody Device device, HttpServletRequest request) {
        try {
            deviceService.update(device);
            return AjaxResponse.success();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);
    }

    @RequestMapping(value = "/device/list", method = RequestMethod.GET)
    @ResponseBody
    public List deviceList(HttpServletRequest request) {
        AjaxResponse resp = new AjaxResponse();
        try {
            List<Device> deviceList = deviceService.list();
            for (Device device : deviceList) {
                Map entry = new HashMap();
                entry.put("name",device.getName());
                entry.put("value",device.getId());
                resp.addDataEntry(entry);
            }
            return resp.getRows();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/device/attribute/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saveAttribute(@Valid @RequestBody ProductUsedAttributeView productUsedAttributeView, HttpServletRequest request) throws Exception {
        Device device = deviceService.findById( productUsedAttributeView.getParentId());
        DeviceAttribute deviceAttribute = new DeviceAttribute();
        deviceAttribute.setId(productUsedAttributeView.getId());
        deviceAttribute.setName(productUsedAttributeView.getName());
        deviceAttribute.setValue(productUsedAttributeView.getValue());
        deviceAttribute.setDevice(device);

        deviceAttributeService.update(deviceAttribute);
        return AjaxResponse.success();
    }

    @RequestMapping(value = "/device/attribute/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public AjaxResponse deleteAttributeDevice(@RequestParam Long id, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            if (StringUtil.isNullOrEmpty(id.toString())) {
                resp.setErrorString("参数不能为空");
                return resp;
            }
            DeviceAttribute deviceAttribute = deviceAttributeService.findById(id);
            deviceAttributeService.delete(deviceAttribute);
            return AjaxResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("删除产品属性异常");
            LOGGER.error("Error delete productAttributes", e);
        }
        return resp;

    }

    @RequestMapping(value = "/device/attribute/listByProductId", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse listByDeviceId(@RequestParam Long parentId, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            if (StringUtil.isNullOrEmpty(parentId.toString())) {
                resp.setErrorString("参数不能为空");
                return resp;
            }
            List<DeviceAttribute> attributeList = deviceAttributeService.listByDeviceId(parentId);
            for (DeviceAttribute attribute : attributeList) {
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
