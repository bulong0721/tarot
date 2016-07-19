package com.myee.tarot.log.dao.impl;

import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.catalog.domain.QDeviceUsed;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.log.dao.SelfCheckLogDao;
import com.myee.tarot.log.domain.QSelfCheckLog;
import com.myee.tarot.log.domain.SelfCheckLog;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

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
    public PageResult<SelfCheckLog> pageAll(PageRequest pageRequest) {
        PageResult<SelfCheckLog> pageList = new PageResult<SelfCheckLog>();
        QSelfCheckLog qSelfCheckLog = QSelfCheckLog.selfCheckLog;
        JPQLQuery<SelfCheckLog> query = new JPAQuery(getEntityManager());
        pageList.setRecordsTotal(query.from(qSelfCheckLog).fetchCount());
        if( pageRequest.getCount() > 0){
            query.offset(pageRequest.getOffset()).limit(pageRequest.getCount());
        }
        pageList.setList(query.fetch());
        return pageList;
    }
}
