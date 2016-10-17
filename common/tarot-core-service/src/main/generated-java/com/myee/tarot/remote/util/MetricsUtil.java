package com.myee.tarot.remote.util;

import com.myee.djinn.dto.metrics.AppInfo;
import com.myee.djinn.dto.metrics.MetricsInfo;
import com.myee.djinn.dto.metrics.SystemMetrics;
import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.catalog.service.DeviceUsedService;
import com.myee.tarot.core.util.DateTimeUtils;
import com.myee.tarot.core.util.StringUtil;
import com.myee.tarot.remote.service.AppInfoService;
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
     * @param list
     * @param deviceUsedService
     * @param systemMetricsService
     * @return
     */
    public static boolean updateSystemMetrics(List<SystemMetrics> list,
                                               DeviceUsedService deviceUsedService,
                                               AppInfoService appInfoService,
                                               MetricsInfoService metricsInfoService,
                                               SystemMetricsService systemMetricsService){
        try {
            if(list == null || list.size() == 0){
                return false;
            }
            Date now = new Date();
            for(SystemMetrics systemMetrics : list){
                if(systemMetrics.getBoardNo() == null || StringUtil.isBlank(systemMetrics.getBoardNo())){
                    continue;
                }
                DeviceUsed deviceUsed = deviceUsedService.getByBoardNo(systemMetrics.getBoardNo());
                if(deviceUsed == null){
                    continue;
                }
                com.myee.tarot.metrics.domain.SystemMetrics systemMetrics1 = new com.myee.tarot.metrics.domain.SystemMetrics();
                systemMetrics1.setDeviceUsed(deviceUsed);
                systemMetrics1.setCreated(now);

                systemMetrics1.setNode(systemMetrics.getNode());
                systemMetrics1.setLogTime(DateTimeUtils.toMillis(systemMetrics.getLogTime()));

                systemMetrics1 = systemMetricsService.update(systemMetrics1);

                systemMetrics1.setAppList(transformAppInfo(systemMetrics1,appInfoService, systemMetrics, deviceUsed, now));
                systemMetrics1.setMetricsInfoList(transformMetricsInfo(systemMetrics1,metricsInfoService,systemMetrics, deviceUsed,now));



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
     *
     * @param systemMetricsDB
     * @param metricsInfoService
     * @param systemMetrics
     * @param deviceUsed
     * @param now
     * @return
     */
    private static List<com.myee.tarot.metrics.domain.MetricsInfo> transformMetricsInfo(com.myee.tarot.metrics.domain.SystemMetrics systemMetricsDB,
                                                                                        MetricsInfoService metricsInfoService, SystemMetrics systemMetrics, DeviceUsed deviceUsed, Date now) {
        List<MetricsInfo> metricsInfoList = systemMetrics.getMetricsInfoList();
        if(metricsInfoList == null || metricsInfoList.size() == 0){
            return null;
        }
        List<com.myee.tarot.metrics.domain.MetricsInfo> result = new ArrayList<com.myee.tarot.metrics.domain.MetricsInfo>();
        for(MetricsInfo metricsInfo : metricsInfoList){
            com.myee.tarot.metrics.domain.MetricsInfo metricsInfo1 = new com.myee.tarot.metrics.domain.MetricsInfo();
            metricsInfo1.setCreated(now);
            metricsInfo1.setDeviceUsed(deviceUsed);
            metricsInfo1.setDescription(metricsInfo.getDescription());
            metricsInfo1.setLogTime(DateTimeUtils.toMillis(systemMetrics.getLogTime()));
            metricsInfo1.setName(metricsInfo.getName());
            metricsInfo1.setNode(metricsInfo.getNode());
            metricsInfo1.setSystemMetrics(systemMetricsDB);
            metricsInfo1.setValue(metricsInfo.getValue());
            metricsInfo1 = metricsInfoService.update(metricsInfo1);
            result.add(metricsInfo1);
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
    private static List<com.myee.tarot.metrics.domain.AppInfo> transformAppInfo(com.myee.tarot.metrics.domain.SystemMetrics systemMetricsDB, AppInfoService appInfoService, SystemMetrics systemMetrics, DeviceUsed deviceUsed, Date now) {
        List<AppInfo> appLists = systemMetrics.getAppLists();
        if(appLists == null || appLists.size() == 0){
            return null;
        }
        List<com.myee.tarot.metrics.domain.AppInfo> result = new ArrayList<com.myee.tarot.metrics.domain.AppInfo>();
        for(AppInfo appInfo : appLists){
            com.myee.tarot.metrics.domain.AppInfo appInfo1 = new com.myee.tarot.metrics.domain.AppInfo();
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
