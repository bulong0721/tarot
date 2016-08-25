package com.myee.tarot.admin.service.impl;

import com.myee.tarot.admin.dao.RoleDao;
import com.myee.tarot.admin.service.RoleService;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.profile.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Martin on 2016/4/21.
 */
@Service
public class RoleServiceImpl extends GenericEntityServiceImpl<Long, Role> implements RoleService {

    protected RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao) {
        super(roleDao);
        this.roleDao = roleDao;
    }

    @Override
    public List<Role> listAll() {
        return list();
    }
}
