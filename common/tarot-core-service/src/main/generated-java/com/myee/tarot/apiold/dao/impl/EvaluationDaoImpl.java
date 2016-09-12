package com.myee.tarot.apiold.dao.impl;

import com.myee.tarot.apiold.dao.EvaluationDao;
import com.myee.tarot.apiold.domain.Evaluation;
import com.myee.tarot.apiold.domain.QEvaluation;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.DateTimeUtils;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.WhereRequest;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;


/**
 * Created by Chay on 2016/8/10.
 */
@Repository
public class EvaluationDaoImpl extends GenericEntityDaoImpl<Long, Evaluation> implements EvaluationDao {

    public Evaluation getLatestByTableId(Long tableId) {
        QEvaluation qEvaluation = QEvaluation.evaluation;

        JPQLQuery<Evaluation> query = new JPAQuery(getEntityManager());

        query.from(qEvaluation)
                .where(qEvaluation.table.id.eq(tableId))
                .orderBy(qEvaluation.timeSecond.desc())
                .offset(0).limit(1);

        return query.fetchOne();
    }

    @Override
    public PageResult<Evaluation> pageList(WhereRequest whereRequest, int type) {
        PageResult<Evaluation> evaluationPageResult = new PageResult<Evaluation>();
        QEvaluation qEvaluation = QEvaluation.evaluation;
        JPQLQuery<Evaluation> query = new JPAQuery(getEntityManager()).from(qEvaluation);
        if (type != Constants.SUPERMAN_EVALUATION_AVG) {
            query.where(qEvaluation.active.eq(true));
        }
        if (whereRequest.getTableId() != null) {
            query.where(qEvaluation.table.id.eq(whereRequest.getTableId()));
        }
        if (whereRequest.getStoreId() != null) {
            query.where(qEvaluation.table.store.id.eq(whereRequest.getStoreId()));
        }
        String beginDate = DateTimeUtils.toShortDateTime(DateTimeUtils.getDateByStringEs(whereRequest.getBeginDate()));
        String endDate = DateTimeUtils.toShortDateTime(DateTimeUtils.getDateByStringEs(whereRequest.getEndDate()));
        if (whereRequest.getBeginDate() != null && whereRequest.getEndDate() != null && type != Constants.SUPERMAN_EVALUATION_AVG) {
            query.from(qEvaluation).where(qEvaluation.timeSecond.between(Long.valueOf(beginDate), Long.valueOf(endDate)));
        }
        query.orderBy(qEvaluation.id.desc());
        evaluationPageResult.setRecordsTotal(query.fetchCount());
        if (whereRequest.getCount() > 0 && type != Constants.SUPERMAN_EVALUATION_AVG) {
            query.offset(whereRequest.getOffset()).limit(whereRequest.getCount());
        }
        evaluationPageResult.setList(query.fetch());
        return evaluationPageResult;
    }
}
