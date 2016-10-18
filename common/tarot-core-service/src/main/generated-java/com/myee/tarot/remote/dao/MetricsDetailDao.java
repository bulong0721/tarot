package com.myee.tarot.remote.dao;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.metric.domain.MetricDetail;

/**
 * Created by Chay on 2016/8/10.
 */
public interface MetricsDetailDao extends GenericEntityDao<Long, MetricDetail> {
    MetricDetail findByKey(String name);
}
