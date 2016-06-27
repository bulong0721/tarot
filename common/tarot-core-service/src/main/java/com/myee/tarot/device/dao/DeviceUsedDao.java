package com.myee.tarot.device.dao;

import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;

/**
 * Created by Administrator on 2016/5/31.
 */
public interface DeviceUsedDao extends GenericEntityDao<Long, DeviceUsed> {
    PageResult<DeviceUsed> pageListByStore(Long id,PageRequest pageRequest );
}
