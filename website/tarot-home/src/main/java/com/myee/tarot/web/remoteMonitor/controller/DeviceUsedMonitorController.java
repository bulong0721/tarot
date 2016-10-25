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
import com.myee.tarot.remote.service.AppInfoService;
import com.myee.tarot.remote.service.MetricDetailService;
import com.myee.tarot.remote.service.MetricInfoService;
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
import java.util.*;

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
    @Autowired
    private AppInfoService appInfoService;
    @Autowired
    private MetricInfoService metricInfoService;

    @RequestMapping(value = {"admin/remoteMonitor/deviceUsed/summary"}, method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse getDeviceUsedSummary(@RequestParam(value = "deviceUsedId") Long deviceUsedId,
                                             HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        try {
            if (request.getSession().getAttribute(Constants.ADMIN_STORE) == null) {
                return AjaxResponse.failed(-1, "请先切换门店");
            }
            if (deviceUsedId == null || StringUtil.isBlank(deviceUsedId + "")) {
                return AjaxResponse.failed(-1, "参数错误");
            }
            DeviceUsed deviceUsed = deviceUsedService.findById(deviceUsedId);
            if (deviceUsed == null) {
                return AjaxResponse.failed(-1, "设备不存在");
            }

            //从缓存中查询数据，如果缓存无数据，则从数据库查数据，并将结果存入缓存
            //因为redis是key-value存储，没办法实现数据库式的查询操作。
            SystemMetrics systemMetrics = systemMetricsService.getLatestByBoardNo(deviceUsed.getBoardNo(), com.myee.djinn.constants.Constants.PATH_SUMMARY);
            if (systemMetrics == null) {
                return AjaxResponse.failed(-1, "无可用概览数据");
            }
            systemMetrics.setAppList(appInfoService.listBySystemMetricsId(systemMetrics.getId()));
            systemMetrics.setMetricInfoList(metricInfoService.listBySystemMetricsId(systemMetrics.getId(), null));
            Map entry = commonMetricsToMap(systemMetrics, deviceUsed, null);
            entry.put("logTime", systemMetrics.getLogTime());
            //metricInfoList
            List<Map> metricInfoList = null;
            if (systemMetrics.getMetricInfoList() != null && systemMetrics.getMetricInfoList().size() > 0) {
                metricInfoList = new ArrayList<Map>();
                List<MetricDetail> metricDetailList = metricDetailService.list();
                Map<String, MetricDetail> metricDetailMap = MetricsUtil.metricDetailListToKeyNameMap(metricDetailList);
                for (MetricInfo metricInfo : systemMetrics.getMetricInfoList()) {
                    Map temp = new HashMap();
                    MetricDetail metricDetailTemp = metricDetailMap.get(metricInfo.getKeyName());
                    if (metricDetailTemp == null) {
                        continue;
                    }
                    temp.put("key", metricDetailTemp.getKeyName());//指标键名
                    temp.put("name", valueToString(metricDetailTemp.getName()));//指标显示名称
                    temp.put("unit", valueToString(metricDetailTemp.getUnit()));//指标计量单位
                    temp.put("description", valueToString(metricDetailTemp.getDescription()));
                    temp.put("value", valueToString(metricInfo.getValue()));
                    metricInfoList.add(temp);
                }
            }
            entry.put("metricInfoList", metricInfoList);
            resp.addDataEntry(entry);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            resp.setErrorString("出错");
        }

        return resp;
    }


    /**
     * @param deviceUsedId     设备ID
     * @param period           要查询数据的时间跨度
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
                return AjaxResponse.failed(-1, "请先切换门店");
            }
            if (deviceUsedId == null || StringUtil.isBlank(deviceUsedId + "")) {
                return AjaxResponse.failed(-1, "参数错误");
            }
            DeviceUsed deviceUsed = deviceUsedService.findById(deviceUsedId);
            if (deviceUsed == null) {
                return AjaxResponse.failed(-1, "设备不存在");
            }

            //获取最新的一条动态指标
            SystemMetrics systemMetrics = systemMetricsService.getLatestByBoardNo(deviceUsed.getBoardNo(), com.myee.djinn.constants.Constants.PATH_METRICS);
            if (systemMetrics == null) {
                return AjaxResponse.failed(-1, "无可用指标数据");
            }
            systemMetrics.setAppList(appInfoService.listBySystemMetricsId(systemMetrics.getId()));

            //获取最新的一条静态指标，为了取出已安装的应用列表
            SystemMetrics systemMetricsSummary = systemMetricsService.getLatestByBoardNo(deviceUsed.getBoardNo(), com.myee.djinn.constants.Constants.PATH_SUMMARY);
            List<AppInfo> installedAppInfoList = null;
            if (systemMetricsSummary.getId() != null) {
                installedAppInfoList = appInfoService.listBySystemMetricsId(systemMetricsSummary.getId());
            }
            //把已安装的应用列表缓存到Map中
            Map<String, AppInfo> installedAppMap = MetricsUtil.appInfoListToMap(installedAppInfoList, Constants.APPINFO_TYPE_APP);

            //先把deviceUsed，正在运行的服务、进程写入map
            Map entry = commonMetricsToMap(systemMetrics, deviceUsed, installedAppMap);

            //指标概览summary里面要用的实时指标值
            List<MetricInfo> metricInfoList4Summary = metricInfoService.listBySystemMetricsId(systemMetricsSummary.getId(),Constants.METRICS_4_SUMMARY_KEY_LIST);
            entry.putAll(systemMetrics4SummaryToMap(metricInfoList4Summary));

            //metricInfoList指标详细列表,先一次性根据period和deviceUsedId去查出来，再用for循环遍历到每个指标里
            //没有要展示的指标详细列表，则直接返回全部动态指标
            List<String> metricsKeyList = null;
            if (metricsKeyString == null || StringUtil.isBlank(metricsKeyString)) {
                metricsKeyList = new ArrayList<>();
                metricsKeyList.addAll(Constants.METRICS_NEED_TIME_KEY_LIST);
                metricsKeyList.addAll(Constants.METRICS_NO_TIME_KEY_LIST);
            } else {
                metricsKeyList = JSON.parseArray(metricsKeyString, String.class);
            }

            //处理显示时间长度参数
            period = decidePeriod(period);

            List<Map> metricInfoList = null;
            //根据展示时间段、指标keyList和设备ID去查找数据，一次性取出所有需要展示值随时间变化的数据
            Long now = System.currentTimeMillis();
            List<MetricInfo> metricInfoListDB = metricInfoService.listByBoardNoPeriod(deviceUsed.getBoardNo(), now, period, com.myee.djinn.constants.Constants.PATH_METRICS_METRICSINFO, Constants.METRICS_NEED_TIME_KEY_LIST);
            List<MetricInfo> metricInfoNoTimeListDB = metricInfoService.listBySystemMetricsId(systemMetrics.getId(),Constants.METRICS_NO_TIME_KEY_LIST);
            if(metricInfoNoTimeListDB != null && metricInfoNoTimeListDB.size() > 0){
                metricInfoListDB.addAll(metricInfoNoTimeListDB);
            }
            if (metricInfoListDB == null || metricInfoListDB.size() == 0) {
                entry.put("metricInfoList", "");
                return resp;
            }

            metricInfoList = new ArrayList<Map>();
            //一次查询出所有指标详细作为缓存
            List<MetricDetail> metricDetailList = metricDetailService.list();
            Map<String, MetricDetail> metricDetailMap = MetricsUtil.metricDetailListToKeyNameMap(metricDetailList);
            //使用遍历去按照指标key拆分查询出来的数据
            Map valuesByMetricsKey = sortMetricsByKey(metricInfoListDB, metricsKeyList, metricDetailMap);
            //根据要展示的指标列表去选择性展示指标

            for (String keyForDisplay : metricsKeyList) {
                MetricDetail metricDetail = metricDetailMap.get(keyForDisplay);
                if (metricDetail == null
                        || !(Constants.METRICS_NEED_TIME_KEY_LIST.contains(keyForDisplay) || Constants.METRICS_NO_TIME_KEY_LIST.contains(keyForDisplay))) {
                    continue;
                }
                Map metricEntry = new HashMap();
                //展示随时间变化的指标
//                if(Constants.METRICS_NEED_TIME_KEY_LIST.contains(keyForDisplay)) {
                    metricEntry.put("key", keyForDisplay);
                    metricEntry.put("name", valueToString(metricDetail.getName()));
                    metricEntry.put("drawType", valueToString(metricDetail.getDrawType()));
                    metricEntry.put("maxValue", valueToString(metricDetail.getMaxValue()));
                    metricEntry.put("minValue", valueToString(metricDetail.getMinValue()));
                    metricEntry.put("warning", valueToString(metricDetail.getWarning()));
                    metricEntry.put("alert", valueToString(metricDetail.getAlert()));
                    metricEntry.put("unit", valueToString(metricDetail.getUnit()));
                    metricEntry.put("description", valueToString(metricDetail.getDescription()));
                    metricEntry.put("values", valuesByMetricsKey.get(keyForDisplay));

//                }
//                //展示只需要显示最新值的指标
//                else if(Constants.METRICS_NO_TIME_KEY_LIST.contains(keyForDisplay)){
//                    metricEntry.put("key", keyForDisplay);
//                    metricEntry.put("name", valueToString(metricDetail.getName()));
//                    metricEntry.put("drawType", valueToString(metricDetail.getDrawType()));
//                    metricEntry.put("maxValue", valueToString(metricDetail.getMaxValue()));
//                    metricEntry.put("minValue", valueToString(metricDetail.getMinValue()));
//                    metricEntry.put("warning", valueToString(metricDetail.getWarning()));
//                    metricEntry.put("alert", valueToString(metricDetail.getAlert()));
//                    metricEntry.put("unit", valueToString(metricDetail.getUnit()));
//                    metricEntry.put("description", valueToString(metricDetail.getDescription()));
//                    metricEntry.put("values", valuesByMetricsKey.get(keyForDisplay));
//                }
                metricInfoList.add(metricEntry);


            }

            entry.put("metricInfoList", metricInfoList);
            resp.addDataEntry(entry);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            resp.setErrorString("出错");
        }
        return resp;
    }


    private String valueToString(Object o) {
        if (o == null) {
            return "";
        }
        return String.valueOf(o);
    }

    /**
     * systemMetrics4Summary根据静态指标需要展示的列表转化成map
     * 返回前端时数据结构如下
     * "summaryUsed": {
     * "cpuTotal":{//关联的概要指标键名
     * key:"cpuUsed"//详细指标键名
     * value:30 //详细指标值
     * }
     * "productLocalIP":{//如果没有关联的概要指标键名，则使用该指标的键名
     * key: productLocalIP
     * value:127.0.0.1
     * }
     * }
     *
     * @param systemMetrics4Summary
     * @return
     */
    private Map systemMetrics4SummaryToMap(List<MetricInfo> metircInfoList) {
        Map entry = new HashMap();
        if (metircInfoList == null || metircInfoList.size() == 0) {
            entry.put("summaryUsed", "");
            return entry;
        }
        Map tempResult = new HashMap();
        for (MetricInfo metricInfo : metircInfoList) {
            String keyName = metricInfo.getKeyName();
            if (!Constants.METRICS_4_SUMMARY_KEY_LIST.contains(keyName)) {
                continue;
            }
            Map temp = new HashMap();
            temp.put("key", keyName);
            temp.put("value", valueToString(metricInfo.getValue()));
            tempResult.put(keyName, temp);
        }
        entry.put("summaryUsed", tempResult);
        return entry;
    }

    /**
     * 根据需求处理指标显示周期，最小是1小时
     *
     * @param period
     * @return
     */
    private Long decidePeriod(Long period) {
        if (period == null || period < Constants.PERIOD_1_HOUR) {//1小时毫秒数
            period = Constants.PERIOD_1_HOUR;
        }
        return period;
    }

    /**
     * 使用遍历去按照指标key拆分查询出来的数据
     *
     * @param metricInfoListDB
     * @param metricsKeyList
     * @param metricDetailMap
     * @return
     */
    private Map sortMetricsByKey(List<MetricInfo> metricInfoListDB, List<String> metricsKeyList, Map<String, MetricDetail> metricDetailMap) {
        Map<String, ArrayList<Map>> valuesByMetricsKey = new HashMap<String, ArrayList<Map>>();
        for (String metricsKey : metricsKeyList) {
            valuesByMetricsKey.put(metricsKey, new ArrayList<Map>());
        }
//        for (SystemMetrics systemMetrics : systemMetricsList) {
        for (MetricInfo metricInfo : metricInfoListDB) {
            String keyTemp = metricInfo.getKeyName();
            //如果该参数指标不在要显示的指标里面
            if (!metricsKeyList.contains(keyTemp)) {
                continue;
            }
            ArrayList<Map> tempList = valuesByMetricsKey.get(keyTemp);
            Map tempEntry = new HashMap();
            tempEntry.put("time", metricInfo.getLogTime());
            tempEntry.put("value", valueToString(metricInfo.getValue()));
            tempEntry.put("state", valueToString(calMetricInfoState(metricInfo, metricDetailMap)));
            tempList.add(tempEntry);
            valuesByMetricsKey.put(keyTemp, tempList);
        }
//        }

        return valuesByMetricsKey;
    }

    /**
     * 判断指标当前值超标状态，0正常，1警告，2报警
     *
     * @param metricInfo
     * @param metricDetailMap
     * @return
     */
    private int calMetricInfoState(MetricInfo metricInfo, Map<String, MetricDetail> metricDetailMap) {
        MetricDetail metricDetail = metricDetailMap.get(metricInfo.getKeyName());
        if (metricDetail.getValueType() != Constants.METRIC_DETAIL_VALUE_TYPE_NUM_ALERT
                && (StringUtil.isBlank(metricDetail.getAlertRegular()))) {
            return Constants.METRIC_STATE_OK;
        }
        try {
            BigDecimal value = BigDecimal.valueOf(Double.parseDouble(metricInfo.getValue()));
            BigDecimal warning = metricDetail.getWarning();
            BigDecimal alert = metricDetail.getAlert();
            if ((value.compareTo(warning) == 1 || value.compareTo(warning) == 0) && value.compareTo(alert) == -1) {
                return Constants.METRIC_STATE_WARN;
            } else if (value.compareTo(alert) == 1 || value.compareTo(alert) == 0) {
                return Constants.METRIC_STATE_ALERT;
            }
        } catch (Exception e) {
            LOGGER.error("指标值不是数字：" + e.getMessage());
        }

        return Constants.METRIC_STATE_OK;
    }

    /**
     * summary和详细指标返回前端的其他部分数据结构一样，只有metricInfoList不一样。所以用该函数把公共部分处理成map。
     * <p/>
     * appInfoList 在summary中表示已安装的应用列表，在metrics中表示正在运行的服务/进程列表
     *
     * @param systemMetrics
     * @param installedApp
     * @return
     */
    private Map<String, Object> commonMetricsToMap(SystemMetrics systemMetrics, DeviceUsed deviceUsed, Map<String, AppInfo> installedApp) {
        Map entry = new HashMap();
        //deviceUsed
        entry.put("deviceUsed", deviceUsedToMap(deviceUsed));
        //appInfoList
        if (systemMetrics == null || systemMetrics.getAppList() == null) {
            entry.put("appInfoList", "");
        } else {
            entry.put("appInfoList", appInfoListToMap(systemMetrics.getAppList(), installedApp));
        }
        return entry;
    }

    /**
     * 把deviceUsed转换成返回前端的Map
     *
     * @param deviceUsed
     * @return
     */
    private Map deviceUsedToMap(DeviceUsed deviceUsed) {
        Map deviceUsedM = new HashMap();
        deviceUsedM.put("id", valueToString(deviceUsed.getId()));
        deviceUsedM.put("name", valueToString(deviceUsed.getName()));
        deviceUsedM.put("boardNo", valueToString(deviceUsed.getBoardNo()));
        deviceUsedM.put("merchantName", valueToString(deviceUsed.getStore().getName()));
        return deviceUsedM;
    }

    /**
     * 把AppInfoList转化成返回前端的Map格式
     *
     * @param appList
     * @param installedApp
     * @return
     */
    private List<Map> appInfoListToMap(List<AppInfo> appList, Map<String, AppInfo> installedApp) {
        List<Map> appInfoList = Collections.EMPTY_LIST;
        if (appList != null && appList.size() > 0) {
            appInfoList = new ArrayList<Map>();
            Map result = new HashMap();
            List<Map> servicesList = new ArrayList<>();

            List<Map> appsList = new ArrayList<>();

            List<Map> processesList = new ArrayList<>();
            for (AppInfo appInfo : appList) {
                //服务和进程的应用名称为空，只能通过包名去已安装的应用列表中查询对应的应用名称
                String appName = getAppName4Service(installedApp, appInfo);

                if (appInfo.getType() == Constants.APPINFO_TYPE_SERVICE) {
                    Map serviceTemp = new HashMap();
                    serviceTemp.put("versionCode", valueToString(appInfo.getVersionCode()));
                    serviceTemp.put("versionName", valueToString(appInfo.getVersionName()));
                    serviceTemp.put("packageName", valueToString(appInfo.getPackageName()));
                    serviceTemp.put("appName", valueToString(appName));
                    servicesList.add(serviceTemp);
                } else if (appInfo.getType() == Constants.APPINFO_TYPE_APP) {
                    Map appTemp = new HashMap();
                    appTemp.put("versionCode", valueToString(appInfo.getVersionCode()));
                    appTemp.put("versionName", valueToString(appInfo.getVersionName()));
                    appTemp.put("appName", valueToString(appName));
                    appTemp.put("packageName", valueToString(appInfo.getPackageName()));
                    appTemp.put("installDate", valueToString(appInfo.getInstallDate()));
                    appTemp.put("lastUpdateDate", valueToString(appInfo.getLastUpdateTime()));
                    appsList.add(appTemp);
                } else if (appInfo.getType() == Constants.APPINFO_TYPE_PROCESS) {
                    Map processTemp = new HashMap();
                    processTemp.put("versionCode", valueToString(appInfo.getVersionCode()));
                    processTemp.put("versionName", valueToString(appInfo.getVersionName()));
                    processTemp.put("packageName", valueToString(appInfo.getPackageName()));
                    processTemp.put("appName", valueToString(appName));
                    processTemp.put("processName", valueToString(appInfo.getProcessName()));
                    processTemp.put("pid", valueToString(appInfo.getPid()));
                    processesList.add(processTemp);
                }
            }
            result.put("services", servicesList);
            result.put("apps", appsList);
            result.put("processes", processesList);
            appInfoList.add(result);
        }
        return appInfoList;
    }

    /**
     * 服务和进程的应用名称为空，只能通过包名去已安装的应用列表中查询对应的应用名称
     *
     * @param installedApp
     * @param appInfo
     * @return
     */
    private String getAppName4Service(Map<String, AppInfo> installedApp, AppInfo appInfo) {
        String appName = appInfo.getAppName();
        if(installedApp == null || installedApp.size() == 0){
            return appName;
        }
        if (appName == null || StringUtil.isBlank(appName)) {
            AppInfo appInfo1 = installedApp.get(appInfo.getPackageName());
            if (appInfo1 != null) {
                appName = appInfo1.getAppName();
            } else {
                appName = "";
            }
        }
        return appName;
    }

}
