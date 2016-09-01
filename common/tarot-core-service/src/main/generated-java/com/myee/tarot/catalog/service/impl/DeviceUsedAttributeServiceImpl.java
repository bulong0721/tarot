package com.myee.tarot.catalog.service.impl;

import com.myee.tarot.catalog.domain.DeviceUsedAttribute;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.catalog.dao.DeviceUsedAttributeDao;
import com.myee.tarot.catalog.service.DeviceUsedAttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016/5/31.
 */
@Service
public class DeviceUsedAttributeServiceImpl extends GenericEntityServiceImpl<Long, DeviceUsedAttribute>implements DeviceUsedAttributeService {

    protected DeviceUsedAttributeDao deviceUsedAttributeDao;

    @Autowired
    public DeviceUsedAttributeServiceImpl(DeviceUsedAttributeDao deviceUsedAttributeDao) {
        super(deviceUsedAttributeDao);
        this.deviceUsedAttributeDao = deviceUsedAttributeDao;
    }

    @Override
    public List<DeviceUsedAttribute> listByDeviceUsedId(Long id){
        return deviceUsedAttributeDao.listByDeviceUsedId(id);
    }

    public void deleteByDeviceUsedId(Long id){
        deviceUsedAttributeDao.deleteByDeviceUsedId(id);
    }
}
