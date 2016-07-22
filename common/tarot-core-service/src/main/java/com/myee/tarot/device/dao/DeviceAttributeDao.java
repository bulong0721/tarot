package com.myee.tarot.device.dao;

import com.myee.tarot.catalog.domain.Device;
import com.myee.tarot.catalog.domain.DeviceAttribute;
import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;

import java.util.List;

/**
 * Created by Chay on 2016/6/19.
 */
public interface DeviceAttributeDao extends GenericEntityDao<Long, DeviceAttribute> {
    List<DeviceAttribute> listByDeviceId(Long id);

    void deleteByDeviceId(Long id);
}
