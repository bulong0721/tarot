package com.myee.tarot.device.dao.impl;

import com.myee.tarot.catalog.domain.Device;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.device.dao.DeviceDao;
import org.springframework.stereotype.Repository;

/**
 * Created by Administrator on 2016/5/31.
 */
@Repository
public class DeviceDaoImpl extends GenericEntityDaoImpl<Long, Device> implements DeviceDao{
}
