package com.myee.tarot.apiold.service.impl;

import com.myee.tarot.apiold.dao.TablePhoneDao;
import com.myee.tarot.apiold.domain.TablePhone;
import com.myee.tarot.apiold.service.TablePhoneService;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xiaoni on 2016/8/10.
 */
@Service
public class TablePhoneServiceImpl extends GenericEntityServiceImpl<Long, TablePhone> implements TablePhoneService {

    protected TablePhoneDao tablePhoneDao;

    @Autowired
    public TablePhoneServiceImpl(TablePhoneDao tablePhoneDao) {
        super(tablePhoneDao);
        this.tablePhoneDao = tablePhoneDao;
    }

    public TablePhone findByTableId(Long tableId){
        return tablePhoneDao.findByTableId(tableId);
    }

    public PageResult<TablePhone> pageByStore(Long id, PageRequest pageRequest){
        return tablePhoneDao.pageByStore(id,  pageRequest);
    }
}
