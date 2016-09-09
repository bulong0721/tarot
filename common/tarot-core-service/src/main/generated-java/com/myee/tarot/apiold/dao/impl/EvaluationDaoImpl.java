package com.myee.tarot.apiold.dao.impl;

import com.myee.tarot.apiold.dao.EvaluationDao;
import com.myee.tarot.apiold.domain.Evaluation;
import com.myee.tarot.apiold.domain.QEvaluation;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

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
    public PageResult<Evaluation> pageList(PageRequest pageRequest) {
        PageResult<Evaluation> evaluationPageResult = new PageResult<>();
        QEvaluation qEvaluation = QEvaluation.evaluation;
        JPQLQuery<Evaluation> query = new JPAQuery(getEntityManager()).from(qEvaluation);
        query.where(qEvaluation.active.eq(true)).orderBy(qEvaluation.id.desc());
        if (pageRequest.getTableId() != null) {
            query.where(qEvaluation.table.id.eq(pageRequest.getTableId()));
        }
        if (pageRequest.getStoreId() != null) {
            query.where(qEvaluation.table.store.id.eq(pageRequest.getStoreId()));
        }
        if (pageRequest.getBegin() != null) {
            query.from(qEvaluation).where(qEvaluation.evaluCreated.after(pageRequest.getBegin()));
        }
        if (pageRequest.getEnd() != null) {
            query.from(qEvaluation).where(qEvaluation.evaluCreated.before(pageRequest.getEnd()));
        }
        evaluationPageResult.setRecordsTotal(query.fetchCount());
        if (pageRequest.getCount() > 0) {
            query.offset(pageRequest.getOffset()).limit(pageRequest.getCount());
        }
        evaluationPageResult.setList(query.fetch());
        return evaluationPageResult;
    }
}
