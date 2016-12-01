package com.myee.tarot.catalog.service;

import com.myee.tarot.catalog.domain.Device;
import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.WhereRequest;

/**
 * Created by Administrator on 2016/5/31.
 */
public interface DeviceService extends GenericEntityService<Long, Device> {
    PageResult<Device> pageList(WhereRequest whereRequest);
}
