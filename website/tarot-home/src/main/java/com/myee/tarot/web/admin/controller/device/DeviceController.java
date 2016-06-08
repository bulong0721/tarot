package com.myee.tarot.web.admin.controller.device;

import com.myee.tarot.catalog.domain.Device;
import com.myee.tarot.core.exception.ServiceException;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.device.service.DeviceService;
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
 * Created by Administrator on 2016/5/31.
 */

@Controller
public class DeviceController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    private DeviceService deviceService;

    @RequestMapping(value = "/device/paging", method = RequestMethod.GET)
    @ResponseBody
    public AjaxPageableResponse pagedevice(Model model, HttpServletRequest request, PageRequest pageRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        try {
            PageResult<Device> pageList = deviceService.pageList(pageRequest);
            List<Device> deviceList = pageList.getList();
            resp.setRecordsTotal(pageList.getRecordsTotal());
            resp.setRecordsFiltered(pageList.getRecordsFiltered());
            for (Device device : deviceList) {
                Map entry = new HashMap();
                entry.put("id",device.getId());
                entry.put("name",device.getName());
                entry.put("versionNum",device.getVersionNum());
                entry.put("description",device.getDescription());
                resp.addDataEntry(entry);
            }
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



}
