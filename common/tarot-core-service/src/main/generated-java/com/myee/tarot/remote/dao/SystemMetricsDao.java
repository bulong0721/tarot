package com.myee.tarot.remote.dao;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.metric.domain.SystemMetrics;

import java.util.Date;
import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
public interface SystemMetricsDao extends GenericEntityDao<Long, SystemMetrics> {
    SystemMetrics getLatestByBoardNo(String boardNo, String nodeName, Long period, Long now);

    List<SystemMetrics> listByBoardNoPeriod(String boardNo, Long now, Long period, String nodeName);

    SystemMetrics getByBoardNoLogTimeNod(String boardNo, long logTime, String node);

    List<SystemMetrics> listByCreateTime(Date date);

    void deleteByTime(Date date);
}
