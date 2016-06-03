package com.myee.tarot.catering.service;

import com.myee.tarot.catering.domain.TableType;
import com.myee.tarot.catering.domain.TableZone;
import com.myee.tarot.core.service.GenericEntityService;

import java.util.List;

/**
 * Created by Martin on 2016/4/11.
 */
public interface TableZoneService extends GenericEntityService<Long, TableZone> {

    List<TableZone> listByStore(long storeId);
}
