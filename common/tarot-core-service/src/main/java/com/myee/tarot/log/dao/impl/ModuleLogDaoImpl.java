package com.myee.tarot.log.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.log.dao.EventLevelLogDao;
import com.myee.tarot.log.dao.ModuleLogDao;
import com.myee.tarot.log.domain.EventLevelLog;
import com.myee.tarot.log.domain.ModuleLog;
import com.myee.tarot.log.domain.QEventLevelLog;
import com.myee.tarot.log.domain.QModuleLog;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Ray.Fu on 2016/7/19.
 */
@Repository
public class ModuleLogDaoImpl extends GenericEntityDaoImpl<Long, ModuleLog> implements ModuleLogDao {

    @Override
    public List getModuleList() {
        QModuleLog qModuleLog = QModuleLog.moduleLog;
        JPQLQuery<ModuleLog> query = new JPAQuery(getEntityManager());
        query.from(qModuleLog).groupBy(qModuleLog.moduleId);
        return query.fetch();
    }

    @Override
    public List<ModuleLog> getFunctionListByModule(Integer moduleId) {
        QModuleLog qModuleLog = QModuleLog.moduleLog;
        JPQLQuery<ModuleLog> query = new JPAQuery(getEntityManager());
        query.from(qModuleLog);
        if(moduleId != null) {
            query.where(qModuleLog.moduleId.eq(moduleId));
        }
        return query.fetch();
    }
}
