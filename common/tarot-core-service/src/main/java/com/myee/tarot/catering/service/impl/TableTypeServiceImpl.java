package com.myee.tarot.catering.service.impl;

import com.myee.tarot.admin.dao.RoleDao;
import com.myee.tarot.catering.dao.TableTypeDao;
import com.myee.tarot.catering.domain.TableType;
import com.myee.tarot.catering.service.TableTypeService;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Martin on 2016/4/21.
 */
@Service
public class TableTypeServiceImpl extends GenericEntityServiceImpl<Long, TableType> implements TableTypeService {

    protected TableTypeDao tableTypeDao;

    @Autowired
    public TableTypeServiceImpl(TableTypeDao tableTypeDao) {
        super(tableTypeDao);
        this.tableTypeDao = tableTypeDao;
    }
}
