package com.myee.tarot.remote.service;

import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.metric.domain.MetricDetail;

import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
public interface MetricDetailService extends GenericEntityService<Long, MetricDetail> {
    MetricDetail findByKey(String name);

    List<String> listKey();
}
