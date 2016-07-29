package com.myee.tarot.datacenter.dao;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.datacenter.domain.EventModule;

import java.util.List;

/**
 * Created by Ray.Fu on 2016/7/19.
 */
public interface ModuleLogDao extends GenericEntityDao<Long, EventModule> {

    public List getModuleList();

    public List getFunctionListByModule(Integer moduleId);
}
