package com.myee.tarot.catering.dao;

import com.myee.tarot.catering.domain.Table;
import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;

import java.util.List;

/**
 * Created by Martin on 2016/4/11.
 */
public interface TableDao extends GenericEntityDao<Long, Table> {

    List<Table> listByStore(long storeId);

    PageResult<Table> pageByStore(Long id, PageRequest pageRequest);
}
