package com.myee.tarot.admin.dao;

import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.WhereRequest;

import java.util.List;
import java.util.Set;

/**
 * Created by Martin on 2016/4/11.
 */
public interface AdminUserDao extends GenericEntityDao<Long, AdminUser> {

    AdminUser getByUserName(String userName);

    List<AdminUser> listByEmail(String emailAddress);

    List<AdminUser> listByIds(Set<Long> ids);

    AdminUser getByLogin(String login);

    PageResult<AdminUser> pageList(WhereRequest whereRequest);
}
