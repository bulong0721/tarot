package com.myee.tarot.admin.service.impl;

import com.myee.tarot.admin.dao.AdminUserDao;
import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.admin.service.AdminUserService;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.core.service.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Martin on 2016/4/21.
 */
@Service
public class AdminUserServiceServiceImpl extends GenericEntityServiceImpl<Long, AdminUser> implements AdminUserService {

    protected AdminUserDao adminUserDao;

    @Autowired
    public AdminUserServiceServiceImpl(AdminUserDao adminUserDao) {
        super(adminUserDao);
        this.adminUserDao = adminUserDao;
    }

    @Override
    public AdminUser getByUserName(String username) {
        return adminUserDao.getByUserName(username);
    }

    @Override
    public GenericResponse changePassword(String username, String oldPassword, String password, String confirmPassword) {
        return null;
    }
}
