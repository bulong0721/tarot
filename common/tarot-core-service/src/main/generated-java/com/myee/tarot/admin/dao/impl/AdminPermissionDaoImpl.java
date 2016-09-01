package com.myee.tarot.admin.dao.impl;

import com.myee.tarot.admin.domain.AdminPermission;
import com.myee.tarot.admin.dao.AdminPermissionDao;

import org.springframework.stereotype.Repository;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;

@Repository
public class AdminPermissionDaoImpl extends GenericEntityDaoImpl<Long, AdminPermission> implements AdminPermissionDao {

    @Override
    public AdminPermission getByName(String name) {
        return null;
    }

    @Override
    public AdminPermission getByNameAndType(String name, String type) {
        return null;
    }
}

