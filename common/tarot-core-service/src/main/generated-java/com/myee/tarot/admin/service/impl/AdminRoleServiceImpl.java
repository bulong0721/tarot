package com.myee.tarot.admin.service.impl;

import com.myee.tarot.admin.domain.AdminRole;
import com.myee.tarot.admin.dao.AdminRoleDao;
import com.myee.tarot.admin.service.AdminRoleService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.myee.tarot.core.service.GenericEntityServiceImpl;

import java.util.List;

@Service
public class AdminRoleServiceImpl extends GenericEntityServiceImpl<java.lang.Long, AdminRole> implements AdminRoleService {

    protected AdminRoleDao dao;

    @Autowired
    public AdminRoleServiceImpl(AdminRoleDao dao) {
        super(dao);
        this.dao = dao;
    }

    @Override
    public AdminRole getByName(String name) {
        return dao.getByName(name);
    }

    @Override
    public List<AdminRole> listByIds(List<Long> bindList) {
        return dao.listByIds(bindList);
    }
}

