package com.myee.tarot.device.dao.impl;

import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.catalog.domain.VoiceLog;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.device.dao.DeviceUsedDao;
import com.myee.tarot.device.dao.VoiceLogDao;
import org.springframework.stereotype.Repository;

/**
 * Created by Ray.Fu on 2016/8/11.
 */
@Repository
public class VoiceLogDaoImpl extends GenericEntityDaoImpl<Long, VoiceLog> implements VoiceLogDao {
}
