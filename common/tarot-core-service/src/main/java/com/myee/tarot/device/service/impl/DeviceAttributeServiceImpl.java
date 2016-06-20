package com.myee.tarot.device.service.impl;

import com.myee.tarot.catalog.domain.Device;
import com.myee.tarot.catalog.domain.DeviceAttribute;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.device.dao.DeviceAttributeDao;
import com.myee.tarot.device.dao.DeviceDao;
import com.myee.tarot.device.service.DeviceAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016/5/31.
 */
@Service
public class DeviceAttributeServiceImpl extends GenericEntityServiceImpl<Long, DeviceAttribute>implements DeviceAttributeService {

    protected DeviceAttributeDao deviceAttributeDao;

    @Autowired
    public DeviceAttributeServiceImpl(DeviceAttributeDao deviceAttributeDao) {
        super(deviceAttributeDao);
        this.deviceAttributeDao = deviceAttributeDao;
    }

    @Override
    public List<DeviceAttribute> listByDeviceId(Long id){
        return deviceAttributeDao.listByDeviceId(id);
    }
}
