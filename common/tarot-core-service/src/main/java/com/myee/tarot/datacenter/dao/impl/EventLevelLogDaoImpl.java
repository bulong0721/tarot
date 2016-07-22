package com.myee.tarot.datacenter.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.datacenter.dao.EventLevelLogDao;
import com.myee.tarot.datacenter.domain.EventLevelLog;
import com.myee.tarot.datacenter.domain.QEventLevelLog;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Ray.Fu on 2016/7/19.
 */
@Repository
public class EventLevelLogDaoImpl extends GenericEntityDaoImpl<Long, EventLevelLog> implements EventLevelLogDao {

    @Override
    public List<EventLevelLog> getEventLevelList() {
        QEventLevelLog qEventLevelLog = QEventLevelLog.eventLevelLog;
        JPQLQuery<EventLevelLog> query = new JPAQuery(getEntityManager());
        query.from(qEventLevelLog);
        return query.fetch();
    }
}
