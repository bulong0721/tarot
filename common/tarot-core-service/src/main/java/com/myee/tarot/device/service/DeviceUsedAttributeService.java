package com.myee.tarot.device.service;

import com.myee.tarot.catalog.domain.DeviceUsedAttribute;
import com.myee.tarot.core.service.GenericEntityService;

import java.util.List;

/**
 * Created by Chay on 2016/6/19.
 */
public interface DeviceUsedAttributeService extends GenericEntityService<Long, DeviceUsedAttribute> {
    List<DeviceUsedAttribute> listByDeviceUsedId(Long id);

    void deleteByDeviceUsedId(Long id);
}
