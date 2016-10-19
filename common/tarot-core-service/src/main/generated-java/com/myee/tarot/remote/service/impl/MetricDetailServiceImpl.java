package com.myee.tarot.remote.service.impl;

import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.metric.domain.MetricDetail;
import com.myee.tarot.remote.dao.MetricDetailDao;
import com.myee.tarot.remote.service.MetricDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
@Service
public class MetricDetailServiceImpl extends GenericEntityServiceImpl<Long, MetricDetail> implements MetricDetailService {

    protected MetricDetailDao metricDetailDao;

    @Autowired
    public MetricDetailServiceImpl(MetricDetailDao metricDetailDao) {
        super(metricDetailDao);
        this.metricDetailDao = metricDetailDao;
    }

    @Override
    public MetricDetail findByKey(String name){
        return metricDetailDao.findByKey(name);
    }

    @Override
    public List<String> listKey(){
        return metricDetailDao.listKey();
    }
}
