package com.myee.tarot.admin.service.impl;

import com.myee.tarot.admin.dao.AdminUserDao;
import com.myee.tarot.admin.dao.RoleDao;
import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.admin.service.AdminUserService;
import com.myee.tarot.admin.service.RoleService;
import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.core.service.GenericResponse;
import com.myee.tarot.reference.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Martin on 2016/4/21.
 */
@Service
public class RoleServiceImpl extends GenericEntityServiceImpl<Long, Role> implements RoleService {

    protected RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao) {
        super(roleDao);
    }
}
