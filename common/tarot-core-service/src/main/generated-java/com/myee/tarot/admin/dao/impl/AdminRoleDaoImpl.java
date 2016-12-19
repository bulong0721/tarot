package com.myee.tarot.admin.dao.impl;

import com.myee.tarot.admin.domain.AdminRole;
import com.myee.tarot.admin.dao.AdminRoleDao;
import com.myee.tarot.admin.domain.QAdminRole;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;

import java.util.List;

@Repository
public class AdminRoleDaoImpl extends GenericEntityDaoImpl<java.lang.Long, AdminRole> implements AdminRoleDao {

    public final static Logger LOGGER = LoggerFactory.getLogger(AdminRoleDaoImpl.class);

    @Override
    public AdminRole getByName(String name) {
        QAdminRole qAdminRole = QAdminRole.adminRole;
        JPQLQuery<AdminRole> query = new JPAQuery(getEntityManager());
        query.from(qAdminRole)
                .where(qAdminRole.name.eq(name));
        LOGGER.info("获取总条数{}",query.fetchCount());
        return query.fetchFirst();
    }

    @Override
    public List<AdminRole> listByIds(List<Long> bindList) {
        QAdminRole qAdminRole = QAdminRole.adminRole;
        JPQLQuery<AdminRole> query = new JPAQuery(getEntityManager());
        query.from(qAdminRole).where(qAdminRole.id.in(bindList));
        return query.fetch();
    }
}

