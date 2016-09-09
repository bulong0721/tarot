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
    public Collection getFeelAverage(PageRequest pageRequest) {
        QEvaluation qEvaluation = QEvaluation.evaluation;
        JPQLQuery<Evaluation> query = new JPAQuery(getEntityManager()).select(
                qEvaluation.feelEnvironment.avg().divide(2), qEvaluation.feelWhole.avg().divide(2), qEvaluation.feelService.avg().divide(2), qEvaluation.feelFlavor.avg().divide(2)).from(qEvaluation);

        Long tableId = (Long) pageRequest.getFilter().get("tableId");
        Date begin = (Date) pageRequest.getFilter().get("begin");
        Date end = (Date) pageRequest.getFilter().get("end");

        if (tableId != null) {
            query.where(qEvaluation.table.id.eq(tableId));
        }
        if (pageRequest.getCount() > 0) {
            query.offset(pageRequest.getOffset()).limit(pageRequest.getCount());
        }
        if (begin != null) {
            query.from(qEvaluation).where(qEvaluation.evaluCreated.after(begin));
        }
        if (end != null) {
            query.from(qEvaluation).where(qEvaluation.evaluCreated.before(end));
        }
        query.groupBy(qEvaluation.feelEnvironment, qEvaluation.feelWhole, qEvaluation.feelService, qEvaluation.feelFlavor);
        return query.fetch();
    }

    @Override
    public PageResult<Evaluation> listInPage(PageRequest pageRequest) {
        PageResult<Evaluation> evaluationPageResult = new PageResult<>();
        QEvaluation qEvaluation = QEvaluation.evaluation;
        JPQLQuery<Evaluation> query = new JPAQuery(getEntityManager()).from(qEvaluation);
        Long tableId = (Long) pageRequest.getFilter().get("tableId");
        Date begin = (Date) pageRequest.getFilter().get("begin");
        Date end = (Date) pageRequest.getFilter().get("end");
        query.where(qEvaluation.active.eq(true)).orderBy(qEvaluation.id.desc());
        if (tableId != null) {
            query.where(qEvaluation.table.id.eq(tableId));
        }
        if (begin != null) {
            query.from(qEvaluation).where(qEvaluation.evaluCreated.after(begin));
        }
        if (end != null) {
            query.from(qEvaluation).where(qEvaluation.evaluCreated.before(end));
        }
        evaluationPageResult.setRecordsTotal(query.fetchCount());
        if (pageRequest.getCount() > 0) {
            query.offset(pageRequest.getOffset()).limit(pageRequest.getCount());
        }
        evaluationPageResult.setList(query.fetch());
        return evaluationPageResult;
    }
}
