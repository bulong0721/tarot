package com.myee.tarot.remote.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.metric.domain.AppInfo;
import com.myee.tarot.metric.domain.QAppInfo;
import com.myee.tarot.remote.dao.AppInfoDao;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
@Repository
public class AppInfoDaoImpl extends GenericEntityDaoImpl<Long, AppInfo> implements AppInfoDao {

    @Override
    public List<AppInfo> listBySystemMetricsId(Long systemMetricsId){
        QAppInfo qAppInfo = QAppInfo.appInfo;
        JPAQuery<AppInfo> query = new JPAQuery<>(getEntityManager());
        query.from(qAppInfo);
        if(systemMetricsId != null){
            query.where(qAppInfo.systemMetricsId.eq(systemMetricsId));
        }

        query.orderBy(qAppInfo.logTime.desc());
        return query.fetch();
    }

    @Override
    public List<AppInfo> listByBoardNoPeriod(String boardNo, Long now, Long period, String nodeName){
        QAppInfo qAppInfo = QAppInfo.appInfo;
        JPAQuery<AppInfo> query = new JPAQuery<>(getEntityManager());
        query.from(qAppInfo);
        if (boardNo != null) {
            query.where(qAppInfo.boardNo.eq(boardNo));
        }
        if(nodeName != null) {
            query.where(qAppInfo.node.eq(nodeName));
        }
        if(period != null){
            Long from = now - period;
            query.where(qAppInfo.logTime.between(new Date(from),new Date(now)));
        }
        query.orderBy(qAppInfo.logTime.desc());
        return query.fetch();
    }

    @Override
    public List<AppInfo> listByCreateTime(Date date) {
        QAppInfo qAppInfo = QAppInfo.appInfo;
        JPAQuery<AppInfo> query = new JPAQuery<>(getEntityManager());
        query.from(qAppInfo);
        if (date != null) {
            query.where(qAppInfo.created.before(date));
        }
        return query.fetch();
    }

    @Override
    public void deleteByTime(Date date) {
        QAppInfo qAppInfo = QAppInfo.appInfo;
        new JPADeleteClause(getEntityManager(), qAppInfo).where(qAppInfo.created.before(date)).execute();
    }
}
