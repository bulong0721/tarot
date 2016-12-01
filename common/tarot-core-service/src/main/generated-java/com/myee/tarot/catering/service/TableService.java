package com.myee.tarot.catering.service;

import com.myee.tarot.catering.domain.Table;
import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.WhereRequest;

import java.util.List;

/**
 * Created by Martin on 2016/4/11.
 */
public interface TableService extends GenericEntityService<Long, Table> {

    List<Table> listByStore(long storeId);

    PageResult<Table> pageByStore(Long id, WhereRequest whereRequest);
}
