package com.myee.tarot.catering.service.impl;

import com.myee.tarot.catering.dao.TableTypeDao;
import com.myee.tarot.catering.domain.TableType;
import com.myee.tarot.catering.service.TableTypeService;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<TableType> listByStore(long storeId) {
        return tableTypeDao.listByStore(storeId);
    }

    @Override
    public PageResult<TableType> pageByStore(Long id, PageRequest pageRequest){
        return tableTypeDao.pageByStore(id, pageRequest);
    }

    @Override
    public TableType findByStoreIdAndName(Long id, String name) {
        return tableTypeDao.findByStoreIdAndName(id, name);
    }
}
