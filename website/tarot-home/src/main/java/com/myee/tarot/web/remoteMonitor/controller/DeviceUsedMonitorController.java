package com.myee.tarot.web.remoteMonitor.controller;

import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.StringUtil;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.metrics.domain.AppInfo;
import com.myee.tarot.metrics.domain.MetricsInfo;
import com.myee.tarot.metrics.domain.SystemMetrics;
import com.myee.tarot.remote.service.SystemMetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chay on 2016/6/3.
 */
@Controller
public class DeviceUsedMonitorController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceUsedMonitorController.class);

    @Autowired
    private SystemMetricsService systemMetricsService;

    @RequestMapping(value = {"admin/remoteMonitor/deviceUsed/summary"}, method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse getDeviceUsedSummary(@RequestParam(value = "deviceUsedId") Long deviceUsedId,
                                             HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                return AjaxResponse.failed(-1,"请先切换门店");
            }
            if(deviceUsedId == null || StringUtil.isBlank(deviceUsedId+"")){
                return AjaxResponse.failed(-1,"参数错误");
            }

            //从缓存中查询数据，如果缓存无数据，则从数据库查数据，并将结果存入缓存
            //因为redis是key-value存储，没办法实现数据库式的查询操作。
            SystemMetrics systemMetrics = systemMetricsService.getLatestByDUId(deviceUsedId);
            resp.addDataEntry(objectToEntry(systemMetrics));

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            resp.setErrorString("出错");
        }
        return resp;
    }

    /**
     *
     * @param deviceUsedId    设备ID
     * @param period         要查询数据的时间跨度
     * @param metricsKeyList 要显示的指标key列表
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"admin/remoteMonitor/deviceUsed/metrics"}, method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse listDeviceUsedMetrics(@RequestParam(value = "deviceUsedId") Long deviceUsedId,
                                              @RequestParam(value = "period") Long period,
                                              @RequestParam(value = "metricsKeyList") String metricsKeyList,
                                             HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        if(metricsKeyList == null || StringUtil.isBlank(metricsKeyList)){
            return AjaxResponse.success();
        }
        try {
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                return AjaxResponse.failed(-1,"请先切换门店");
            }
            if(deviceUsedId == null || StringUtil.isBlank(deviceUsedId+"")){
                return AjaxResponse.failed(-1,"参数错误");
            }



        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            resp.setErrorString("出错");
        }
        return resp;
    }

    private Map<String, Object> objectToEntry(SystemMetrics systemMetrics) {
        Map entry = new HashMap();

        //deviceUsed
        Map deviceUsedM = new HashMap();
        deviceUsedM.put("id",systemMetrics.getDeviceUsed().getId());
        deviceUsedM.put("name",systemMetrics.getDeviceUsed().getName());
        deviceUsedM.put("boardNo",systemMetrics.getDeviceUsed().getBoardNo());
        deviceUsedM.put("merchantName",systemMetrics.getDeviceUsed().getStore().getName());

        entry.put("deviceUsed",deviceUsedM);
        entry.put("logTime",systemMetrics.getLogTime());

        //metricsInfoList
        List<Map> metricsInfoList = null;
        if(systemMetrics.getMetricsInfoList() != null ){
            metricsInfoList = new ArrayList<Map>();
            for(MetricsInfo metricsInfo : systemMetrics.getMetricsInfoList()){
                Map metricsInfoM = new HashMap();
                Map temp = new HashMap();
                temp.put("name",metricsInfo.getMetricsDetail().getName());//指标显示名称
                temp.put("unit",metricsInfo.getMetricsDetail().getUnit());//指标计量单位
                temp.put("description",metricsInfo.getMetricsDetail().getDescription());
                temp.put("value", metricsInfo.getValue());
                metricsInfoM.put(metricsInfo.getMetricsDetail().getKey(), temp);
                metricsInfoList.add(metricsInfoM);
            }
        }
        entry.put("metricsInfoList",metricsInfoList);

        //appInfoList
        List<Map> appInfoList = null;
        if(systemMetrics.getAppList() != null ){
            appInfoList = new ArrayList<Map>();

            Map services = new HashMap();
            List<Map> servicesList = new ArrayList<>();

            Map apps = new HashMap();
            List<Map> appsList = new ArrayList<>();
            for(AppInfo appInfo : systemMetrics.getAppList()){
                if(appInfo.getType() == Constants.APPINFO_TYPE_SERVICE){
                    Map serviceTemp = new HashMap();
                    serviceTemp.put("versionCode",appInfo.getVersionCode());
                    serviceTemp.put("versionName",appInfo.getVersionName());
                    servicesList.add(serviceTemp);
                }
                else if(appInfo.getType() == Constants.APPINFO_TYPE_APP){
                    Map appTemp = new HashMap();
                    appTemp.put("versionCode",appInfo.getVersionCode());
                    appTemp.put("versionName",appInfo.getVersionName());
                    appsList.add(appTemp);
                }
            }
            services.put("services",servicesList);
            apps.put("apps",appsList);
            appInfoList.add(services);
            appInfoList.add(apps);
        }
        entry.put("appInfoList", appInfoList);

        return entry;
    }



}
