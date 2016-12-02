package com.myee.tarot.catering.service.impl;

import com.myee.tarot.catering.dao.TableDao;
import com.myee.tarot.catering.domain.Table;
import com.myee.tarot.catering.service.TableService;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.WhereRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Martin on 2016/4/21.
 */
@Service
public class TableServiceImpl extends GenericEntityServiceImpl<Long, Table> implements TableService {

    protected TableDao tableDao;

    @Autowired
    public TableServiceImpl(TableDao tableDao) {
        super(tableDao);
        this.tableDao = tableDao;
    }

    @Override
    public List<Table> listByStore(long storeId) {
        return tableDao.listByStore(storeId);
    }

    @Override
    public PageResult<Table> pageByStore(Long id, WhereRequest whereRequest){
        return tableDao.pageByStore(id, whereRequest);
    }
}
