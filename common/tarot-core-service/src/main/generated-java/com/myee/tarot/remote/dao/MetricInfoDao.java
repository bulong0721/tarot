package com.myee.tarot.remote.dao;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.metric.domain.MetricInfo;

import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
public interface MetricInfoDao extends GenericEntityDao<Long, MetricInfo> {
    List<MetricInfo> listBySystemMetricsId(Long systemMetricsId, List<String> metricsKeyListToSearch);

    List<MetricInfo> listByBoardNoPeriod(String boardNo, Long now, Long period, String nodeName, List<String> metricsKeyList);
}
