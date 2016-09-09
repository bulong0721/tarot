package com.myee.tarot.apiold.service;

import com.myee.tarot.apiold.domain.RollMain;
import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;

import java.util.Date;
import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
public interface RollMainService extends GenericEntityService<Long, RollMain> {
    List<RollMain> listByTypeStoreTime(Long storeId, int type,Date now);

    Long countByStore(Long id);

    PageResult<RollMain> pageByTypeStore(PageRequest pageRequest, Integer type, Long storeId);
}
