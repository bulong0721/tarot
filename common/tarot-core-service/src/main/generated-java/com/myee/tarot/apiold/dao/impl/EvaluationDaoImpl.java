package com.myee.tarot.apiold.dao.impl;

import com.myee.tarot.apiold.dao.EvaluationDao;
import com.myee.tarot.apiold.domain.Evaluation;
import com.myee.tarot.apiold.domain.QEvaluation;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

/**
 * Created by Chay on 2016/8/10.
 */
@Repository
public class EvaluationDaoImpl extends GenericEntityDaoImpl<Long, Evaluation> implements EvaluationDao {

    public Evaluation getLatestByTableId(Long tableId){
        QEvaluation qEvaluation = QEvaluation.evaluation;

        JPQLQuery<Evaluation> query = new JPAQuery(getEntityManager());

        query.from(qEvaluation)
                .where(qEvaluation.table.id.eq(tableId))
                .orderBy(qEvaluation.timeSecond.desc())
                .offset(0).limit(1);

        return query.fetchOne();
    }
}
