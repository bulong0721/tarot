package com.myee.tarot.admin.dao;

import com.myee.tarot.admin.domain.AdminModule;
import com.myee.tarot.core.dao.GenericEntityDao;

/**
 * Created by Martin on 2016/4/29.
 */
public interface AdminModuleDao extends GenericEntityDao<Long, AdminModule> {

    AdminModule getByModuleKey(String moduleKey);
}
