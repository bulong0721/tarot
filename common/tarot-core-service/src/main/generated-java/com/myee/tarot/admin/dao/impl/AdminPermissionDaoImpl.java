package com.myee.tarot.admin.dao.impl;

import com.myee.tarot.admin.domain.AdminPermission;
import com.myee.tarot.admin.dao.AdminPermissionDao;
import com.myee.tarot.admin.domain.QAdminPermission;
import com.myee.tarot.core.Constants;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import java.util.List;

@Repository
public class AdminPermissionDaoImpl extends GenericEntityDaoImpl<Long, AdminPermission> implements AdminPermissionDao {

    @Override
    public AdminPermission getByName(String name) {
        return null;
    }

    @Override
    public List<AdminPermission> listAllPermissions(Boolean isFriendly) {
        QAdminPermission qAdminPermission = QAdminPermission.adminPermission;
        JPQLQuery<AdminPermission> query = new JPAQuery(getEntityManager());
        query.from(qAdminPermission);
        if (isFriendly != null && isFriendly.equals(Constants.PERMISSION_FOR_ADMIN)) {
            query.where(qAdminPermission.isFriendly.eq(Constants.PERMISSION_FOR_ADMIN).or(qAdminPermission.isFriendly.eq(Constants.PERMISSION_FOR_CUSTOMER)));
        } else {
            query.where(qAdminPermission.isFriendly.eq(Constants.PERMISSION_FOR_CUSTOMER));
        }
        query.where(qAdminPermission.type.eq(Constants.PERMISSION_TYPE_ALL));
        return query.fetch();
    }


    @Override
    public List<AdminPermission> listByIds(List<Long> bindList) {
        QAdminPermission qAdminPermission = QAdminPermission.adminPermission;
        JPQLQuery<AdminPermission> query = new JPAQuery(getEntityManager());
        query.from(qAdminPermission);
        if (bindList != null && bindList.size() > 0) {
            query.where(qAdminPermission.id.in(bindList));
            return query.fetch();
        } else {
            return null;
        }
    }
}

