package com.myee.tarot.admin.service;


import com.myee.tarot.admin.domain.AdminPermission;
import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.core.util.PageResult;

import java.util.List;

/**
 * Created by Martin on 2016/4/11.
 */
public interface AdminPermissionService extends GenericEntityService<Long, AdminPermission> {
    List<AdminPermission> listAllPermissions(Boolean isFriendly);

    List<AdminPermission> listByIds(List<Long> bindList);
}
