package com.myee.tarot.catalog.dao;

import com.myee.tarot.catalog.domain.Device;
import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;

/**
 * Created by Administrator on 2016/5/31.
 */
public interface DeviceDao  extends GenericEntityDao<Long, Device> {
    PageResult<Device> pageList(PageRequest pageRequest);
}
