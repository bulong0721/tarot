package com.myee.tarot.remote.service.impl;

import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.metrics.domain.AppInfo;
import com.myee.tarot.metrics.domain.MetricsInfo;
import com.myee.tarot.remote.dao.AppInfoDao;
import com.myee.tarot.remote.dao.MetricsInfoDao;
import com.myee.tarot.remote.service.AppInfoService;
import com.myee.tarot.remote.service.MetricsInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Chay on 2016/8/10.
 */
@Service
public class MetricsInfoServiceImpl extends GenericEntityServiceImpl<Long, MetricsInfo> implements MetricsInfoService {

    protected MetricsInfoDao metricsInfoDao;

    @Autowired
    public MetricsInfoServiceImpl(MetricsInfoDao metricsInfoDao) {
        super(metricsInfoDao);
        this.metricsInfoDao = metricsInfoDao;
    }
}
