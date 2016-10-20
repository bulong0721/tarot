package com.myee.tarot.remote.service.impl;

import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.metric.domain.SystemMetrics;
import com.myee.tarot.remote.dao.SystemMetricsDao;
import com.myee.tarot.remote.service.SystemMetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
@Service
public class SystemMetricsServiceImpl extends GenericEntityServiceImpl<Long, SystemMetrics> implements SystemMetricsService {

    protected SystemMetricsDao systemMetricsDao;

    @Autowired
    public SystemMetricsServiceImpl(SystemMetricsDao systemMetricsDao) {
        super(systemMetricsDao);
        this.systemMetricsDao = systemMetricsDao;
    }

    @Override
    public SystemMetrics getLatestByBoardNo(String boardNo) {
        return systemMetricsDao.getLatestByBoardNo(boardNo);
    }

    @Override
    public List<SystemMetrics> listByBoardNoPeriodKeyList(String boardNo, Long period, List<String> metricsKeyList){
        return systemMetricsDao.listByBoardNoPeriodKeyList(boardNo, period, metricsKeyList);
    }
}
