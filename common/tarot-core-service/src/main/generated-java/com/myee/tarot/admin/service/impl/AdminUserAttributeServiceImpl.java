package com.myee.tarot.admin.service.impl;

import com.myee.tarot.admin.domain.AdminUserAttribute;
import com.myee.tarot.admin.dao.AdminUserAttributeDao;
import com.myee.tarot.admin.service.AdminUserAttributeService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.myee.tarot.core.service.GenericEntityServiceImpl;

@Service
public class AdminUserAttributeServiceImpl extends GenericEntityServiceImpl<java.lang.Long, AdminUserAttribute> implements AdminUserAttributeService {

    protected AdminUserAttributeDao dao;

    @Autowired
    public AdminUserAttributeServiceImpl(AdminUserAttributeDao dao) {
        super(dao);
        this.dao = dao;
    }

}

