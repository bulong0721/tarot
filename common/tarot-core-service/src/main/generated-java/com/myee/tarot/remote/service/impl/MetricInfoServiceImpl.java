package com.myee.tarot.remote.service.impl;

import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.metric.domain.MetricInfo;
import com.myee.tarot.remote.dao.MetricInfoDao;
import com.myee.tarot.remote.service.MetricInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
@Service
public class MetricInfoServiceImpl extends GenericEntityServiceImpl<Long, MetricInfo> implements MetricInfoService {

    protected MetricInfoDao metricInfoDao;

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
}
