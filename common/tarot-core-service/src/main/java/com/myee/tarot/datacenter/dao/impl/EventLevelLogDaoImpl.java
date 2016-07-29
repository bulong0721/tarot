package com.myee.tarot.datacenter.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.datacenter.dao.EventLevelLogDao;
import com.myee.tarot.datacenter.domain.EventLevel;
import com.myee.tarot.datacenter.domain.QEventLevel;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Ray.Fu on 2016/7/19.
 */
@Repository
public class EventLevelLogDaoImpl extends GenericEntityDaoImpl<Long, EventLevel> implements EventLevelLogDao {

    @Override
    public List<EventLevel> getEventLevelList() {
        QEventLevel qEventLevel = QEventLevel.eventLevel;
        JPQLQuery<EventLevel> query = new JPAQuery(getEntityManager());
        query.from(qEventLevel);
        return query.fetch();
    }
}
