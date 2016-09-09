package com.myee.tarot.apiold.service;

import com.myee.tarot.apiold.domain.Evaluation;
import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;

/**
 * Created by Chay on 2016/8/10.
 */
public interface EvaluationService extends GenericEntityService<Long, Evaluation> {
    Evaluation getLatestByTableId(Long tableId);

    PageResult<Evaluation> pageList(PageRequest pageRequest);
}
