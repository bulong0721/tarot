package com.myee.tarot.profile.dao.impl;

import com.myee.tarot.profile.domain.QRole;
import com.myee.tarot.profile.domain.Role;
import com.myee.tarot.profile.dao.RoleDao;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;

@Repository
public class RoleDaoImpl extends GenericEntityDaoImpl<java.lang.Long, Role> implements RoleDao {

    public static Log log = LogFactory.getLog(RoleDaoImpl.class);

    @Override
    public Role getByName(String name) {
        QRole qRole = QRole.role;
        JPQLQuery<Role> query = new JPAQuery(getEntityManager());
        query.from(qRole)
                .where(qRole.roleName.eq(name));
        log.info(query.fetchCount());
        return query.fetchFirst();
    }
}

