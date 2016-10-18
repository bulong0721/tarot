package com.myee.tarot.remote.service.impl;

import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.metric.domain.MetricDetail;
import com.myee.tarot.remote.dao.MetricsDetailDao;
import com.myee.tarot.remote.service.MetricsDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Chay on 2016/8/10.
 */
@Service
public class MetricsDetailServiceImpl extends GenericEntityServiceImpl<Long, MetricDetail> implements MetricsDetailService {

    protected MetricsDetailDao metricsDetailDao;

    @Autowired
    public MetricsDetailServiceImpl(MetricsDetailDao metricsDetailDao) {
        super(metricsDetailDao);
        this.metricsDetailDao = metricsDetailDao;
    }

    @Override
    public MetricDetail findByKey(String name){
        return metricsDetailDao.findByKey(name);
    }
}
