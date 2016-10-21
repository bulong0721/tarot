package com.myee.tarot.remote.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.metric.domain.QSystemMetrics;
import com.myee.tarot.metric.domain.SystemMetrics;
import com.myee.tarot.remote.dao.SystemMetricsDao;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
@Repository
public class SystemMetricsDaoImpl extends GenericEntityDaoImpl<Long, SystemMetrics> implements SystemMetricsDao {

    public SystemMetrics getLatestByBoardNo(String boardNo, String nodeName){
        QSystemMetrics qSystemMetrics = QSystemMetrics.systemMetrics;
        JPQLQuery<SystemMetrics> query = new JPAQuery(getEntityManager());
        query.from(qSystemMetrics);
        if(nodeName != null){
            query.where(qSystemMetrics.node.eq(nodeName));
        }
        if (boardNo != null) {
            query.where(qSystemMetrics.boardNo.eq(boardNo));
        }
        query.orderBy(qSystemMetrics.logTime.desc());
        return query.fetchFirst();
    }

    public SystemMetrics getByBoardNoLogTimeNod(String boardNo, long logTime, String nodeName){
        QSystemMetrics qSystemMetrics = QSystemMetrics.systemMetrics;
        JPQLQuery<SystemMetrics> query = new JPAQuery(getEntityManager());
        query.from(qSystemMetrics);
        query.where(qSystemMetrics.logTime.eq(new Date(logTime)));

        if(nodeName != null){
            query.where(qSystemMetrics.node.eq(nodeName));
        }
        if (boardNo != null) {
            query.where(qSystemMetrics.boardNo.eq(boardNo));
        }
        query.orderBy(qSystemMetrics.logTime.desc());
        return query.fetchFirst();
    }

    public List<SystemMetrics> listByBoardNoPeriod(String boardNo, Long now, Long period, String nodeName){
        QSystemMetrics qSystemMetrics = QSystemMetrics.systemMetrics;
        JPQLQuery<SystemMetrics> query = new JPAQuery(getEntityManager());
        query.from(qSystemMetrics);
        if (boardNo != null) {
            query.where(qSystemMetrics.boardNo.eq(boardNo));
        }
        if(nodeName != null) {
            query.where(qSystemMetrics.node.eq(nodeName));
        }
        if(period != null){
            Long from = now - period;
            query.where(qSystemMetrics.logTime.between(new Date(from),new Date(now)));
        }
        query.orderBy(qSystemMetrics.logTime.desc());
        return query.fetch();
    }
}
