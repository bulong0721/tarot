package com.myee.tarot.remote.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.metric.domain.MetricInfo;
import com.myee.tarot.metric.domain.QMetricInfo;
import com.myee.tarot.remote.dao.MetricInfoDao;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
@Repository
public class MetricInfoDaoImpl extends GenericEntityDaoImpl<Long, MetricInfo> implements MetricInfoDao {
    public List<MetricInfo> listBySystemMetricsId(Long systemMetricsId, List<String> metricsKeyListToSearch) {
        QMetricInfo qMetricInfo = QMetricInfo.metricInfo;
        JPAQuery<MetricInfo> query = new JPAQuery<>(getEntityManager());
        query.from(qMetricInfo);
        if(systemMetricsId != null){
            query.where(qMetricInfo.systemMetricsId.eq(systemMetricsId));
        }
        if (metricsKeyListToSearch != null) {
            query.where(qMetricInfo.keyName.in(metricsKeyListToSearch));

        }
        query.orderBy(qMetricInfo.logTime.desc());
        return query.fetch();
    }

    public List<MetricInfo> listByBoardNoPeriod(String boardNo, Long now, Long period, String nodeName, List<String> metricsKeyList) {
        QMetricInfo qMetricInfo = QMetricInfo.metricInfo;
        JPAQuery<MetricInfo> query = new JPAQuery<>(getEntityManager());
        query.from(qMetricInfo);
        if (boardNo != null) {
            query.where(qMetricInfo.boardNo.eq(boardNo));
        }
        if(nodeName != null) {
            query.where(qMetricInfo.node.eq(nodeName));
        }
        if(period != null){
            Long from = now - period;
            query.where(qMetricInfo.logTime.between(new Date(from),new Date(now)));
        }
        if(metricsKeyList != null){
            query.where(qMetricInfo.keyName.in(metricsKeyList));
        }
        query.orderBy(qMetricInfo.logTime.asc());//前端显示指标详细图需要时间从小到大排序
        return query.fetch();
    }

    @Override
    public void deleteByTime(Date date) {
        QMetricInfo qMetricInfo = QMetricInfo.metricInfo;
        new JPADeleteClause(getEntityManager(), qMetricInfo).where(qMetricInfo.created.before(date)).execute();
    }

    @Override
    public List<MetricInfo> listByCreateTime(Date date) {
        QMetricInfo qMetricInfo = QMetricInfo.metricInfo;
        JPAQuery<MetricInfo> query = new JPAQuery<>(getEntityManager());
        query.from(qMetricInfo);
        if (date != null) {
            query.where(qMetricInfo.created.before(date));
        }
        return query.fetch();
    }


}
