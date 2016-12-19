package com.myee.tarot.profile.service;

import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.profile.domain.Role;

import java.util.List;

public interface RoleService extends GenericEntityService<java.lang.Long, Role> {

    Role getByName(String name);

    List<Role> listByIds(List<Long> bindList);
}

