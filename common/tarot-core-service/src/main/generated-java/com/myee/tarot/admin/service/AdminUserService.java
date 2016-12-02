package com.myee.tarot.admin.service;

import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.core.service.GenericResponse;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.WhereRequest;

/**
 * Created by Martin on 2016/4/11.
 */
public interface AdminUserService extends GenericEntityService<Long, AdminUser> {

    AdminUser getByUserName(String username);

    GenericResponse changePassword(String username, String oldPassword, String password, String confirmPassword);

    AdminUser getByLogin(String username);

    PageResult<AdminUser> pageList(WhereRequest whereRequest);
}
