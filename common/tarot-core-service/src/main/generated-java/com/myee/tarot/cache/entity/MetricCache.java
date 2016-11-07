package com.myee.tarot.cache.entity;

import com.myee.tarot.metric.domain.MetricInfo;
import org.redisson.api.annotation.REntity;
import org.redisson.api.annotation.RId;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Ray.Fu on 2016/11/3.
 */
@REntity
public class MetricCache implements Serializable {
    @RId
    private String envName;

    private Map<String, Long> lastUpdateTimeCache;

    private Map<String, List<MetricInfo>> oneHourMetricInfoPointsCache;

    private Map<String, List<MetricInfo>> twoHourMetricInfoPointsCache;

    private Map<String, List<MetricInfo>> fourHourMetricInfoPointsCache;

    private Map<String, List<MetricInfo>> halfDayMetricInfoPointsCache;

    private Map<String, List<MetricInfo>> oneDayMetricInfoPointsCache;

    private Map<String, List<MetricInfo>> oneWeekMetricInfoPointsCache;

    private Map<String, List<MetricInfo>> oneMonthMetricInfoPointsCache;

    private Map<String, List<MetricInfo>> oneYearMetricInfoPointsCache;

    public static final String LAST_UPDATE_TIME_KEY_ONE_HOUR = "oneHourLastUpdateTime"; //一小时的查询范围的时刻更新

    public static final String LAST_UPDATE_TIME_KEY_TWO_HOUR = "twoHourLastUpdateTime"; //两小时的查询范围的时刻更新

    public static final String LAST_UPDATE_TIME_KEY_FOUR_HOUR = "fourHourLastUpdateTime"; //四小时的查询范围的时刻更新

    public static final String LAST_UPDATE_TIME_KEY_HALF_DAY = "halfDayLastUpdateTime"; //半天的查询范围的时刻更新

    public static final String LAST_UPDATE_TIME_KEY_ONE_DAY = "oneDayLastUpdateTime"; //一天的查询范围的时刻更新

    public static final String LAST_UPDATE_TIME_KEY_ONE_WEEK = "oneWeekLastUpdateTime"; //一周的查询范围的时刻更新

    public static final String LAST_UPDATE_TIME_KEY_ONE_MONTH = "oneMonthLastUpdateTime"; //一个月的查询范围的时刻更新

    public static final String LAST_UPDATE_TIME_KEY_ONE_YEAR = "oneYearLastUpdateTime"; //一年的查询范围的时刻更新

    public static final String ONE_HOUR_METRICINFO_KEY = "oneHourMetricInfo"; //一小时的查询范围的数据

    public static final String TWO_HOUR_METRICINFO_KEY = "twoHourMetricInfo"; //两小时的查询范围的数据

    public static final String FOUR_HOUR_METRICINFO_KEY = "fourHourMetricInfo";

    public static final String HALF_DAY_METRICINFO_KEY = "halfDayMetricInfo";

    public static final String ONE_DAY_METRICINFO_KEY = "oneDayMetricInfo";

    public static final String ONE_WEEK_METRICINFO_KEY = "oneWeekMetricInfo";

    public static final String ONE_MONTH_METRICINFO_KEY = "oneMonthMetricInfo";

    public static final String ONE_YEAR_METRICINFO_KEY = "oneYearMetricInfo";

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public Map<String, Long> getLastUpdateTimeCache() {
        return lastUpdateTimeCache;
    }

    public void setLastUpdateTimeCache(Map<String, Long> lastUpdateTimeCache) {
        this.lastUpdateTimeCache = lastUpdateTimeCache;
    }

    public Map<String, List<MetricInfo>> getOneHourMetricInfoPointsCache() {
        return oneHourMetricInfoPointsCache;
    }

    public void setOneHourMetricInfoPointsCache(Map<String, List<MetricInfo>> oneHourMetricInfoPointsCache) {
        this.oneHourMetricInfoPointsCache = oneHourMetricInfoPointsCache;
    }

    public Map<String, List<MetricInfo>> getTwoHourMetricInfoPointsCache() {
        return twoHourMetricInfoPointsCache;
    }

    public void setTwoHourMetricInfoPointsCache(Map<String, List<MetricInfo>> twoHourMetricInfoPointsCache) {
        this.twoHourMetricInfoPointsCache = twoHourMetricInfoPointsCache;
    }

    public Map<String, List<MetricInfo>> getFourHourMetricInfoPointsCache() {
        return fourHourMetricInfoPointsCache;
    }

    public void setFourHourMetricInfoPointsCache(Map<String, List<MetricInfo>> fourHourMetricInfoPointsCache) {
        this.fourHourMetricInfoPointsCache = fourHourMetricInfoPointsCache;
    }

    public Map<String, List<MetricInfo>> getHalfDayMetricInfoPointsCache() {
        return halfDayMetricInfoPointsCache;
    }

    public void setHalfDayMetricInfoPointsCache(Map<String, List<MetricInfo>> halfDayMetricInfoPointsCache) {
        this.halfDayMetricInfoPointsCache = halfDayMetricInfoPointsCache;
    }

    public Map<String, List<MetricInfo>> getOneDayMetricInfoPointsCache() {
        return oneDayMetricInfoPointsCache;
    }

    public void setOneDayMetricInfoPointsCache(Map<String, List<MetricInfo>> oneDayMetricInfoPointsCache) {
        this.oneDayMetricInfoPointsCache = oneDayMetricInfoPointsCache;
    }

    public Map<String, List<MetricInfo>> getOneWeekMetricInfoPointsCache() {
        return oneWeekMetricInfoPointsCache;
    }

    public void setOneWeekMetricInfoPointsCache(Map<String, List<MetricInfo>> oneWeekMetricInfoPointsCache) {
        this.oneWeekMetricInfoPointsCache = oneWeekMetricInfoPointsCache;
    }

    public Map<String, List<MetricInfo>> getOneMonthMetricInfoPointsCache() {
        return oneMonthMetricInfoPointsCache;
    }

    public void setOneMonthMetricInfoPointsCache(Map<String, List<MetricInfo>> oneMonthMetricInfoPointsCache) {
        this.oneMonthMetricInfoPointsCache = oneMonthMetricInfoPointsCache;
    }

    public Map<String, List<MetricInfo>> getOneYearMetricInfoPointsCache() {
        return oneYearMetricInfoPointsCache;
    }

    public void setOneYearMetricInfoPointsCache(Map<String, List<MetricInfo>> oneYearMetricInfoPointsCache) {
        this.oneYearMetricInfoPointsCache = oneYearMetricInfoPointsCache;
    }
}
