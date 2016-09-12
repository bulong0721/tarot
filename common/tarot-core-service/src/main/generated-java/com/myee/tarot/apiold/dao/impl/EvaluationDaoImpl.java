package com.myee.tarot.apiold.dao.impl;

import com.myee.tarot.apiold.dao.EvaluationDao;
import com.myee.tarot.apiold.domain.Evaluation;
import com.myee.tarot.apiold.domain.QEvaluation;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.DateTimeUtils;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

/**
 * Created by Chay on 2016/8/10.
 */
@Repository
public class EvaluationDaoImpl extends GenericEntityDaoImpl<Long, Evaluation> implements EvaluationDao {

    private static final int AVG = 1;

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
    public PageResult<Evaluation> pageList(PageRequest pageRequest, int type) {
        PageResult<Evaluation> evaluationPageResult = new PageResult<Evaluation>();
        QEvaluation qEvaluation = QEvaluation.evaluation;
        JPQLQuery<Evaluation> query = new JPAQuery(getEntityManager()).from(qEvaluation);
        if (type != AVG) {
            query.where(qEvaluation.active.eq(true));
        }
        if (pageRequest.getTableId() != null) {
            query.where(qEvaluation.table.id.eq(pageRequest.getTableId()));
        }
        if (pageRequest.getStoreId() != null) {
            query.where(qEvaluation.table.store.id.eq(pageRequest.getStoreId()));
        }
        if (pageRequest.getBegin() != null && pageRequest.getEnd() != null && type != AVG) {
            query.from(qEvaluation).where(qEvaluation.timeSecond.between(DateTimeUtils.toShortDateTimeL(pageRequest.getBegin()), DateTimeUtils.toShortDateTimeL(pageRequest.getEnd())));
        }
        query.orderBy(qEvaluation.id.desc());
        evaluationPageResult.setRecordsTotal(query.fetchCount());
        if (pageRequest.getCount() > 0 && type != AVG) {
            query.offset(pageRequest.getOffset()).limit(pageRequest.getCount());
        }
        evaluationPageResult.setList(query.fetch());
        return evaluationPageResult;
    }
}
