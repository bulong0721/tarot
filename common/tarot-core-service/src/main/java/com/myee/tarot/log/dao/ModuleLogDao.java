package com.myee.tarot.log.dao;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.log.domain.EventLevelLog;
import com.myee.tarot.log.domain.ModuleLog;

import java.util.List;

/**
 * Created by Ray.Fu on 2016/7/19.
 */
public interface ModuleLogDao extends GenericEntityDao<Long, ModuleLog> {

    public List getModuleList();

    public List getFunctionListByModule(Integer moduleId);
}
