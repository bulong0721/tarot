package com.myee.tarot.remote.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.metric.domain.MetricInfo;
import com.myee.tarot.remote.dao.MetricsInfoDao;
import org.springframework.stereotype.Repository;

/**
 * Created by Chay on 2016/8/10.
 */
@Repository
public class MetricsInfoDaoImpl extends GenericEntityDaoImpl<Long, MetricInfo> implements MetricsInfoDao {
}