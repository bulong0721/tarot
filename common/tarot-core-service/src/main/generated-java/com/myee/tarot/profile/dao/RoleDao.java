package com.myee.tarot.profile.dao;

import com.myee.tarot.profile.domain.Role;

import com.myee.tarot.core.dao.GenericEntityDao;

public interface RoleDao extends GenericEntityDao<java.lang.Long, Role> {

    Role getByName(String name);
}

