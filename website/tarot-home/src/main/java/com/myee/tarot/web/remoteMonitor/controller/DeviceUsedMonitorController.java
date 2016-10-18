package com.myee.tarot.web.remoteMonitor.controller;

import com.alibaba.fastjson.JSON;
import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.StringUtil;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.metric.domain.AppInfo;
import com.myee.tarot.metric.domain.MetricDetail;
import com.myee.tarot.metric.domain.MetricInfo;
import com.myee.tarot.metric.domain.SystemMetrics;
import com.myee.tarot.remote.service.MetricDetailService;
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
    @Autowired
    private MetricDetailService metricDetailService;

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
            Map entry = commonMetricsToMap(systemMetrics);
            entry.put("logTime", systemMetrics.getLogTime());
            //metricInfoList
            List<Map> metricInfoList = null;
            if(systemMetrics.getMetricInfoList() != null ){
                metricInfoList = new ArrayList<Map>();
                for(MetricInfo metricInfo : systemMetrics.getMetricInfoList()){
                    Map temp = new HashMap();
                    temp.put("key", metricInfo.getMetricDetail().getKey());//指标键名
                    temp.put("name", metricInfo.getMetricDetail().getName());//指标显示名称
                    temp.put("unit", metricInfo.getMetricDetail().getUnit());//指标计量单位
                    temp.put("description", metricInfo.getMetricDetail().getDescription());
                    temp.put("value", metricInfo.getValue());
                    metricInfoList.add(temp);
                }
            }
            entry.put("metricInfoList",metricInfoList);
            resp.addDataEntry(entry);

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
     * @param metricsKeyString 要显示的指标key列表
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"admin/remoteMonitor/deviceUsed/metrics"}, method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse listDeviceUsedMetrics(@RequestParam(value = "deviceUsedId") Long deviceUsedId,
                                              @RequestParam(value = "period") Long period,
                                              @RequestParam(value = "metricsKeyString") String metricsKeyString,
                                             HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        //没有要展示的指标详细列表，则直接返回空
        if(metricsKeyString == null || StringUtil.isBlank(metricsKeyString)){
            return AjaxResponse.success();
        }
        try {
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                return AjaxResponse.failed(-1,"请先切换门店");
            }
            if(deviceUsedId == null || StringUtil.isBlank(deviceUsedId+"")){
                return AjaxResponse.failed(-1,"参数错误");
            }

            SystemMetrics systemMetrics = systemMetricsService.getLatestByDUId(deviceUsedId);
            Map entry = commonMetricsToMap(systemMetrics);

            //指标概览summary里面要用的实时指标值

            //metricInfoList指标详细列表
            List<Map> metricInfoList = null;
            List<String> metricsKeyList = JSON.parseArray(metricsKeyString, String.class);

            MetricDetail metricDetail = metricDetailService.findByKey(com.myee.djinn.constants.Constants.METRIC_bluetoothState);
            Map metricEntry = new HashMap();
            metricEntry.put("key",metricDetail.getKey());
            metricEntry.put("name",metricDetail.getName());
            metricEntry.put("drawType",metricDetail.getDrawType());
            metricEntry.put("maxValue",metricDetail.getMaxValue());
            metricEntry.put("minValue",metricDetail.getMinValue());
            metricEntry.put("warning",metricDetail.getWarning());
            metricEntry.put("alert",metricDetail.getAlert());
            metricEntry.put("unit",metricDetail.getUnit());
            metricEntry.put("description",metricDetail.getDescription());


            entry.put("metricInfoList",metricInfoList);
            resp.addDataEntry(entry);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            resp.setErrorString("出错");
        }
        return resp;
    }

    /**
     * summary和详细指标返回前端的其他部分数据结构一样，只有metricInfoList不一样。所以用该函数把公共部分处理成map。
     * @param systemMetrics
     * @return
     */
    private Map<String, Object> commonMetricsToMap(SystemMetrics systemMetrics) {
        Map entry = new HashMap();
        //deviceUsed
        entry.put("deviceUsed",deviceUsedToMap(systemMetrics.getDeviceUsed()));
        //appInfoList
        entry.put("appInfoList", appInfoListToMap(systemMetrics.getAppList()));
        return entry;
    }

    /**
     * 把deviceUsed转换成返回前端的Map
     * @param deviceUsed
     * @return
     */
    private Map deviceUsedToMap(DeviceUsed deviceUsed){
        Map deviceUsedM = new HashMap();
        deviceUsedM.put("id", deviceUsed.getId());
        deviceUsedM.put("name", deviceUsed.getName());
        deviceUsedM.put("boardNo", deviceUsed.getBoardNo());
        deviceUsedM.put("merchantName", deviceUsed.getStore().getName());
        return deviceUsedM;
    }

    /**
     * 把AppInfoList转化成返回前端的Map格式
     * @param appList
     * @return
     */
    private List<Map> appInfoListToMap(List<AppInfo> appList){
        List<Map> appInfoList = null;
        if(appList != null ){
            appInfoList = new ArrayList<Map>();
            Map services = new HashMap();
            List<Map> servicesList = new ArrayList<>();

            Map apps = new HashMap();
            List<Map> appsList = new ArrayList<>();
            for(AppInfo appInfo : appList){
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
        return appInfoList;
    }

}
