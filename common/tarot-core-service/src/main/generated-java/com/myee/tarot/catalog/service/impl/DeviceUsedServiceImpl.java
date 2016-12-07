package com.myee.tarot.catalog.service.impl;

import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.catalog.dao.DeviceUsedDao;
import com.myee.tarot.catalog.service.DeviceUsedService;
import com.myee.tarot.core.util.WhereRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public PageResult<DeviceUsed> pageByStore(Long id,WhereRequest whereRequest ){
        return deviceUsedDao.pageByStore(id,whereRequest );
    }

    @Override
    public List<DeviceUsed> listByIDs(List<Long> bindList){
        return deviceUsedDao.listByIDs(bindList);
    }

    @Override
    public DeviceUsed getByBoardNo(String mainBoardCode) {
        return deviceUsedDao.getStoreInfoByMbCode(mainBoardCode);
    }

    @Override
    public DeviceUsed findByStoreIdAndName(Long storeId, String deviceUsedName) {
        return deviceUsedDao.findByStoreIdAndName(storeId, deviceUsedName);
    }
}
