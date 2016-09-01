package com.myee.tarot.admin.service.impl;

import com.myee.tarot.admin.domain.AdminPermissionQualifiedEntity;
import com.myee.tarot.admin.dao.AdminPermissionQualifiedEntityDao;
import com.myee.tarot.admin.service.AdminPermissionQualifiedEntityService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.myee.tarot.core.service.GenericEntityServiceImpl;

@Service
public class AdminPermissionQualifiedEntityServiceImpl extends GenericEntityServiceImpl<java.lang.Long, AdminPermissionQualifiedEntity> implements AdminPermissionQualifiedEntityService {

    protected AdminPermissionQualifiedEntityDao dao;

    @Autowired
    public AdminPermissionQualifiedEntityServiceImpl(AdminPermissionQualifiedEntityDao dao) {
        super(dao);
        this.dao = dao;
    }

}

