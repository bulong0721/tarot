package com.myee.tarot.admin.service;


import com.myee.tarot.admin.domain.AdminRole;
import com.myee.tarot.core.service.GenericEntityService;

/**
 * Created by Martin on 2016/4/11.
 */
public interface AdminRoleService extends GenericEntityService<Long, AdminRole> {
    AdminRole getByName(String name);
}
