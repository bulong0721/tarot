package com.myee.tarot.admin.dao.impl;

import com.myee.tarot.admin.domain.AdminRole;
import com.myee.tarot.admin.dao.AdminRoleDao;
import com.myee.tarot.admin.domain.QAdminRole;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;

@Repository
public class AdminRoleDaoImpl extends GenericEntityDaoImpl<java.lang.Long, AdminRole> implements AdminRoleDao {

    public static Log log = LogFactory.getLog(AdminRoleDaoImpl.class);

    @Override
    public AdminRole getByName(String name) {
        QAdminRole qAdminRole = QAdminRole.adminRole;
        JPQLQuery<AdminRole> query = new JPAQuery(getEntityManager());
        query.from(qAdminRole)
                .where(qAdminRole.name.eq(name));
        log.info(query.fetchCount());
        return query.fetchFirst();
    }
}

