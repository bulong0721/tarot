package com.myee.tarot.remote.service;

import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.metric.domain.SystemMetrics;

import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
public interface SystemMetricsService extends GenericEntityService<Long, SystemMetrics> {
    SystemMetrics getLatestByDUId(Long deviceUsedId);

    List<SystemMetrics> listByDUIdPeriodKeyList(Long deviceUsedId, Long period, List<String> metricsKeyList);
}
