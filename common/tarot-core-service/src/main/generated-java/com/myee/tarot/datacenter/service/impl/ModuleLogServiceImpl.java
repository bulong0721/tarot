package com.myee.tarot.datacenter.service.impl;

import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.datacenter.dao.ModuleLogDao;
import com.myee.tarot.datacenter.service.ModuleLogService;
import com.myee.tarot.datacenter.domain.EventModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Ray.Fu on 2016/7/19.
 */
@Service
public class ModuleLogServiceImpl extends GenericEntityServiceImpl<Long, EventModule> implements ModuleLogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleLogServiceImpl.class);

    private ModuleLogDao moduleLogDao;

    @Autowired
    public ModuleLogServiceImpl(ModuleLogDao moduleLogDao) {
        super(moduleLogDao);
        this.moduleLogDao = moduleLogDao;
    }

    @Override
    public List listGroupByModuleId() {
        return moduleLogDao.listGroupByModuleId();
    }

    @Override
    public List listByModuleId(Integer moduleId) {
        return moduleLogDao.listByModuleId(moduleId);
    }
}
