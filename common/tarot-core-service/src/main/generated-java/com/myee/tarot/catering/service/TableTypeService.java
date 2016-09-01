package com.myee.tarot.catering.service;

import com.myee.tarot.catering.domain.TableType;
import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;

import java.util.List;

/**
 * Created by Martin on 2016/4/11.
 */
public interface TableTypeService extends GenericEntityService<Long, TableType> {

    List<TableType> listByStore(long storeId);

    PageResult<TableType> pageByStore(Long id, PageRequest pageRequest);
}
