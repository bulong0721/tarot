package com.myee.tarot.djinn.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.myee.djinn.dto.DataUploadInfoDTO;
import com.myee.djinn.dto.UploadResourceType;
import com.myee.djinn.dto.metrics.SystemMetrics;
import com.myee.djinn.rpc.RemoteException;
import com.myee.djinn.server.operations.DataStoreService;
import com.myee.tarot.cache.entity.MetricCache;
import com.myee.tarot.cache.util.RedissonUtil;
import com.myee.tarot.catalog.service.DeviceUsedService;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.exception.ServiceException;
import com.myee.tarot.core.service.TransactionalAspectAware;
import com.myee.tarot.datacenter.domain.SelfCheckLog;
import com.myee.tarot.datacenter.domain.SelfCheckLogVO;
import com.myee.tarot.datacenter.service.SelfCheckLogService;
import com.myee.tarot.metric.domain.MetricInfo;
import com.myee.tarot.remote.service.AppInfoService;
import com.myee.tarot.remote.service.MetricDetailService;
import com.myee.tarot.remote.service.MetricInfoService;
import com.myee.tarot.remote.service.SystemMetricsService;
import com.myee.tarot.remote.util.MetricsUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Martin on 2016/9/6.
 */
@Service
public class DataStoreServiceImpl implements DataStoreService, TransactionalAspectAware {

    private static final Logger LOG = LoggerFactory.getLogger(DataStoreServiceImpl.class);
    @Autowired
    private SelfCheckLogService selfCheckLogService;
    @Autowired
    private SystemMetricsService systemMetricsService;
    @Autowired
    private AppInfoService appInfoService;
    @Autowired
    private MetricInfoService metricInfoService;
    @Autowired
    private MetricDetailService metricDetailService;
    @Autowired
    private DeviceUsedService deviceUsedService;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    @Value("${cleverm.push.dirs}")
    private String DOWNLOAD_HOME;

    @Autowired
    private RedissonUtil redissonUtil;

    @Override
    public int receiveLog(long orgId, UploadResourceType fileType, String logText) throws RemoteException {
        return 0;
    }

    @Override
    public int receiveText(long orgId, String text) throws RemoteException {
        return 0;
    }

    @Override
    public String readTextFile(long orgId, String path) throws RemoteException {
        LOG.info("orgId= {}  path= {} DOWNLOAD_HOME={}", orgId, path, DOWNLOAD_HOME);
        path = path.replaceAll("#","");
        String fileData = "";
        StringBuffer sb = new StringBuffer();
        sb.append(DOWNLOAD_HOME).append(File.separator).append(orgId).append(File.separator).append(path);
        String filePath = sb.toString();
        LOG.info(" File path is ==========" + filePath);
        File file = new File(filePath);
        if (!file.exists()) {
            return fileData;
        }
        try {
            fileData = FileUtils.readFileToString(file, "utf-8");
        } catch (IOException e) {
            LOG.error(" read file error ", e);
        }

        return fileData;
    }

    @Override
    public boolean uploadData(DataUploadInfoDTO dataUploadInfoDTO) throws RemoteException {
        if (dataUploadInfoDTO != null && "selfCheckLog".equals(dataUploadInfoDTO.getType().getValue())) {
            SelfCheckLogVO selfCheckLogVO = JSON.parseObject(dataUploadInfoDTO.getData(), SelfCheckLogVO.class);
            try {
                SelfCheckLog scl = selfCheckLogService.update(new SelfCheckLog(selfCheckLogVO));
                if (scl != null) {
                    return true;
                }
            } catch (ServiceException e) {
                System.out.println("error: " + e.toString());
            }
        }
        return false;
    }

    @Override
    public boolean uploadSystemMetrics(final List<SystemMetrics> list) throws RemoteException {
        if (list == null) {
            return false;
        }

        final List<SystemMetrics> list1 = JSON.parseArray(JSON.toJSONString(list), SystemMetrics.class);
        //异步插入数据库
        //用线程池代替原来的new Thread方法
        taskExecutor.submit(new Runnable() {
            @Override
			public void run() {
				MetricsUtil.updateSystemMetrics(list1, deviceUsedService, appInfoService, metricInfoService, metricDetailService, systemMetricsService);
			}
		});
        LOG.info("------开始执行-----");
        //新线程跑前台展示用的数据
        taskExecutor.submit(new Runnable() {
            @Override
            public void run() {
//                List<SystemMetrics> list1 = JSON.parseArray(JSON.toJSONString(list), SystemMetrics.class);
                long now = System.currentTimeMillis();
                for (Long range : Constants.METRICS_SELECT_RANGE_LIST) {
                    insertReportTable(now, range, list1);
                }
            }
        });
        LOG.info("------结束执行-----");
        return true;
    }

    /**
     * 例如，一个小时范围的数据：每5秒钟插入一次报表的数据，更新最新元素插入时间点，每次拿当前时间跟上一次更新时间比较如果大于等于5秒钟的间隔，
     * 则插入或更新，满了之后再移出最早的元素，插入最新的元素,
     * 查看更新时间
     * 1个小时有多少个5秒钟
     * @param now
     * @param type
     * @param list
     */
    private void insertReportTable(long now, Long type, List<SystemMetrics> list) {
        int pointCount = 0;
        MetricCache metricCache = redissonUtil.metricCache();
        LOG.info("{}", metricCache);
        boolean ifContainKey = false;
        try {
            Map<String, Long> map = metricCache.getLastUpdateTimeCache();
            if (map != null) {
                for (String key : map.keySet()) {
                    if (key.startsWith(list.get(0).getBoardNo())) {
                        ifContainKey = true;
                        break;
                    }
                }
            }
            if (metricCache != null && map == null || (map != null && !ifContainKey)) {
                Map<String, Long> lastUpdateTimeCache = Maps.newConcurrentMap();
                lastUpdateTimeCache.put(list.get(0).getBoardNo() + "_" + MetricCache.LAST_UPDATE_TIME_KEY_ONE_HOUR, 0L);
                lastUpdateTimeCache.put(list.get(0).getBoardNo() + "_" + MetricCache.LAST_UPDATE_TIME_KEY_TWO_HOUR, 0L);
                lastUpdateTimeCache.put(list.get(0).getBoardNo() + "_" + MetricCache.LAST_UPDATE_TIME_KEY_FOUR_HOUR, 0L);
                lastUpdateTimeCache.put(list.get(0).getBoardNo() + "_" + MetricCache.LAST_UPDATE_TIME_KEY_HALF_DAY, 0L);
                lastUpdateTimeCache.put(list.get(0).getBoardNo() + "_" + MetricCache.LAST_UPDATE_TIME_KEY_ONE_DAY, 0L);
                lastUpdateTimeCache.put(list.get(0).getBoardNo() + "_" + MetricCache.LAST_UPDATE_TIME_KEY_ONE_WEEK, 0L);
                lastUpdateTimeCache.put(list.get(0).getBoardNo() + "_" + MetricCache.LAST_UPDATE_TIME_KEY_ONE_MONTH, 0L);
                lastUpdateTimeCache.put(list.get(0).getBoardNo() + "_" + MetricCache.LAST_UPDATE_TIME_KEY_ONE_YEAR, 0L);
                metricCache.setLastUpdateTimeCache(lastUpdateTimeCache);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ifContainKey = false;
        Map<String, Long> lastUpdateTimeCache = metricCache.getLastUpdateTimeCache();
        if (type.equals(Constants.METRICS_SELECT_RANGE_LIST.get(0))) { //一小时范围,每30秒1个点
            pointCount = Constants.ONE_HOUR_POINT_COUNT;
            Map<String, List<MetricInfo>> oneHourMetricInfoCache = metricCache.getOneHourMetricInfoPointsCache();
            if (oneHourMetricInfoCache != null) {
                for (String key : oneHourMetricInfoCache.keySet()) {
                    if (key.startsWith(list.get(0).getBoardNo())) {
                        ifContainKey = true;
                        break;
                    }
                }
            }
            if (oneHourMetricInfoCache == null || (oneHourMetricInfoCache != null && !ifContainKey)) {
                oneHourMetricInfoCache = Maps.newConcurrentMap();

                metricCache.setOneHourMetricInfoPointsCache(oneHourMetricInfoCache);
            }
            insertDataToRedisByRange(pointCount, list, now, lastUpdateTimeCache, metricCache.getOneHourMetricInfoPointsCache(), list.get(0).getBoardNo() + "_" + MetricCache.LAST_UPDATE_TIME_KEY_ONE_HOUR, Constants.INTERVAL_ONE_HOUR);
        } else if (type.equals(Constants.METRICS_SELECT_RANGE_LIST.get(1))) { //两小时范围
            pointCount = Constants.TWO_HOUR_POINT_COUNT;
            Map<String, List<MetricInfo>> twoHourMetricInfoCache = metricCache.getTwoHourMetricInfoPointsCache();
            if (twoHourMetricInfoCache != null) {
                for (String key : twoHourMetricInfoCache.keySet()) {
                    if (key.startsWith(list.get(0).getBoardNo())) {
                        ifContainKey = true;
                        break;
                    }
                }
            }
            if (twoHourMetricInfoCache == null || (twoHourMetricInfoCache != null && !ifContainKey)) {
                twoHourMetricInfoCache = Maps.newConcurrentMap();
                metricCache.setTwoHourMetricInfoPointsCache(twoHourMetricInfoCache);
            }
            insertDataToRedisByRange(pointCount, list, now,lastUpdateTimeCache, metricCache.getTwoHourMetricInfoPointsCache(), list.get(0).getBoardNo() + "_" + MetricCache.LAST_UPDATE_TIME_KEY_TWO_HOUR, Constants.INTERVAL_TWO_HOUR);
        } else if (type.equals(Constants.METRICS_SELECT_RANGE_LIST.get(2))) { //四小时范围
            pointCount = Constants.FOUR_HOUR_POINT_COUNT;
            Map<String, List<MetricInfo>> fourHourMetricInfoCache = metricCache.getFourHourMetricInfoPointsCache();
            if (fourHourMetricInfoCache != null) {
                for (String key : fourHourMetricInfoCache.keySet()) {
                    if (key.startsWith(list.get(0).getBoardNo())) {
                        ifContainKey = true;
                        break;
                    }
                }
            }
            if (fourHourMetricInfoCache == null || (fourHourMetricInfoCache != null && !ifContainKey)) {
                fourHourMetricInfoCache = Maps.newConcurrentMap();
                metricCache.setFourHourMetricInfoPointsCache(fourHourMetricInfoCache);
            }
            insertDataToRedisByRange(pointCount, list, now,lastUpdateTimeCache, metricCache.getFourHourMetricInfoPointsCache(), list.get(0).getBoardNo() + "_" + MetricCache.LAST_UPDATE_TIME_KEY_FOUR_HOUR, Constants.INTERVAL_FOUR_HOUR);
        } else if (type.equals(Constants.METRICS_SELECT_RANGE_LIST.get(3))) { //半天范围
            pointCount = Constants.HALF_DAY_POINT_COUNT;
            Map<String, List<MetricInfo>> halfDayMetricInfoCache = metricCache.getHalfDayMetricInfoPointsCache();
            if (halfDayMetricInfoCache != null) {
                for (String key : halfDayMetricInfoCache.keySet()) {
                    if (key.startsWith(list.get(0).getBoardNo())) {
                        ifContainKey = true;
                        break;
                    }
                }
            }
            if (halfDayMetricInfoCache == null || (halfDayMetricInfoCache != null && !ifContainKey)) {
                halfDayMetricInfoCache = Maps.newConcurrentMap();
                metricCache.setHalfDayMetricInfoPointsCache(halfDayMetricInfoCache);
            }
            insertDataToRedisByRange(pointCount, list, now,lastUpdateTimeCache, metricCache.getHalfDayMetricInfoPointsCache(), list.get(0).getBoardNo() + "_" + MetricCache.LAST_UPDATE_TIME_KEY_HALF_DAY, Constants.INTERVAL_HALF_DAY);
        } else if (type.equals(Constants.METRICS_SELECT_RANGE_LIST.get(4))) { //一天范围
            pointCount = Constants.ONE_DAY_POINT_COUNT;
            Map<String, List<MetricInfo>> oneDayMetricInfoCache = metricCache.getOneDayMetricInfoPointsCache();
            if (oneDayMetricInfoCache != null) {
                for (String key : oneDayMetricInfoCache.keySet()) {
                    if (key.startsWith(list.get(0).getBoardNo())) {
                        ifContainKey = true;
                        break;
                    }
                }
            }
            if (oneDayMetricInfoCache == null || (oneDayMetricInfoCache != null && !ifContainKey)) {
                oneDayMetricInfoCache = Maps.newConcurrentMap();
                metricCache.setOneDayMetricInfoPointsCache(oneDayMetricInfoCache);
            }
            insertDataToRedisByRange(pointCount, list, now,lastUpdateTimeCache, metricCache.getOneDayMetricInfoPointsCache(), list.get(0).getBoardNo() + "_" + MetricCache.LAST_UPDATE_TIME_KEY_ONE_DAY, Constants.INTERVAL_ONE_DAY);
        } else if (type.equals(Constants.METRICS_SELECT_RANGE_LIST.get(5))) { //一周范围
            pointCount = Constants.ONE_WEEK_POINT_COUNT;
            Map<String, List<MetricInfo>> oneWeekMetricInfoCache = metricCache.getOneWeekMetricInfoPointsCache();
            if (oneWeekMetricInfoCache != null) {
                for (String key : oneWeekMetricInfoCache.keySet()) {
                    if (key.startsWith(list.get(0).getBoardNo())) {
                        ifContainKey = true;
                        break;
                    }
                }
            }
            if (oneWeekMetricInfoCache == null || (oneWeekMetricInfoCache != null && !ifContainKey)) {
                oneWeekMetricInfoCache = Maps.newConcurrentMap();
                metricCache.setOneWeekMetricInfoPointsCache(oneWeekMetricInfoCache);
            }
            insertDataToRedisByRange(pointCount, list, now,lastUpdateTimeCache, metricCache.getOneWeekMetricInfoPointsCache(), list.get(0).getBoardNo() + "_" + MetricCache.LAST_UPDATE_TIME_KEY_ONE_WEEK, Constants.INTERVAL_ONE_WEEK);
        } else if (type.equals(Constants.METRICS_SELECT_RANGE_LIST.get(6))) { //一个月范围
            pointCount = Constants.ONE_MONTH_POINT_COUNT;
            Map<String, List<MetricInfo>> oneMonthMetricInfoCache = metricCache.getOneMonthMetricInfoPointsCache();
            if (oneMonthMetricInfoCache != null) {
                for (String key : oneMonthMetricInfoCache.keySet()) {
                    if (key.startsWith(list.get(0).getBoardNo())) {
                        ifContainKey = true;
                        break;
                    }
                }
            }
            if (oneMonthMetricInfoCache == null || (oneMonthMetricInfoCache != null && !ifContainKey)) {
                oneMonthMetricInfoCache = Maps.newConcurrentMap();
                metricCache.setOneMonthMetricInfoPointsCache(oneMonthMetricInfoCache);
            }
            insertDataToRedisByRange(pointCount, list, now,lastUpdateTimeCache, metricCache.getOneMonthMetricInfoPointsCache(), list.get(0).getBoardNo() + "_" + MetricCache.LAST_UPDATE_TIME_KEY_ONE_MONTH, Constants.INTERVAL_ONE_MONTH);
        } else if (type.equals(Constants.METRICS_SELECT_RANGE_LIST.get(7))) { //一年范围
            pointCount = Constants.ONE_YEAR_POINT_COUNT;
            Map<String, List<MetricInfo>> oneYearMetricInfoCache = redissonUtil.metricCache().getOneYearMetricInfoPointsCache();
            if (oneYearMetricInfoCache != null) {
                for (String key : oneYearMetricInfoCache.keySet()) {
                    if (key.startsWith(list.get(0).getBoardNo())) {
                        ifContainKey = true;
                        break;
                    }
                }
            }
            if (oneYearMetricInfoCache == null || (oneYearMetricInfoCache != null && !ifContainKey)) {
                oneYearMetricInfoCache = Maps.newConcurrentMap();
                metricCache.setOneYearMetricInfoPointsCache(oneYearMetricInfoCache);
            }
            insertDataToRedisByRange(pointCount, list, now,lastUpdateTimeCache, metricCache.getOneYearMetricInfoPointsCache(), list.get(0).getBoardNo() + "_" + MetricCache.LAST_UPDATE_TIME_KEY_ONE_YEAR, Constants.INTERVAL_ONE_YEAR);
        }
    }

    private void insertDataToRedisByRange(int pointCount,
                                          List<SystemMetrics> list, long now, Map<String, Long> lastUpdateTimeCache,
                                          Map<String, List<MetricInfo>> metricInfoCache, String lastUpdateTimeKey, long timeIntervalVal) {
        if (lastUpdateTimeCache == null || metricInfoCache == null) {
            LOG.info("程序无法执行");
            throw new RuntimeException("缓存为空,程序无法执行");
        } else {
            if (lastUpdateTimeCache != null && lastUpdateTimeCache.get(lastUpdateTimeKey).equals(0L) || metricInfoCache != null && metricInfoCache.size() == 0) { //如果第一次插入就不考虑任何因素
                MetricsUtil.insertReportDataInRedis(metricInfoCache, list, metricDetailService, deviceUsedService, pointCount);
                lastUpdateTimeCache.put(lastUpdateTimeKey, now);
                LOG.info("第一次插数据后的更新时间 {}", lastUpdateTimeCache.get(lastUpdateTimeKey));
            } else {
                long lastUpdateTime = lastUpdateTimeCache.get(lastUpdateTimeKey);
                long timeInterval = now - lastUpdateTime;
                if (timeInterval < 0) {
                    LOG.error("程序异常");
                    throw new RuntimeException("程序异常");
                }
                if (timeInterval >= timeIntervalVal) { //如果间隔超过10秒钟的毫秒数了，则更新Redis中数据
                    MetricsUtil.insertReportDataInRedis(metricInfoCache, list, metricDetailService, deviceUsedService, pointCount);
                    lastUpdateTimeCache.put(lastUpdateTimeKey, now);
                }
            }
        }
    }
}
