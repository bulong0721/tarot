package com.myee.tarot.web.admin.controller.device;

import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.core.exception.ServiceException;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.device.service.DeviceUsedService;
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
 * Created by Administrator on 2016/6/2.
 */
@Controller
public class DeviceUsedController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceController.class);

    @Autowired
    private DeviceUsedService deviceUsedService;

    @RequestMapping(value = "/deviceUsed/paging", method = RequestMethod.GET)
    @ResponseBody
    public AjaxPageableResponse pagedeviceUsed(Model model, HttpServletRequest request, PageRequest pageRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        try {
            PageResult<DeviceUsed> pageResult = deviceUsedService.pageList(pageRequest);
            List<DeviceUsed> deviceUsedList = pageResult.getList();
            resp.setRecordsTotal(pageResult.getRecordsTotal());
            resp.setRecordsFiltered(pageResult.getRecordsFiltered());
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
                resp.addDataEntry(entry);
            }
        } catch (Exception e) {
            LOGGER.error("Error while paging products", e);
        }
        return resp;
    }

    @RequestMapping(value = "/deviceUsed/update", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse updateDeviceUsed(@Valid @RequestBody DeviceUsed deviceUsed, HttpServletRequest request) {
        try {
            deviceUsedService.update(deviceUsed);
            return AjaxResponse.success();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);
    }
}
