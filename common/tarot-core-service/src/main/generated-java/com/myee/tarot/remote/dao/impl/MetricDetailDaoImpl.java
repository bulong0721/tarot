package com.myee.tarot.remote.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.metric.domain.MetricDetail;
import com.myee.tarot.metric.domain.QMetricDetail;
import com.myee.tarot.remote.dao.MetricDetailDao;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
@Repository
public class MetricDetailDaoImpl extends GenericEntityDaoImpl<Long, MetricDetail> implements MetricDetailDao {
    public MetricDetail findByKey(String name){
        QMetricDetail qMetricDetail = QMetricDetail.metricDetail;
        JPQLQuery<MetricDetail> query = new JPAQuery(getEntityManager());
        query.from(qMetricDetail);
        if (name != null) {
            query.where(qMetricDetail.key.eq(name));
        }
        return query.fetchFirst();
    }

    public List<String> listKey(){
        QMetricDetail qMetricDetail = QMetricDetail.metricDetail;
        JPQLQuery<String> query = new JPAQuery(getEntityManager());
        query.select(qMetricDetail.name);
        query.from(qMetricDetail);

        return query.fetch();
    }
}
