package com.myee.tarot.admin.dao;

import com.myee.tarot.admin.domain.AdminRole;
import com.myee.tarot.core.dao.GenericEntityDao;

import java.util.List;

/**
 * Created by Martin on 2016/4/11.
 */
public interface AdminRoleDao extends GenericEntityDao<Long, AdminRole> {

    AdminRole getByName(String name);

    List<AdminRole> listByIds(List<Long> bindList);
}
