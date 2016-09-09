package com.myee.tarot.apiold.dao;

import com.myee.tarot.apiold.domain.Evaluation;
import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import java.util.Collection;

/**
 * Created by Chay on 2016/8/10.
 */
public interface EvaluationDao extends GenericEntityDao<Long, Evaluation> {
    Evaluation getLatestByTableId(Long tableId);

    PageResult<Evaluation> pageList(PageRequest pageRequest, int type);
}
