package com.myee.tarot.catering.dao;

import com.myee.tarot.catering.domain.TableZone;
import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;

import java.util.List;

/**
 * Created by Martin on 2016/4/11.
 */
public interface TableZoneDao extends GenericEntityDao<Long, TableZone> {

    List<TableZone> listByStore(long storeId);

    PageResult<TableZone> pageByStore(Long id, PageRequest pageRequest);
}
