package com.myee.tarot.admin.service.impl;

import com.myee.tarot.admin.dao.AdminUserDao;
import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.admin.service.AdminUserService;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.core.service.GenericResponse;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.WhereRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Martin on 2016/4/21.
 */
@Service
public class AdminUserServiceImpl extends GenericEntityServiceImpl<Long, AdminUser> implements AdminUserService {

    protected AdminUserDao adminUserDao;

    @Autowired
    public AdminUserServiceImpl(AdminUserDao adminUserDao) {
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

    @Override
    public AdminUser getByLogin(String login) {
        return adminUserDao.getByLogin(login);
    }

    @Override
    public PageResult<AdminUser> pageList(WhereRequest whereRequest){
        return adminUserDao.pageList(whereRequest);
    }
}
