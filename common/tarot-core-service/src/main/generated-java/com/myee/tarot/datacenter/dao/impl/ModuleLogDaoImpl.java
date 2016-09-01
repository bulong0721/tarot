package com.myee.tarot.datacenter.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.datacenter.dao.ModuleLogDao;
import com.myee.tarot.datacenter.domain.EventModule;
import com.myee.tarot.datacenter.domain.QEventModule;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Ray.Fu on 2016/7/19.
 */
@Repository
public class ModuleLogDaoImpl extends GenericEntityDaoImpl<Long, EventModule> implements ModuleLogDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleLogDaoImpl.class);

    @Override
    public List listGroupByModuleId() {
        QEventModule qEventModule = QEventModule.eventModule;
        JPQLQuery<EventModule> query = new JPAQuery(getEntityManager());
        query.from(qEventModule).groupBy(qEventModule.moduleId);
        return query.fetch();
    }

    @Override
    public List<EventModule> listByModuleId(Integer moduleId) {
        QEventModule qEventModule = QEventModule.eventModule;
        JPQLQuery<EventModule> query = new JPAQuery(getEntityManager());
        query.from(qEventModule);
        if(moduleId != null) {
            query.where(qEventModule.moduleId.eq(moduleId));
        }
        return query.fetch();
    }
}
