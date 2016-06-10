package com.myee.tarot.admin.dao;

import com.myee.tarot.admin.domain.AdminSection;
import com.myee.tarot.core.dao.GenericEntityDao;

/**
 * Created by Martin on 2016/4/29.
 */
public interface AdminSectionDao extends GenericEntityDao<Long, AdminSection> {

    AdminSection getByClassAndSectionId(Class<?> clazz, String sectionId);

    AdminSection getSectionByURI(String uri);

    AdminSection getBySectionKey(String sectionKey);
}
