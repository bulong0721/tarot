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
                    temp.put("key", metricInfo.getMetricDetail().getKeyName());//指标键名
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

            SystemMetrics systemMetrics = systemMetricsService.getLatestByDUId(deviceUsedId);
            if(systemMetrics == null){
                return AjaxResponse.success();
            }
            Map entry = commonMetricsToMap(systemMetrics);

            //指标概览summary里面要用的实时指标值
            List<SystemMetrics> systemSummaryList = systemMetricsService.listByDUIdPeriodKeyList(deviceUsedId, period, Constants.SUMMARY_KEY_LIST);

            //metricInfoList指标详细列表,先一次性根据period和deviceUsedId去查出来，再用for循环遍历到每个指标里
            List<String> metricsKeyList = null;
            //没有要展示的指标详细列表，则直接返回全部指标
            if(metricsKeyString == null || StringUtil.isBlank(metricsKeyString)){
                metricsKeyList = metricDetailService.listKey();
            }
            else {
                metricsKeyList = JSON.parseArray(metricsKeyString, String.class);
            }

            period = decidePeriod(period);

            List<Map> metricInfoList = null;
            //根据展示时间段、指标keyList和设备ID去查找数据，一次性取出所有数据
            List<SystemMetrics> systemMetricsList = systemMetricsService.listByDUIdPeriodKeyList(deviceUsedId, period, metricsKeyList);
            if(systemMetricsList == null || systemMetricsList.size() == 0){
                entry.put("metricInfoList",null);
                return resp;
            }
            //使用遍历去按照指标key拆分查询出来的数据
            Map valuesByMetricsKey = sortMetricsByKey(systemMetricsList,metricsKeyList);
            //根据要展示的指标列表去选择性展示指标
            for(String keyForDisplay : metricsKeyList){
                MetricDetail metricDetail = metricDetailService.findByKey(keyForDisplay);
                if(metricDetail == null){
                    continue;
                }
                Map metricEntry = null;
                metricEntry = new HashMap();
                metricEntry.put("key", keyForDisplay);
                metricEntry.put("name",metricDetail.getName());
                metricEntry.put("drawType",metricDetail.getDrawType());
                metricEntry.put("maxValue",metricDetail.getMaxValue());
                metricEntry.put("minValue",metricDetail.getMinValue());
                metricEntry.put("warning",metricDetail.getWarning());
                metricEntry.put("alert",metricDetail.getAlert());
                metricEntry.put("unit",metricDetail.getUnit());
                metricEntry.put("description",metricDetail.getDescription());
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

    /**
     * 根据需求处理指标显示周期，最小是1小时
     * @param period
     * @return
     */
    private Long decidePeriod(Long period) {
        if(period == null || period < 3600000L) {//1小时毫秒数
            period = 3600000L;
        }
        return period;
    }

    /**
     * 使用遍历去按照指标key拆分查询出来的数据
     * @param systemMetricsList
     * @param metricsKeyList
     * @return
     */
    private Map sortMetricsByKey(List<SystemMetrics> systemMetricsList, List<String> metricsKeyList) {
        Map<String,ArrayList<Map>> valuesByMetricsKey = new HashMap<String,ArrayList<Map>>();
        for(String metricsKey : metricsKeyList){
            valuesByMetricsKey.put(metricsKey, new ArrayList<Map>());
        }
        for(SystemMetrics systemMetrics : systemMetricsList){
            for(MetricInfo metricInfo : systemMetrics.getMetricInfoList()){
                String keyTemp = metricInfo.getMetricDetail().getKeyName();
                //如果该参数指标不在要显示的指标里面
                if(!metricsKeyList.contains(keyTemp)){
                    continue;
                }
                ArrayList<Map> tempList = valuesByMetricsKey.get(keyTemp);
                Map tempEntry = new HashMap();
                tempEntry.put("time",metricInfo.getLogTime());
                tempEntry.put("value",metricInfo.getValue());
                tempEntry.put("state",calMetricInfoState(metricInfo));
                tempList.add(tempEntry);
                valuesByMetricsKey.put(keyTemp,tempList);
            }
        }

        return valuesByMetricsKey;
    }

    /**
     * 判断指标当前值超标状态，0正常，1警告，2报警
     * @param metricInfo
     * @return
     */
    private int calMetricInfoState(MetricInfo metricInfo) {
        MetricDetail metricDetail = metricInfo.getMetricDetail();
        if( metricDetail.getValueType() != Constants.METRIC_DETAIL_VALUE_TYPE_NUM ){
            return Constants.METRIC_STATE_OK;
        }
        try {
            double value = Double.parseDouble(metricInfo.getValue());
            double warning = metricDetail.getWarning();
            double alert = metricDetail.getAlert();
            if(value >= warning && value < alert){
                return Constants.METRIC_STATE_WARN;
            }
            else if(value >= alert){
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
