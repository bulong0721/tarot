package com.myee.tarot.remote.dao;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.metrics.domain.SystemMetrics;

import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
public interface SystemMetricsDao extends GenericEntityDao<Long, SystemMetrics> {
    SystemMetrics getLatestByDUId(Long deviceUsedId);
}
