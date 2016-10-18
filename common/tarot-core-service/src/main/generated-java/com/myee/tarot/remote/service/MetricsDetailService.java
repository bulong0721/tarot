package com.myee.tarot.remote.service;

import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.metrics.domain.MetricsDetail;

/**
 * Created by Chay on 2016/8/10.
 */
public interface MetricsDetailService extends GenericEntityService<Long, MetricsDetail> {
    MetricsDetail findByKey(String name);
}
