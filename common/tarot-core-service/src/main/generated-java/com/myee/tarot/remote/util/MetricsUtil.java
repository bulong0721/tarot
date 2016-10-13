package com.myee.tarot.remote.util;

import com.myee.djinn.dto.gather.AppInfo;
import com.myee.djinn.dto.gather.SystemMetrics;
import com.myee.djinn.dto.gather.SystemSummary;
import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.catalog.service.DeviceUsedService;
import com.myee.tarot.core.util.DateTimeUtils;
import com.myee.tarot.core.util.StringUtil;
import com.myee.tarot.remote.service.SystemMetricsService;
import com.myee.tarot.remote.service.SystemSummaryService;
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
                systemMetrics1.setBluetoothState(systemMetrics.getBluetoothState());
                systemMetrics1.setCharging(systemMetrics.getCharging());
                systemMetrics1.setComment(systemMetrics.getComment());
                systemMetrics1.setCpuUsed(systemMetrics.getCpuUsed());
                systemMetrics1.setLogTime(DateTimeUtils.toMillis(systemMetrics.getLogTime()));
                systemMetrics1.setProductGlobalIP(systemMetrics.getProductGlobalIP());
                systemMetrics1.setProductLocalIP(systemMetrics.getProductLocalIP());
                systemMetrics1.setProductPower(systemMetrics.getProductPower());
                systemMetrics1.setRamUsed(systemMetrics.getRamUsed());
                systemMetrics1.setRomUsed(systemMetrics.getRomUsed());
                systemMetrics1.setSSID(systemMetrics.getSSID());
                systemMetrics1.setVolume(systemMetrics.getVolume());
                systemMetrics1.setWifiStatus(systemMetrics.getWifiStatus());
                systemMetrics1.setAppLists( transformAppInfo(systemMetrics.getAppLists(),deviceUsed) );

                systemMetrics1.setDeviceUsed(deviceUsed);
                systemMetrics1.setCreated(now);
                systemMetricsService.update(systemMetrics1);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        }
    }

    /**
     * 把agent传来的AppInfo转化成我们web端使用的类
     * @param appLists
     * @return
     */
    private static List<com.myee.tarot.metrics.domain.AppInfo> transformAppInfo(List<AppInfo> appLists,DeviceUsed deviceUsed) {
        if(appLists == null || appLists.size() == 0){
            return null;
        }
        List<com.myee.tarot.metrics.domain.AppInfo> result = new ArrayList<com.myee.tarot.metrics.domain.AppInfo>();
        for(AppInfo appInfo : appLists){
            com.myee.tarot.metrics.domain.AppInfo appInfo1 = new com.myee.tarot.metrics.domain.AppInfo();
            appInfo1.setDeviceUsed(deviceUsed);
            appInfo1.setInstallDate(DateTimeUtils.toMillis(appInfo.getInstallDate()));
            appInfo1.setState(appInfo.getState());
            appInfo1.setType(appInfo.getType());
            appInfo1.setVersionCode(appInfo.getVersionCode());
            appInfo1.setVersionName(appInfo.getVersionName());
            result.add(appInfo1);
        }
        return result;
    }

    /**
     * 把监控概要批量写入数据库
     * @param list
     * @param deviceUsedService
     * @param systemSummaryService
     * @return
     */
    public static boolean updateSystemSummary(List<SystemSummary> list,
                                              DeviceUsedService deviceUsedService,
                                              SystemSummaryService systemSummaryService){
        try {
            if(list == null || list.size() == 0){
                return false;
            }
            Date now = new Date();
            for(SystemSummary systemSummary : list){
                if(systemSummary.getBoardNo() == null || StringUtil.isBlank(systemSummary.getBoardNo())){
                    continue;
                }
                DeviceUsed deviceUsed = deviceUsedService.getByBoardNo(systemSummary.getBoardNo());
                if(deviceUsed == null){
                    continue;
                }
                com.myee.tarot.metrics.domain.SystemSummary systemSummary1 = new com.myee.tarot.metrics.domain.SystemSummary();
                systemSummary1.setBrand(systemSummary.getBrand());
                systemSummary1.setComputerType(systemSummary.getComputerType());
                systemSummary1.setCpuName(systemSummary.getCpuName());
                systemSummary1.setDeviceMode(systemSummary.getDeviceMode());
                systemSummary1.setLanguage(systemSummary.getLanguage());
                systemSummary1.setMacAddress(systemSummary.getMacAddress());
                systemSummary1.setMaxFrequency(systemSummary.getMaxFrequency());
                systemSummary1.setMinFrequency(systemSummary.getMinFrequency());
                systemSummary1.setOsKernelVersion(systemSummary.getOsKernelVersion());
                systemSummary1.setOsUUID(systemSummary.getOsKernelVersion());
                systemSummary1.setOsVersion(systemSummary.getOsVersion());
                systemSummary1.setRamTotal(systemSummary.getRamTotal());
                systemSummary1.setResolution(systemSummary.getResolution());
                systemSummary1.setRomTotal(systemSummary.getRomTotal());
                systemSummary1.setSerial(systemSummary.getSerial());
                systemSummary1.setAppLists(transformAppInfo(systemSummary.getAppLists(), deviceUsed));

                systemSummary1.setDeviceUsed(deviceUsed);
                systemSummary1.setCreated(now);
                systemSummaryService.update(systemSummary1);
            }
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        }
    }
}
