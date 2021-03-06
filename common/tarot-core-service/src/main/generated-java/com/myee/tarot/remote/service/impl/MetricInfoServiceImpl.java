package com.myee.tarot.remote.service.impl;

import com.google.common.collect.Lists;
import com.myee.tarot.cache.entity.MetricCache;
import com.myee.tarot.cache.util.RedissonUtil;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.metric.domain.MetricInfo;
import com.myee.tarot.remote.dao.MetricInfoDao;
import com.myee.tarot.remote.service.MetricInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Chay on 2016/8/10.
 */
@Service
public class MetricInfoServiceImpl extends GenericEntityServiceImpl<Long, MetricInfo> implements MetricInfoService {

    protected MetricInfoDao metricInfoDao;

    @Autowired
    private RedissonUtil redissonUtil;

    @Autowired
    public MetricInfoServiceImpl(MetricInfoDao metricInfoDao) {
        super(metricInfoDao);
        this.metricInfoDao = metricInfoDao;
    }

    public List<MetricInfo> listBySystemMetricsId(Long systemMetricsId, List<String> metricsKeyListToSearch) {
        return metricInfoDao.listBySystemMetricsId(systemMetricsId, metricsKeyListToSearch);
    }

    public List<MetricInfo> listByBoardNoPeriod(String boardNo, Long now, Long period, String nodeName, List<String> metricsKeyList) {
        return metricInfoDao.listByBoardNoPeriod( boardNo,  now,  period,  nodeName,metricsKeyList);
    }

    @Override
    public List<MetricInfo> listMetricsInfoPointsByPeriod(List<String> metricsKeyString, Long period, String boardNo) {
        MetricCache metricCache = redissonUtil.metricCache();
        List<MetricInfo> list = getListOfMetricInfoByKeyName(metricsKeyString, period, metricCache, boardNo);
        return list;
    }

    @Override
    public void deleteByTime(Date date) {
        metricInfoDao.deleteByTime(date);
    }

    @Override
    public List<MetricInfo> listByCreateTime(Date date) {
        return metricInfoDao.listByCreateTime(date);
    }

    private List<MetricInfo> getListOfMetricInfoByKeyName(List<String> metricsKeyString, Long period, MetricCache metricCache, String boardNo) {
        List<MetricInfo> list = Lists.newArrayList();
        Map<String, List<MetricInfo>> metricInfoPointsCache = null;
        //获取什么范围的list
        if (period.equals(Constants.METRICS_SELECT_RANGE_LIST.get(0))) {
            metricInfoPointsCache = metricCache.getOneHourMetricInfoPointsCache();
        } else if (period.equals(Constants.METRICS_SELECT_RANGE_LIST.get(1))) {
            metricInfoPointsCache = metricCache.getTwoHourMetricInfoPointsCache();
        } else if (period.equals(Constants.METRICS_SELECT_RANGE_LIST.get(2))) {
            metricInfoPointsCache = metricCache.getFourHourMetricInfoPointsCache();
        } else if (period.equals(Constants.METRICS_SELECT_RANGE_LIST.get(3))) {
            metricInfoPointsCache = metricCache.getHalfDayMetricInfoPointsCache();
        } else if (period.equals(Constants.METRICS_SELECT_RANGE_LIST.get(4))) {
            metricInfoPointsCache = metricCache.getOneDayMetricInfoPointsCache();
        } else if (period.equals(Constants.METRICS_SELECT_RANGE_LIST.get(5))) {
            metricInfoPointsCache = metricCache.getOneWeekMetricInfoPointsCache();
        } else if (period.equals(Constants.METRICS_SELECT_RANGE_LIST.get(6))) {
            metricInfoPointsCache = metricCache.getOneMonthMetricInfoPointsCache();
        } else if (period.equals(Constants.METRICS_SELECT_RANGE_LIST.get(7))) {
            metricInfoPointsCache = metricCache.getOneYearMetricInfoPointsCache();
        }
        for (String keyName : metricsKeyString) {
            if (metricInfoPointsCache.get(boardNo + "_" + keyName) != null) {
                list.addAll(metricInfoPointsCache.get(boardNo + "_" + keyName));
            }
        }
        return list;
    }
}
