package com.myee.tarot.remote.util;

import com.myee.djinn.dto.metrics.AppInfo;
import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.catalog.service.DeviceUsedService;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.DateTimeUtils;
import com.myee.tarot.core.util.StringUtil;
import com.myee.tarot.metric.domain.MetricInfo;
import com.myee.tarot.metric.domain.MetricDetail;
import com.myee.tarot.metric.domain.SystemMetrics;
import com.myee.tarot.remote.service.AppInfoService;
import com.myee.tarot.remote.service.MetricDetailService;
import com.myee.tarot.remote.service.MetricInfoService;
import com.myee.tarot.remote.service.SystemMetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by chay on 2016/10/13.
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
                                              MetricInfoService metricInfoService,
                                              MetricDetailService metricDetailService,
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
                //如果库中已有该条数据，则跳过，不重复插入
                SystemMetrics systemMetricsOLD = systemMetricsService.getByBoardNoLogTimeNod(systemMetrics.getBoardNo(), systemMetrics.getLogTime(), systemMetrics.getNode());
                if (systemMetricsOLD != null) {
                    continue;
                }
                SystemMetrics systemMetricsDB = new SystemMetrics();
//                systemMetricsDB.setDeviceUsed(deviceUsed);
                systemMetricsDB.setBoardNo(systemMetrics.getBoardNo());
                systemMetricsDB.setCreated(now);

                systemMetricsDB.setNode(systemMetrics.getNode());
                systemMetricsDB.setLogTime(DateTimeUtils.toMillis(systemMetrics.getLogTime()));

                systemMetricsDB = systemMetricsService.update(systemMetricsDB);

                systemMetricsDB.setAppList(transformAppInfo(systemMetricsDB, appInfoService, systemMetrics, deviceUsed, now));
                systemMetricsDB.setMetricInfoList(transformMetricsInfo(systemMetricsDB, metricInfoService, metricDetailService, systemMetrics, deviceUsed, now));


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
     * @param metricInfoService
     * @param metricDetailService
     * @param systemMetrics
     * @param deviceUsed
     * @param now                 @return
     */
    private static List<MetricInfo> transformMetricsInfo(SystemMetrics systemMetricsDB,
                                                         MetricInfoService metricInfoService,
                                                         MetricDetailService metricDetailService,
                                                         com.myee.djinn.dto.metrics.SystemMetrics systemMetrics,
                                                         DeviceUsed deviceUsed,
                                                         Date now) {
        List<com.myee.djinn.dto.metrics.MetricInfo> metricInfoList = systemMetrics.getMetricInfoList();
        if (metricInfoList == null || metricInfoList.size() == 0) {
            return null;
        }
        List<MetricInfo> result = new ArrayList<MetricInfo>();
        //一次查询出所有指标详细作为缓存
        List<MetricDetail> metricDetailList = metricDetailService.list();
        Map<String, MetricDetail> metricDetailMap = metricDetailListToKeyNameMap(metricDetailList);
        String boardNo = deviceUsed.getBoardNo();
        Long systemMetricsId = systemMetricsDB.getId();
        for (com.myee.djinn.dto.metrics.MetricInfo metricInfo : metricInfoList) {
            MetricInfo metricInfoDB = new MetricInfo();
            MetricDetail metricDetail = metricDetailMap.get(metricInfo.getName());
            if (metricDetail == null) {
                continue;
            }
            metricInfoDB.setKeyName(metricInfo.getName());
            metricInfoDB.setCreated(now);
            metricInfoDB.setBoardNo(boardNo);
            metricInfoDB.setDescription(metricInfo.getDescription());
            metricInfoDB.setLogTime(DateTimeUtils.toMillis(systemMetrics.getLogTime()));
            metricInfoDB.setNode(metricInfo.getNode());
            metricInfoDB.setSystemMetricsId(systemMetricsId);
            metricInfoDB.setValue(metricInfo.getValue());
            metricInfoDB = metricInfoService.update(metricInfoDB);
            result.add(metricInfoDB);
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
    private static List<com.myee.tarot.metric.domain.AppInfo> transformAppInfo(SystemMetrics systemMetricsDB,
                                                                               AppInfoService appInfoService,
                                                                               com.myee.djinn.dto.metrics.SystemMetrics systemMetrics,
                                                                               DeviceUsed deviceUsed,
                                                                               Date now) {
        List<AppInfo> appLists = systemMetrics.getAppLists();
        if (appLists == null || appLists.size() == 0) {
            return null;
        }
        List<com.myee.tarot.metric.domain.AppInfo> result = new ArrayList<com.myee.tarot.metric.domain.AppInfo>();
        String boardNo = deviceUsed.getBoardNo();
        Long systemMetricsId = systemMetricsDB.getId();
        for (AppInfo appInfo : appLists) {
            com.myee.tarot.metric.domain.AppInfo appInfoDB = new com.myee.tarot.metric.domain.AppInfo();
            appInfoDB.setAppName(appInfo.getAppName());
            appInfoDB.setNode(appInfo.getNode());
            appInfoDB.setCreated(now);
            appInfoDB.setBoardNo(boardNo);
            appInfoDB.setInstallDate(DateTimeUtils.toMillis(appInfo.getInstallDate()));
            appInfoDB.setLastUpdateTime(DateTimeUtils.toMillis(appInfo.getLastUpdateTime()));
            appInfoDB.setLogTime(DateTimeUtils.toMillis(systemMetrics.getLogTime()));
            appInfoDB.setPackageName(appInfo.getPackageName());
            appInfoDB.setState(appInfo.getState());
            appInfoDB.setType(appInfo.getType());
            appInfoDB.setSystemMetricsId(systemMetricsId);
            appInfoDB.setVersionCode(appInfo.getVersionCode());
            appInfoDB.setVersionName(appInfo.getVersionName());
            appInfoDB = appInfoService.update(appInfoDB);
            result.add(appInfoDB);
        }
        return result;
    }


    /**
     * 把metricDetail查询出来的list转为map，以便快速使用。键名是指标KeyName
     *
     * @param metricDetailList
     * @return
     */
    public static Map<String, MetricDetail> metricDetailListToKeyNameMap(List<MetricDetail> metricDetailList) {
        Map<String, MetricDetail> entry = new HashMap<String, MetricDetail>();
        for (MetricDetail metricDetail : metricDetailList) {
            entry.put(metricDetail.getKeyName(), metricDetail);
        }
        return entry;
    }


    /**
     * 把appInfo查询出来的list转为map<包名,AppInfo>，以便快速使用
     *
     * @param appList
     * @param appInfoType
     * @return
     */
    public static Map<String, com.myee.tarot.metric.domain.AppInfo> appInfoListToMap(List<com.myee.tarot.metric.domain.AppInfo> appList, int appInfoType) {
        if (appList == null || appList.size() == 0) {
            return Collections.EMPTY_MAP;
        }
        Map<String, com.myee.tarot.metric.domain.AppInfo> entry = new HashMap<String, com.myee.tarot.metric.domain.AppInfo>();
        for (com.myee.tarot.metric.domain.AppInfo appInfo : appList) {
            if (appInfo.getType() != appInfoType) {
                continue;
            }
            entry.put(appInfo.getPackageName(), appInfo);
        }
        return entry;
    }

    public static Map<String, List<MetricInfo>> mapMetricInfoListByKeyName(int pointCount,
                                                                            Map<String, List<MetricInfo>> map,
                                                                            Map<String, MetricDetail> metricDetailMap,
                                                                            List<com.myee.djinn.dto.metrics.MetricInfo> metricInfoList,
                                                                            DeviceUsedService deviceUsedService,
                                                                            com.myee.djinn.dto.metrics.SystemMetrics systemMetrics) {
        for (String keyName : map.keySet()) {
            if (map.get(keyName) == null) {
                LOGGER.info("{} 没有值", keyName);
            }
        }
        if (map != null && map.size() == 0) {
            for (String keyName : Constants.METRICS_NEED_TIME_KEY_LIST) {
                map.put(keyName, new ArrayList<MetricInfo>());
            }
        }
        for (com.myee.djinn.dto.metrics.MetricInfo metricInfo : metricInfoList) {
            if (Constants.METRICS_NEED_TIME_KEY_LIST.contains(metricInfo.getName())) {
                MetricDetail metricDetail = metricDetailMap.get(metricInfo.getName());
                if (metricDetail == null) {
                    continue;
                }
                List<MetricInfo> tempList = map.get(systemMetrics.getBoardNo() + "_" + metricInfo.getName());
                DeviceUsed deviceUsed = deviceUsedService.getByBoardNo(systemMetrics.getBoardNo());

                if (tempList != null && tempList.size() >= pointCount) {
                    tempList.remove(tempList.get(0));
                    LOGGER.info("{}", metricInfo.getName() + "移除第一个元素");
                } else if (tempList != null && tempList.size() == 0) {
                    tempList = new ArrayList<MetricInfo>();
                }
                MetricInfo metricInfoTarot = transformDijnnMetricInfo(metricInfo, deviceUsed, systemMetrics);
                LOGGER.info("本轮获取到的指标名称->{}", metricInfoTarot.getKeyName());
                LOGGER.info("本轮获取到的日志时间->{}", metricInfoTarot.getLogTime());
                LOGGER.info("本轮获取到的值->{}", metricInfoTarot.getValue());
                tempList.add(metricInfoTarot);
                map.put(metricInfoTarot.getBoardNo() + "_" + metricInfo.getName(), tempList);
            }
        }
        return map;
    }

    public static List<MetricInfo> listMetricsInfoPointsByPeriod(List<String> metricsKeyString, MetricInfoService metricInfoService, Long period, String boardNo) {
        List<MetricInfo> listMetricsInfoPointsByPeriod = metricInfoService.listMetricsInfoPointsByPeriod(metricsKeyString, period, boardNo);
        return listMetricsInfoPointsByPeriod;
    }

    public static Map<String, List<MetricInfo>> insertReportDataInRedis(Map<String, List<MetricInfo>> fiveMinMap, List<com.myee.djinn.dto.metrics.SystemMetrics> list, MetricDetailService metricDetailService, DeviceUsedService deviceUsedService, int pointCount) {
        List<MetricDetail> metricDetailList = metricDetailService.list();
        Map<String, MetricDetail> metricDetailMap = metricDetailListToKeyNameMap(metricDetailList);

        for (com.myee.djinn.dto.metrics.SystemMetrics systemMetrics : list) {
            if (systemMetrics.getBoardNo() == null || StringUtil.isBlank(systemMetrics.getBoardNo())) {
                continue;
            }
            DeviceUsed deviceUsed = deviceUsedService.getByBoardNo(systemMetrics.getBoardNo());
            if (deviceUsed == null) {
                continue;
            }
            fiveMinMap = mapMetricInfoListByKeyName(pointCount, fiveMinMap, metricDetailMap, systemMetrics.getMetricInfoList(), deviceUsedService, systemMetrics);

        }
        return fiveMinMap;
    }

    /**
     * 转换dijnn的MetricInfo对象到tarot的MetricInfo对象
     *
     * @param metricInfo
     * @param deviceUsed
     * @param systemMetrics
     * @return
     */
    private static MetricInfo transformDijnnMetricInfo(com.myee.djinn.dto.metrics.MetricInfo metricInfo, DeviceUsed deviceUsed, com.myee.djinn.dto.metrics.SystemMetrics systemMetrics) {
        String boardNo = deviceUsed.getBoardNo();
        MetricInfo metricInfoDB = new MetricInfo();
        metricInfoDB.setKeyName(metricInfo.getName());
        metricInfoDB.setBoardNo(boardNo);
        metricInfoDB.setLogTime(DateTimeUtils.toMillis(systemMetrics.getLogTime()));
        metricInfoDB.setValue(metricInfo.getValue());
        return metricInfoDB;
    }
}
