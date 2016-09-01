package com.myee.tarot.catalog.service.impl;

import com.myee.tarot.catalog.domain.Device;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.catalog.dao.DeviceDao;
import com.myee.tarot.catalog.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/5/31.
 */
@Service
public class DeviceServiceImpl extends GenericEntityServiceImpl<Long, Device>implements DeviceService{

    protected DeviceDao deviceDao;

    @Autowired
    public DeviceServiceImpl(DeviceDao deviceDao) {
        super(deviceDao);
        this.deviceDao = deviceDao;
    }

    @Override
    public PageResult<Device> pageList(PageRequest pageRequest) {
        return deviceDao.pageList(pageRequest);
    }
}
