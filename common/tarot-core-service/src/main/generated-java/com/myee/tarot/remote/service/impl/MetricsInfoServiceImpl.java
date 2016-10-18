package com.myee.tarot.remote.service.impl;

import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.metric.domain.MetricInfo;
import com.myee.tarot.remote.dao.MetricsInfoDao;
import com.myee.tarot.remote.service.MetricsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Chay on 2016/8/10.
 */
@Service
public class MetricsInfoServiceImpl extends GenericEntityServiceImpl<Long, MetricInfo> implements MetricsInfoService {

    protected MetricsInfoDao metricsInfoDao;

    @Autowired
    public MetricsInfoServiceImpl(MetricsInfoDao metricsInfoDao) {
        super(metricsInfoDao);
        this.metricsInfoDao = metricsInfoDao;
    }
}
