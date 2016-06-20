package com.myee.tarot.device.dao;

import com.myee.tarot.catalog.domain.DeviceUsedAttribute;
import com.myee.tarot.core.dao.GenericEntityDao;

import java.util.List;

/**
 * Created by Chay on 2016/6/19.
 */
public interface DeviceUsedAttributeDao extends GenericEntityDao<Long, DeviceUsedAttribute> {
    List<DeviceUsedAttribute> listByDeviceUsedId(Long id);

    void deleteByDeviceUsedId(Long id);
}
