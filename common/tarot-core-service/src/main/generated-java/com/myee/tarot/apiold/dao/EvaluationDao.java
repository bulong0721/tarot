package com.myee.tarot.apiold.dao;

import com.myee.tarot.apiold.domain.Evaluation;
import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.WhereRequest;

/**
 * Created by Chay on 2016/8/10.
 */
public interface EvaluationDao extends GenericEntityDao<Long, Evaluation> {
    Evaluation getLatestByTableId(Long tableId);

    PageResult<Evaluation> pageList(WhereRequest whereRequest, int type);
}
