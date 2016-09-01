package com.myee.tarot.profile.dao.impl;

import com.myee.tarot.profile.domain.Role;
import com.myee.tarot.profile.dao.RoleDao;

import org.springframework.stereotype.Repository;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;

@Repository
public class RoleDaoImpl extends GenericEntityDaoImpl<java.lang.Long, Role> implements RoleDao {

}

