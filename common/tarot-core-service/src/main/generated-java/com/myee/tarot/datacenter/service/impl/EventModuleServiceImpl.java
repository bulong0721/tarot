package com.myee.tarot.datacenter.service.impl;

import com.myee.tarot.datacenter.domain.EventModule;
import com.myee.tarot.datacenter.dao.EventModuleDao;
import com.myee.tarot.datacenter.service.EventModuleService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.myee.tarot.core.service.GenericEntityServiceImpl;

@Service
public class EventModuleServiceImpl extends GenericEntityServiceImpl<java.lang.Long, EventModule> implements EventModuleService {

    protected EventModuleDao dao;

    @Autowired
    public EventModuleServiceImpl(EventModuleDao dao) {
        super(dao);
        this.dao = dao;
    }

}

