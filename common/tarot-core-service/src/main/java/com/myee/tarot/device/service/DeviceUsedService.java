package com.myee.tarot.device.service;

import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;

/**
 * Created by Administrator on 2016/5/31.
 */
public interface DeviceUsedService extends GenericEntityService<Long, DeviceUsed> {

    PageResult<DeviceUsed> pageList(PageRequest pageRequest);

    PageResult<DeviceUsed> pageListByStore(PageRequest pageRequest, Long id);
}
