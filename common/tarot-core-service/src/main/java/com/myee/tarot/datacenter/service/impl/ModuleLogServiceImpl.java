package com.myee.tarot.datacenter.service.impl;

import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.datacenter.dao.ModuleLogDao;
import com.myee.tarot.datacenter.service.ModuleLogService;
import com.myee.tarot.datacenter.domain.ModuleLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Ray.Fu on 2016/7/19.
 */
@Service
public class ModuleLogServiceImpl extends GenericEntityServiceImpl<Long, ModuleLog> implements ModuleLogService {

    private ModuleLogDao moduleLogDao;

    @Autowired
    public ModuleLogServiceImpl(ModuleLogDao moduleLogDao) {
        super(moduleLogDao);
        this.moduleLogDao = moduleLogDao;
    }

    @Override
    public List getModuleList() {
        return moduleLogDao.getModuleList();
    }

    @Override
    public List getFunctionListByModule(Integer moduleId) {
        return moduleLogDao.getFunctionListByModule(moduleId);
    }
}
