package com.myee.tarot.apiold.dao.impl;

import com.myee.tarot.apiold.dao.UploadAccessLogDao;
import com.myee.tarot.apiold.domain.QUploadAccessAction;
import com.myee.tarot.apiold.domain.QUploadAccessLog;
import com.myee.tarot.apiold.domain.UploadAccessLog;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.DateTimeUtils;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.StringUtil;
import com.myee.tarot.core.util.WhereRequest;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

/**
 * Created by Chay on 2016/8/10.
 */
@Repository
public class UploadAccessLogDaoImpl extends GenericEntityDaoImpl<Long, UploadAccessLog> implements UploadAccessLogDao {
    @Override
    public PageResult<UploadAccessLog> getLevel1Analysis(WhereRequest whereRequest) {
        PageResult<UploadAccessLog> uploadAccessLogPageResult = new PageResult<UploadAccessLog>();
        QUploadAccessLog qUploadAccessLog = QUploadAccessLog.uploadAccessLog;
        QUploadAccessAction qUploadAccessAction = QUploadAccessAction.uploadAccessAction;
        JPQLQuery<UploadAccessLog> query = new JPAQuery(getEntityManager()).from(qUploadAccessLog);
        if (whereRequest.getGroup() != null && whereRequest.getGroup().equals("0")) {
            query.select(qUploadAccessAction.name, qUploadAccessLog.timeStay.sum().as("timeStay"))
                    .where(qUploadAccessAction.active.eq(true)).from(qUploadAccessLog).leftJoin(qUploadAccessAction).on(qUploadAccessLog.actionId.eq(qUploadAccessAction.id)).groupBy(qUploadAccessAction.name);
        }
        if (StringUtil.isNullOrEmpty(whereRequest.getBeginDate(), true) && StringUtil.isNullOrEmpty(whereRequest.getEndDate(), true)) {
            query.where(qUploadAccessLog.timePoit.between(DateTimeUtils.getDateByString(whereRequest.getBeginDate()), DateTimeUtils.getDateByString(whereRequest.getEndDate())));
        }
        uploadAccessLogPageResult.setRecordsTotal(query.fetchCount());
        uploadAccessLogPageResult.setList(query.fetch());
        return uploadAccessLogPageResult;
    }
}
