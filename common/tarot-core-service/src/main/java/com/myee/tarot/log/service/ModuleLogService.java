package com.myee.tarot.log.service;

import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.log.domain.ModuleLog;
import com.myee.tarot.log.domain.SelfCheckLog;

import java.util.List;

/**
 * Created by Ray.Fu on 2016/7/19.
 */
public interface ModuleLogService  extends GenericEntityService<Long, ModuleLog> {

    public List getModuleList();

    public List getFunctionListByModule(Integer moduleId);
}
