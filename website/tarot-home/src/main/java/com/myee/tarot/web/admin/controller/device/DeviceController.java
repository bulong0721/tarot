package com.myee.tarot.web.admin.controller.device;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.myee.tarot.catalog.domain.Device;
import com.myee.tarot.catalog.domain.DeviceAttribute;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.exception.ServiceException;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.device.service.DeviceAttributeService;
import com.myee.tarot.device.service.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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

    @RequestMapping(value = "device/paging", method = RequestMethod.GET)
    @ResponseBody
    public AjaxPageableResponse pagedevice(Model model, HttpServletRequest request, PageRequest pageRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        try {
            PageResult<Device> pageList = deviceService.pageList(pageRequest);
            List<Device> deviceList = pageList.getList();
            for (Device device : deviceList) {
                resp.addDataEntry(objectToEntry(device));
            }
            resp.setRecordsTotal(pageList.getRecordsTotal());
        } catch (Exception e) {
            LOGGER.error("Error while paging products", e);
        }
        return resp;
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(Device device) {
        Map entry = new HashMap();
        entry.put("id",device.getId());
        entry.put("name",device.getName());
        entry.put("versionNum",device.getVersionNum());
        entry.put("description",device.getDescription());
        List<AttributeDTO> attributeDTOs = Lists.transform(device.getAttributes(), new Function<DeviceAttribute, AttributeDTO>() {
            @Nullable
            @Override
            public AttributeDTO apply(DeviceAttribute input) {
                return new AttributeDTO(input);
            }
        });
        entry.put("attributes", attributeDTOs);
        return entry;
    }

    @RequestMapping(value = "device/update", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse updateDevice(@Valid @RequestBody Device device, HttpServletRequest request) {
        try {
            AjaxResponse resp = new AjaxResponse();
            device = deviceService.update(device);
            resp = AjaxResponse.success();
            resp.addEntry("updateResult", objectToEntry(device));
            return resp;
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);
    }

    @RequestMapping(value = "device/list", method = RequestMethod.GET)
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

    @RequestMapping(value = "device/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteDevice(@Valid @RequestBody Device device, HttpServletRequest request) {
        try {
            AjaxResponse resp = new AjaxResponse();
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
                resp.setErrorString("请先切换门店");
                return resp;
            }
            //先手动删除所有该对象关联的属性，再删除该对象。因为关联关系是属性多对一该对象，关联字段放在属性表里，不能通过删对象级联删除属性。
            deviceAttributeService.deleteByDeviceId(device.getId());

            Device deviceUsed1 = deviceService.findById(device.getId());
            deviceService.delete(deviceUsed1);
            return AjaxResponse.success();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);
    }

    @RequestMapping(value = "device/attribute/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse saveAttribute(@ModelAttribute Device device, @Valid @RequestBody DeviceAttribute attribute, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        DeviceAttribute entity = attribute;
        if (null != attribute.getId()) {
            entity = deviceAttributeService.findById(attribute.getId());
            entity.setName(attribute.getName());
            entity.setValue(attribute.getValue());
        } else {
            entity.setDevice(device);
        }
        entity = deviceAttributeService.update(entity);
        entity.setDevice(null);
        resp = AjaxResponse.success();
        resp.addEntry("updateResult", entity);
        return resp;
    }

    @RequestMapping(value = "device/attribute/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteAttribute(@Valid @RequestBody DeviceAttribute attribute,  HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            if (null != attribute.getId()) {
                DeviceAttribute entity = deviceAttributeService.findById(attribute.getId());
                deviceAttributeService.delete(entity);
            }
            return AjaxResponse.success();
        } catch (Exception e) {
            e.printStackTrace();
            resp.setErrorString("删除设备类型属性异常");
            LOGGER.error("Error delete productAttributes", e);
        }
        return resp;

    }

}
