package com.myee.tarot.profile.service.impl;

import com.myee.tarot.profile.domain.Role;
import com.myee.tarot.profile.dao.RoleDao;
import com.myee.tarot.profile.service.RoleService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.myee.tarot.core.service.GenericEntityServiceImpl;

import java.util.List;

@Service
public class RoleServiceImpl extends GenericEntityServiceImpl<java.lang.Long, Role> implements RoleService {

    protected RoleDao dao;

    @Autowired
    public RoleServiceImpl(RoleDao dao) {
        super(dao);
        this.dao = dao;
    }


}

