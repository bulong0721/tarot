package com.myee.tarot.catering.dao;

import com.myee.tarot.catering.domain.TableType;
import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;

import java.util.List;

/**
 * Created by Martin on 2016/4/11.
 */
public interface TableTypeDao extends GenericEntityDao<Long, TableType> {

    List<TableType> listByStore(long storeId);

    PageResult<TableType> pageByStore(Long id, PageRequest pageRequest);
}
