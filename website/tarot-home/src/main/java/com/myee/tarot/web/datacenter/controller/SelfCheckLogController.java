package com.myee.tarot.web.datacenter.controller;

import com.google.common.collect.Maps;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.datacenter.domain.EventLevel;
import com.myee.tarot.datacenter.domain.EventModule;
import com.myee.tarot.datacenter.domain.SelfCheckLog;
import com.myee.tarot.datacenter.service.ModuleLogService;
import com.myee.tarot.datacenter.service.SelfCheckLogService;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.core.util.WhereRequest;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ray.Fu on 2016/7/18.
 */
@Controller
public class SelfCheckLogController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SelfCheckLogController.class);

    @Value("${cleverm.push.dirs}")
    private String DOWNLOAD_HOME;

    @Autowired
    private SelfCheckLogService selfCheckLogService;

    @Autowired
    private ModuleLogService moduleLogService;

    @RequestMapping(value = "admin/selfCheckLog/paging", method = RequestMethod.GET)
    @ResponseBody
    public AjaxPageableResponse getSelfCheckLogList(HttpServletRequest request, WhereRequest whereRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        try {
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                resp.setErrorString("请先切换门店");
                return resp;
            }
            PageResult<SelfCheckLog> pageResult = selfCheckLogService.pageAll(whereRequest);
            List<SelfCheckLog> selfCheckLogList = pageResult.getList();
            for (SelfCheckLog deviceUsed : selfCheckLogList) {
                resp.addDataEntry(objectToEntry(deviceUsed));
            }
            resp.setRecordsTotal(pageResult.getRecordsTotal());
        } catch (Exception e) {
            LOGGER.error("Error while paging products", e);
        }
        return resp;
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(SelfCheckLog selfCheckLog) {
        Map entry = Maps.newHashMap();
        entry.put("id",selfCheckLog.getId());
        entry.put("data",selfCheckLog.getData());
        entry.put("level", selfCheckLog.getEventLevel());
        entry.put("moduleName",selfCheckLog.getEventModule().getModuleName());
        entry.put("functionName",selfCheckLog.getEventModule().getFunctionName());
        entry.put("length",selfCheckLog.getLength());
        entry.put("time",selfCheckLog.getTime());
        return entry;
    }

    @RequestMapping(value = "admin/selfCheckLog/listModule" , method = RequestMethod.GET)
    @ResponseBody
    public List getListModule() throws Exception {
        List resp = new ArrayList();
        try {
            List<EventModule> list = moduleLogService.getModuleList();
            for (EventModule eventModule : list) {
                Map entry = Maps.newHashMap();
                entry.put("name", eventModule.getModuleName());
                entry.put("value", eventModule.getModuleId());
                resp.add(entry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }

    @RequestMapping(value = "admin/selfCheckLog/listFunction" , method = RequestMethod.GET)
    @ResponseBody
    public List getListFuctionByModuleId(Integer moduleId) throws Exception {
        List resp = new ArrayList();
        try {
            List<EventModule> list = moduleLogService.getFunctionListByModule(moduleId);
            for (EventModule eventModule : list) {
                Map entry = Maps.newHashMap();
                entry.put("name", eventModule.getFunctionName());
                entry.put("value", eventModule.getFunctionId());
                resp.add(entry);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resp;
    }
}
