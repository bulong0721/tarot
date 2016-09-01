package com.myee.tarot.datacenter.service;

import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.datacenter.domain.EventModule;

import java.util.List;

/**
 * Created by Ray.Fu on 2016/7/19.
 */
public interface ModuleLogService  extends GenericEntityService<Long, EventModule> {

    public List listGroupByModuleId();

    public List listByModuleId(Integer moduleId);
}
