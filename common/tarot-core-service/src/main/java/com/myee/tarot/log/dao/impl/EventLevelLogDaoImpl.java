package com.myee.tarot.log.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.log.dao.EventLevelLogDao;
import com.myee.tarot.log.dao.SelfCheckLogDao;
import com.myee.tarot.log.domain.EventLevelLog;
import com.myee.tarot.log.domain.ModuleLog;
import com.myee.tarot.log.domain.QEventLevelLog;
import com.myee.tarot.log.domain.SelfCheckLog;
import com.myee.tarot.reference.domain.GeoZone;
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
