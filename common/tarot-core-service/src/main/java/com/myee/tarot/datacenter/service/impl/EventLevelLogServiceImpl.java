package com.myee.tarot.datacenter.service.impl;

import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.datacenter.dao.EventLevelLogDao;
import com.myee.tarot.datacenter.domain.EventLevel;
import com.myee.tarot.datacenter.service.EventLevelLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Ray.Fu on 2016/7/19.
 */
@Service
public class EventLevelLogServiceImpl extends GenericEntityServiceImpl<Long, EventLevel> implements EventLevelLogService {

    private EventLevelLogDao eventLevelLogDao;

    @Autowired
    public EventLevelLogServiceImpl(EventLevelLogDao eventLevelLogDao) {
        super(eventLevelLogDao);
        this.eventLevelLogDao = eventLevelLogDao;
    }

    @Override
    public List getEventLevelList() {
        return eventLevelLogDao.getEventLevelList();
    }
}
