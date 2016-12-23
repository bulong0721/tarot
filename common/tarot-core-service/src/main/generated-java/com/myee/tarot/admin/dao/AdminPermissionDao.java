package com.myee.tarot.admin.dao;

import com.myee.tarot.admin.domain.AdminPermission;
import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.util.PageResult;

import java.util.List;

/**
 * Created by Martin on 2016/4/11.
 */
public interface AdminPermissionDao extends GenericEntityDao<Long, AdminPermission> {

    AdminPermission getByName(String name);

    List<AdminPermission> listAllPermissions(Boolean isFriendly);

    /**
     * 根据选中的权限ID查询所有的权限
     * @param bindList
     * @return
     */
    List<AdminPermission> listByIds(List<Long> bindList);
}
