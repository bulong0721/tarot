package com.myee.tarot.web.remoteMonitor.controller;

import com.alibaba.fastjson.JSON;
import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.catalog.service.DeviceUsedService;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.StringUtil;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.metric.domain.AppInfo;
import com.myee.tarot.metric.domain.MetricDetail;
import com.myee.tarot.metric.domain.MetricInfo;
import com.myee.tarot.metric.domain.SystemMetrics;
import com.myee.tarot.remote.service.MetricDetailService;
import com.myee.tarot.remote.service.SystemMetricsService;
import com.myee.tarot.remote.util.MetricsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
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
    @Autowired
    private DeviceUsedService deviceUsedService;

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
            DeviceUsed deviceUsed = deviceUsedService.findById(deviceUsedId);
            if(deviceUsed == null){
                return AjaxResponse.failed(-1,"设备不存在");
            }

            //从缓存中查询数据，如果缓存无数据，则从数据库查数据，并将结果存入缓存
            //因为redis是key-value存储，没办法实现数据库式的查询操作。
            SystemMetrics systemMetrics = systemMetricsService.getLatestByBoardNo(deviceUsed.getBoardNo());
            Map entry = commonMetricsToMap(systemMetrics,deviceUsed);
            entry.put("logTime", systemMetrics.getLogTime());
            //metricInfoList
            List<Map> metricInfoList = null;
            if(systemMetrics.getMetricInfoList() != null && systemMetrics.getMetricInfoList().size() > 0){
                metricInfoList = new ArrayList<Map>();
                List<MetricDetail> metricDetailList = metricDetailService.list();
                Map<String,MetricDetail> metricDetailMap = MetricsUtil.metricDetailListToMap(metricDetailList);
                for(MetricInfo metricInfo : systemMetrics.getMetricInfoList()){
                    Map temp = new HashMap();
                    MetricDetail metricDetailTemp = metricDetailMap.get(metricInfo.getKeyName());
                    temp.put("key", metricDetailTemp.getKeyName());//指标键名
                    temp.put("name", metricDetailTemp.getName());//指标显示名称
                    temp.put("unit", metricDetailTemp.getUnit());//指标计量单位
                    temp.put("description", metricDetailTemp.getDescription());
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

        try {
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                return AjaxResponse.failed(-1,"请先切换门店");
            }
            if(deviceUsedId == null || StringUtil.isBlank(deviceUsedId+"")){
                return AjaxResponse.failed(-1,"参数错误");
            }
            DeviceUsed deviceUsed = deviceUsedService.findById(deviceUsedId);
            if(deviceUsed == null){
                return AjaxResponse.failed(-1,"设备不存在");
            }

            SystemMetrics systemMetrics = systemMetricsService.getLatestByBoardNo(deviceUsed.getBoardNo());
            if(systemMetrics == null){
                return AjaxResponse.success();
            }
            Map entry = commonMetricsToMap(systemMetrics,deviceUsed);

            //指标概览summary里面要用的实时指标值
            entry.putAll(systemMetrics4SummaryToMap(systemMetrics));

            //metricInfoList指标详细列表,先一次性根据period和deviceUsedId去查出来，再用for循环遍历到每个指标里
            List<String> metricsKeyList = null;
            //没有要展示的指标详细列表，则直接返回全部指标
            if(metricsKeyString == null || StringUtil.isBlank(metricsKeyString)){
                metricsKeyList = metricDetailService.listKey();
            }
            else {
                metricsKeyList = JSON.parseArray(metricsKeyString, String.class);
            }

            //处理显示时间长度参数
            period = decidePeriod(period);

            List<Map> metricInfoList = null;
            //根据展示时间段、指标keyList和设备ID去查找数据，一次性取出所有数据
            List<SystemMetrics> systemMetricsList = systemMetricsService.listByBoardNoPeriodKeyList(deviceUsed.getBoardNo(), period, metricsKeyList);
            if(systemMetricsList == null || systemMetricsList.size() == 0){
                entry.put("metricInfoList",null);
                return resp;
            }
            metricInfoList = new ArrayList<Map>();
            //一次查询出所有指标详细作为缓存
            List<MetricDetail> metricDetailList = metricDetailService.list();
            Map<String,MetricDetail> metricDetailMap = MetricsUtil.metricDetailListToMap(metricDetailList);
            //使用遍历去按照指标key拆分查询出来的数据
            Map valuesByMetricsKey = sortMetricsByKey(systemMetricsList,metricsKeyList,metricDetailMap);
            //根据要展示的指标列表去选择性展示指标
            for(String keyForDisplay : metricsKeyList){
                MetricDetail metricDetail = metricDetailService.findByKey(keyForDisplay);
                if(metricDetail == null){
                    continue;
                }
                Map metricEntry = new HashMap();
                metricEntry.put("key", keyForDisplay);
                metricEntry.put("name",valueToString(metricDetail.getName()));
                metricEntry.put("drawType",valueToString(metricDetail.getDrawType()));
                metricEntry.put("maxValue",valueToString(metricDetail.getMaxValue()));
                metricEntry.put("minValue",valueToString(metricDetail.getMinValue()));
                metricEntry.put("warning",valueToString(metricDetail.getWarning()));
                metricEntry.put("alert",valueToString(metricDetail.getAlert()));
                metricEntry.put("unit",valueToString(metricDetail.getUnit()));
                metricEntry.put("description",valueToString(metricDetail.getDescription()));
                metricEntry.put("values",valuesByMetricsKey.get(keyForDisplay));

                metricInfoList.add(metricEntry);
            }

            entry.put("metricInfoList",metricInfoList);
            resp.addDataEntry(entry);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            resp.setErrorString("出错");
        }
        return resp;
    }


    private String valueToString(Object o){
        if(o == null){
            return "";
        }
        return String.valueOf(o);
    }

    /**
     * systemMetrics4Summary根据静态指标需要展示的列表转化成map
     * 返回前端时数据结构如下
     * "summaryUsed": {
             "cpuTotal":{//关联的概要指标键名
                 key:"cpuUsed"//详细指标键名
                 value:30 //详细指标值
             }
             "productLocalIP":{//如果没有关联的概要指标键名，则使用该指标的键名
                key: productLocalIP
                value:127.0.0.1
             }
        }
     * @param systemMetrics4Summary
     * @return
     */
    private Map systemMetrics4SummaryToMap(SystemMetrics systemMetrics4Summary) {
        Map entry = new HashMap();
        List<MetricInfo> metircInfoList = systemMetrics4Summary.getMetricInfoList();
        if(metircInfoList == null || metircInfoList.size() == 0){
            entry.put("summaryUsed",null);
            return entry;
        }
        Map tempResult = new HashMap();
        for(MetricInfo metricInfo : metircInfoList){
            String keyName = metricInfo.getKeyName();
            if(!Constants.SUMMARY_KEY_LIST.contains(keyName)){
                continue;
            }
            Map temp = new HashMap();
            temp.put("key",keyName);
            temp.put("value",metricInfo.getValue());
            tempResult.put(keyName,temp);
        }
        entry.put("summaryUsed",tempResult);
        return entry;
    }

    /**
     * 根据需求处理指标显示周期，最小是1小时
     * @param period
     * @return
     */
    private Long decidePeriod(Long period) {
        if(period == null || period < Constants.PERIOD_1_HOUR) {//1小时毫秒数
            period = Constants.PERIOD_1_HOUR;
        }
        return period;
    }

    /**
     * 使用遍历去按照指标key拆分查询出来的数据
     * @param systemMetricsList
     * @param metricsKeyList
     * @param metricDetailMap
     * @return
     */
    private Map sortMetricsByKey(List<SystemMetrics> systemMetricsList, List<String> metricsKeyList, Map<String, MetricDetail> metricDetailMap) {
        Map<String,ArrayList<Map>> valuesByMetricsKey = new HashMap<String,ArrayList<Map>>();
        for(String metricsKey : metricsKeyList){
            valuesByMetricsKey.put(metricsKey, new ArrayList<Map>());
        }
        for(SystemMetrics systemMetrics : systemMetricsList){
            for(MetricInfo metricInfo : systemMetrics.getMetricInfoList()){
                String keyTemp = metricInfo.getKeyName();
                //如果该参数指标不在要显示的指标里面
                if(!metricsKeyList.contains(keyTemp)){
                    continue;
                }
                ArrayList<Map> tempList = valuesByMetricsKey.get(keyTemp);
                Map tempEntry = new HashMap();
                tempEntry.put("time",metricInfo.getLogTime());
                tempEntry.put("value",metricInfo.getValue());
                tempEntry.put("state",calMetricInfoState(metricInfo,metricDetailMap));
                tempList.add(tempEntry);
                valuesByMetricsKey.put(keyTemp,tempList);
            }
        }

        return valuesByMetricsKey;
    }

    /**
     * 判断指标当前值超标状态，0正常，1警告，2报警
     * @param metricInfo
     * @param metricDetailMap
     * @return
     */
    private int calMetricInfoState(MetricInfo metricInfo, Map<String, MetricDetail> metricDetailMap) {
        MetricDetail metricDetail = metricDetailMap.get(metricInfo.getKeyName());
        if( metricDetail.getValueType() != Constants.METRIC_DETAIL_VALUE_TYPE_NUM_ALERT
                && ( StringUtil.isBlank(metricDetail.getAlertRegular()))) {
            return Constants.METRIC_STATE_OK;
        }
        try {
            BigDecimal value = BigDecimal.valueOf(Double.parseDouble(metricInfo.getValue()));
            BigDecimal warning = metricDetail.getWarning();
            BigDecimal alert = metricDetail.getAlert();
            if((value.compareTo(warning) == 1 || value.compareTo(warning) == 0) && value.compareTo(alert) == -1 ){
                return Constants.METRIC_STATE_WARN;
            }
            else if(value.compareTo(alert) == 1 || value.compareTo(alert) == 0){
                return Constants.METRIC_STATE_ALERT;
            }
        } catch (Exception e) {
            LOGGER.error("指标值不是数字："+e.getMessage());
        }

        return Constants.METRIC_STATE_OK;
    }

    /**
     * summary和详细指标返回前端的其他部分数据结构一样，只有metricInfoList不一样。所以用该函数把公共部分处理成map。
     * @param systemMetrics
     * @return
     */
    private Map<String, Object> commonMetricsToMap(SystemMetrics systemMetrics,DeviceUsed deviceUsed) {
        Map entry = new HashMap();
        //deviceUsed
        entry.put("deviceUsed",deviceUsedToMap(deviceUsed));
        //appInfoList
        if(systemMetrics == null ){
            entry.put("appInfoList","");
        }
        else {
            entry.put("appInfoList", appInfoListToMap(systemMetrics.getAppList()));
        }
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
        if(appList != null && appList.size() > 0 ){
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
