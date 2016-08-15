package com.myee.tarot.apiold.dao;

import com.myee.tarot.apiold.domain.TablePhone;
import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;

/**
 * Created by xiaoni on 2016/8/10.
 */
public interface TablePhoneDao extends GenericEntityDao<Long, TablePhone> {
    TablePhone findByTableId(Long tableId);

    PageResult<TablePhone> pageByStore(Long id, PageRequest pageRequest);
}
