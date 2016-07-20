package com.myee.tarot.log.service;

import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.log.domain.EventLevelLog;
import com.myee.tarot.log.domain.SelfCheckLog;

import java.util.List;

/**
 * Created by Ray.Fu on 2016/7/19.
 */
public interface EventLevelLogService extends GenericEntityService<Long, EventLevelLog> {

    public List getEventLevelList();
}
