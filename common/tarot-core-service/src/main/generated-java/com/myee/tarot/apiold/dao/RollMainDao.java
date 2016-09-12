package com.myee.tarot.apiold.dao;

import com.myee.tarot.apiold.domain.RollMain;
import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;

import java.util.Date;
import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
public interface RollMainDao extends GenericEntityDao<Long, RollMain> {
    List<RollMain> listByTypeStoreTime(Long storeId, int type,Date now);

    Long countByStore(Long id);

    PageResult<RollMain> pageByTypeStore(PageRequest pageRequest, Integer type, Long storeId);
}
