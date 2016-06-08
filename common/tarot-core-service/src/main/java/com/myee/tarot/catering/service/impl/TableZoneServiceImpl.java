package com.myee.tarot.catering.service.impl;

import com.myee.tarot.catering.dao.TableZoneDao;
import com.myee.tarot.catering.domain.TableZone;
import com.myee.tarot.catering.service.TableZoneService;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Martin on 2016/4/21.
 */
@Service
public class TableZoneServiceImpl extends GenericEntityServiceImpl<Long, TableZone> implements TableZoneService {

    protected TableZoneDao tableZoneDao;

    @Autowired
    public TableZoneServiceImpl(TableZoneDao tableZoneDao) {
        super(tableZoneDao);
        this.tableZoneDao = tableZoneDao;
    }

    @Override
    public List<TableZone> listByStore(long storeId) {
        return tableZoneDao.listByStore(storeId);
    }
}
