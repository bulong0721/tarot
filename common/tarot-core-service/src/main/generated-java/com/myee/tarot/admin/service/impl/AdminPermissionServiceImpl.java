package com.myee.tarot.admin.service.impl;

import com.myee.tarot.admin.domain.AdminPermission;
import com.myee.tarot.admin.dao.AdminPermissionDao;
import com.myee.tarot.admin.service.AdminPermissionService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import java.util.List;

@Service
public class AdminPermissionServiceImpl extends GenericEntityServiceImpl<java.lang.Long, AdminPermission> implements AdminPermissionService {

    protected AdminPermissionDao dao;

    @Autowired
    public AdminPermissionServiceImpl(AdminPermissionDao dao) {
        super(dao);
        this.dao = dao;
    }

    @Override
    public List<AdminPermission> listAllPermissions(Boolean isFriendly) {
        return dao.listAllPermissions(isFriendly);
    }
}

