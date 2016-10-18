package com.myee.tarot.remote.util;

import com.myee.djinn.dto.metrics.AppInfo;
import com.myee.djinn.dto.metrics.MetricsInfo;
import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.catalog.service.DeviceUsedService;
import com.myee.tarot.core.util.DateTimeUtils;
import com.myee.tarot.core.util.StringUtil;
import com.myee.tarot.metric.domain.MetricInfo;
import com.myee.tarot.metric.domain.MetricDetail;
import com.myee.tarot.metric.domain.SystemMetrics;
import com.myee.tarot.remote.service.AppInfoService;
import com.myee.tarot.remote.service.MetricsDetailService;
import com.myee.tarot.remote.service.MetricsInfoService;
import com.myee.tarot.remote.service.SystemMetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by xiaoni on 2016/10/13.
 */
public class MetricsUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsUtil.class);

    /**
     * 把监控指标批量写入数据库
     *
     * @param list
     * @param deviceUsedService
     * @param systemMetricsService
     * @return
     */
    public static boolean updateSystemMetrics(List<com.myee.djinn.dto.metrics.SystemMetrics> list,
                                              DeviceUsedService deviceUsedService,
                                              AppInfoService appInfoService,
                                              MetricsInfoService metricsInfoService,
                                              MetricsDetailService metricsDetailService,
                                              SystemMetricsService systemMetricsService) {
        try {
            if (list == null || list.size() == 0) {
                return false;
            }
            Date now = new Date();
            for (com.myee.djinn.dto.metrics.SystemMetrics systemMetrics : list) {
                if (systemMetrics.getBoardNo() == null || StringUtil.isBlank(systemMetrics.getBoardNo())) {
                    continue;
                }
                DeviceUsed deviceUsed = deviceUsedService.getByBoardNo(systemMetrics.getBoardNo());
                if (deviceUsed == null) {
                    continue;
                }
                SystemMetrics systemMetrics1 = new SystemMetrics();
                systemMetrics1.setDeviceUsed(deviceUsed);
                systemMetrics1.setCreated(now);

                systemMetrics1.setNode(systemMetrics.getNode());
                systemMetrics1.setLogTime(DateTimeUtils.toMillis(systemMetrics.getLogTime()));

                systemMetrics1 = systemMetricsService.update(systemMetrics1);

                systemMetrics1.setAppList(transformAppInfo(systemMetrics1, appInfoService, systemMetrics, deviceUsed, now));
                systemMetrics1.setMetricInfoList(transformMetricsInfo(systemMetrics1, metricsInfoService, metricsDetailService, systemMetrics, deviceUsed, now));


            }
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        }
    }

    /**
     * 把agent传来的MetricsInfo转化成我们web端使用的类
     *
     * @param systemMetricsDB
     * @param metricsInfoService
     * @param metricsDetailService
     *@param systemMetrics
     * @param deviceUsed
     * @param now    @return
     */
    private static List<MetricInfo> transformMetricsInfo(SystemMetrics systemMetricsDB,
                                                                                        MetricsInfoService metricsInfoService, MetricsDetailService metricsDetailService, com.myee.djinn.dto.metrics.SystemMetrics systemMetrics, DeviceUsed deviceUsed, Date now) {
        List<MetricsInfo> metricsInfoList = systemMetrics.getMetricsInfoList();
        if (metricsInfoList == null || metricsInfoList.size() == 0) {
            return null;
        }
        List<MetricInfo> result = new ArrayList<MetricInfo>();
        for (MetricsInfo metricsInfo : metricsInfoList) {
            MetricInfo metricInfo1 = new MetricInfo();
            MetricDetail metricDetail = metricsDetailService.findByKey(metricsInfo.getName());
            if(metricDetail == null ){
                continue;
            }
            metricInfo1.setMetricDetail(metricDetail);
            metricInfo1.setCreated(now);
            metricInfo1.setDeviceUsed(deviceUsed);
            metricInfo1.setDescription(metricsInfo.getDescription());
            metricInfo1.setLogTime(DateTimeUtils.toMillis(systemMetrics.getLogTime()));
            metricInfo1.setNode(metricsInfo.getNode());
            metricInfo1.setSystemMetrics(systemMetricsDB);
            metricInfo1.setValue(metricsInfo.getValue());
            metricInfo1 = metricsInfoService.update(metricInfo1);
            result.add(metricInfo1);
        }
        return result;
    }

    /**
     * 把agent传来的AppInfo转化成我们web端使用的类
     *
     * @param systemMetricsDB
     * @param appInfoService
     * @param systemMetrics
     * @param deviceUsed
     * @param now
     * @return
     */
    private static List<com.myee.tarot.metric.domain.AppInfo> transformAppInfo(SystemMetrics systemMetricsDB, AppInfoService appInfoService, com.myee.djinn.dto.metrics.SystemMetrics systemMetrics, DeviceUsed deviceUsed, Date now) {
        List<AppInfo> appLists = systemMetrics.getAppLists();
        if (appLists == null || appLists.size() == 0) {
            return null;
        }
        List<com.myee.tarot.metric.domain.AppInfo> result = new ArrayList<com.myee.tarot.metric.domain.AppInfo>();
        for (AppInfo appInfo : appLists) {
            com.myee.tarot.metric.domain.AppInfo appInfo1 = new com.myee.tarot.metric.domain.AppInfo();
            appInfo1.setCreated(now);
            appInfo1.setDeviceUsed(deviceUsed);
            appInfo1.setInstallDate(DateTimeUtils.toMillis(appInfo.getInstallDate()));
            appInfo1.setLogTime(DateTimeUtils.toMillis(systemMetrics.getLogTime()));
            appInfo1.setState(appInfo.getState());
            appInfo1.setType(appInfo.getType());
            appInfo1.setSystemMetrics(systemMetricsDB);
            appInfo1.setVersionCode(appInfo.getVersionCode());
            appInfo1.setVersionName(appInfo.getVersionName());
            appInfo1 = appInfoService.update(appInfo1);
            result.add(appInfo1);
        }
        return result;
    }

}
