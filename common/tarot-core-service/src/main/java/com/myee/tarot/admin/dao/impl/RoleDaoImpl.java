package com.myee.tarot.admin.dao.impl;

import com.myee.tarot.admin.dao.AdminUserDao;
import com.myee.tarot.admin.dao.RoleDao;
import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.admin.domain.QAdminUser;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.reference.domain.Role;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Created by Martin on 2016/4/21.
 */
@Repository
public class RoleDaoImpl extends GenericEntityDaoImpl<Long, Role> implements RoleDao {

}
