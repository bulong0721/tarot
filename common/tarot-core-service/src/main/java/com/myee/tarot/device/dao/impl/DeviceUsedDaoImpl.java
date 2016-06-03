package com.myee.tarot.device.dao.impl;

import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.device.dao.DeviceUsedDao;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2016/5/31.
 */
@Repository
public class DeviceUsedDaoImpl extends GenericEntityDaoImpl<Long, DeviceUsed> implements DeviceUsedDao {
}
