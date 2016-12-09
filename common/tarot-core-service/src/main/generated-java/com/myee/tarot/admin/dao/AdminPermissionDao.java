package com.myee.tarot.admin.dao;

import com.myee.tarot.admin.domain.AdminPermission;
import com.myee.tarot.core.dao.GenericEntityDao;
import java.util.List;

/**
 * Created by Martin on 2016/4/11.
 */
public interface AdminPermissionDao extends GenericEntityDao<Long, AdminPermission> {

    AdminPermission getByName(String name);

    AdminPermission getByNameAndType(String name, String type);

    List<AdminPermission> listAllPermissions(Boolean isFriendly);
}
