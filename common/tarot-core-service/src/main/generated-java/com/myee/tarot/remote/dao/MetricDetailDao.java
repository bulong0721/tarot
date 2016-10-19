package com.myee.tarot.remote.dao;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.metric.domain.MetricDetail;

import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
public interface MetricDetailDao extends GenericEntityDao<Long, MetricDetail> {
    MetricDetail findByKey(String name);

    List<String> listKey();
}
