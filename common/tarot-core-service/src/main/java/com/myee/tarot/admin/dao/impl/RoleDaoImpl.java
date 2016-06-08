package com.myee.tarot.admin.dao.impl;

import com.myee.tarot.admin.dao.RoleDao;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.reference.domain.Role;
import org.springframework.stereotype.Repository;

/**
 * Created by Martin on 2016/4/21.
 */
@Repository
public class RoleDaoImpl extends GenericEntityDaoImpl<Long, Role> implements RoleDao {

}
