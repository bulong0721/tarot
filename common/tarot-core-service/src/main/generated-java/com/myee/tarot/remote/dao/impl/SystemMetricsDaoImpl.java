package com.myee.tarot.remote.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.metric.domain.QSystemMetrics;
import com.myee.tarot.metric.domain.SystemMetrics;
import com.myee.tarot.remote.dao.SystemMetricsDao;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

/**
 * Created by Chay on 2016/8/10.
 */
@Repository
public class SystemMetricsDaoImpl extends GenericEntityDaoImpl<Long, SystemMetrics> implements SystemMetricsDao {

    public SystemMetrics getLatestByDUId(Long deviceUsedId){
        QSystemMetrics qSystemMetrics = QSystemMetrics.systemMetrics;
        JPQLQuery<SystemMetrics> query = new JPAQuery(getEntityManager());
        query.from(qSystemMetrics);
        if (deviceUsedId != null) {
            query.where(qSystemMetrics.deviceUsed.id.eq(deviceUsedId))
                .orderBy(qSystemMetrics.logTime.desc());
        }
        return query.fetchOne();
    }
}
