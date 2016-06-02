package com.myee.tarot.catering.dao;

import com.myee.tarot.catering.domain.TableType;
import com.myee.tarot.core.dao.GenericEntityDao;

import java.util.List;
import java.util.Set;

/**
 * Created by Martin on 2016/4/11.
 */
public interface TableTypeDao extends GenericEntityDao<Long, TableType> {

    List<TableType> queryByStore(long storeId);
}
