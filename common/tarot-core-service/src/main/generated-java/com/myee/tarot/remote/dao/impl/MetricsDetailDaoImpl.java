package com.myee.tarot.remote.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.metrics.domain.MetricsDetail;
import com.myee.tarot.metrics.domain.MetricsInfo;
import com.myee.tarot.metrics.domain.QMetricsDetail;
import com.myee.tarot.remote.dao.MetricsDetailDao;
import com.myee.tarot.remote.dao.MetricsInfoDao;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

/**
 * Created by Chay on 2016/8/10.
 */
@Repository
public class MetricsDetailDaoImpl extends GenericEntityDaoImpl<Long, MetricsDetail> implements MetricsDetailDao {
    public MetricsDetail findByKey(String name){
        QMetricsDetail qMetricsDetail = QMetricsDetail.metricsDetail;
        JPQLQuery<MetricsDetail> query = new JPAQuery(getEntityManager());
        query.from(qMetricsDetail);
        if (name != null) {
            query.where(qMetricsDetail.key.eq(name));
        }
        return query.fetchFirst();
    }
}
