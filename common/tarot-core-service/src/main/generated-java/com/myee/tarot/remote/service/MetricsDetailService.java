package com.myee.tarot.remote.service;

import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.metric.domain.MetricDetail;

/**
 * Created by Chay on 2016/8/10.
 */
public interface MetricsDetailService extends GenericEntityService<Long, MetricDetail> {
    MetricDetail findByKey(String name);
}
