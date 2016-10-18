package com.myee.tarot.remote.dao;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.metrics.domain.MetricsDetail;

/**
 * Created by Chay on 2016/8/10.
 */
public interface MetricsDetailDao extends GenericEntityDao<Long, MetricsDetail> {
    MetricsDetail findByKey(String name);
}
