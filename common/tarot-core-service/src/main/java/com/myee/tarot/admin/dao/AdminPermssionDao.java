package com.myee.tarot.admin.dao;

import com.myee.tarot.admin.domain.AdminPermission;
import com.myee.tarot.core.dao.GenericEntityDao;

/**
 * Created by Martin on 2016/4/11.
 */
public interface AdminPermssionDao extends GenericEntityDao<Long, AdminPermission> {

    AdminPermission getByName(String name);

    AdminPermission getByNameAndType(String name, String type);
}
