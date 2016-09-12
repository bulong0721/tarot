package com.myee.tarot.apiold.service.impl;

import com.myee.tarot.apiold.dao.EvaluationDao;
import com.myee.tarot.apiold.domain.Evaluation;
import com.myee.tarot.apiold.service.EvaluationService;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.WhereRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Chay on 2016/8/10.
 */
@Service
public class EvaluationServiceImpl extends GenericEntityServiceImpl<Long, Evaluation> implements EvaluationService {

    protected EvaluationDao evaluationDao;

    @Autowired
    public EvaluationServiceImpl(EvaluationDao evaluationDao) {
        super(evaluationDao);
        this.evaluationDao = evaluationDao;
    }

    @Override
    public Evaluation getLatestByTableId(Long tableId){
        return evaluationDao.getLatestByTableId(tableId);
    }

    @Override
    public PageResult<Evaluation> pageList(WhereRequest whereRequest, int type) {
        PageResult<Evaluation> evaluationPageResult = evaluationDao.pageList(whereRequest, type);
        return evaluationPageResult;
    }
}
