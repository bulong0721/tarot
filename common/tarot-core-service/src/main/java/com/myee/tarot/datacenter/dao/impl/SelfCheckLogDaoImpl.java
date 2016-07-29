package com.myee.tarot.datacenter.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.StringUtil;
import com.myee.tarot.core.util.WhereRequest;
import com.myee.tarot.datacenter.dao.SelfCheckLogDao;
import com.myee.tarot.datacenter.domain.EventLevel;
import com.myee.tarot.datacenter.domain.QSelfCheckLog;
import com.myee.tarot.datacenter.domain.SelfCheckLog;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by Ray.Fu on 2016/7/18.
 */
@Repository
public class SelfCheckLogDaoImpl extends GenericEntityDaoImpl<Long, SelfCheckLog> implements SelfCheckLogDao {

    @Override
    public void uploadSelfCheckLog(SelfCheckLog selfCheckLog) {
        this.update(selfCheckLog);
    }

    @Override
    public PageResult<SelfCheckLog> pageAll(WhereRequest whereRequest) {

        PageResult<SelfCheckLog> pageList = new PageResult<SelfCheckLog>();
        QSelfCheckLog qSelfCheckLog = QSelfCheckLog.selfCheckLog;
        JPQLQuery<SelfCheckLog> query = new JPAQuery(getEntityManager());
        query.from(qSelfCheckLog)
                .leftJoin(qSelfCheckLog.eventModule)
                .fetchJoin();
        String eventLevel = whereRequest.getEventLevel();
        if(StringUtils.isNotBlank(eventLevel)) {
            query.where(qSelfCheckLog.eventLevel.eq(eventLevel));
        }
        Map moduleObjectMap = StringUtil.transStringToMap(whereRequest.getModuleObject());
        if(moduleObjectMap != null) {
            Integer moduleId = (Integer)moduleObjectMap.get("value");
            query.where(qSelfCheckLog.moduleId.eq(moduleId));
        }
        Map functionObjectMap = StringUtil.transStringToMap(whereRequest.getFunctionObject());
        if(functionObjectMap != null) {
            Integer functionId = (Integer)functionObjectMap.get("value");
            query.where(qSelfCheckLog.functionId.eq(functionId));
        }
        pageList.setRecordsTotal(query.fetchCount());
        if( whereRequest.getCount() > 0){
            query.offset(whereRequest.getOffset()).limit(whereRequest.getCount());
        }
        pageList.setList(query.fetch());
        return pageList;
    }
}
