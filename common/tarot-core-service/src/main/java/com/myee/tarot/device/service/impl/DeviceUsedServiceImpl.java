package com.myee.tarot.device.service.impl;

import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.device.dao.DeviceUsedDao;
import com.myee.tarot.device.service.DeviceUsedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/5/31.
 */
@Service
public class DeviceUsedServiceImpl extends GenericEntityServiceImpl<Long, DeviceUsed>implements DeviceUsedService {

    protected DeviceUsedDao deviceUsedDao;

    @Autowired
    public DeviceUsedServiceImpl(DeviceUsedDao deviceUsedDao) {
        super(deviceUsedDao);
        this.deviceUsedDao = deviceUsedDao;
    }

    @Override
    public PageResult<DeviceUsed> pageListByStore(Long id,PageRequest pageRequest ){
        return deviceUsedDao.pageListByStore(id,pageRequest );
    }
}
