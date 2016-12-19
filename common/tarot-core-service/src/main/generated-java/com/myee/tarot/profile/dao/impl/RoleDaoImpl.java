package com.myee.tarot.profile.dao.impl;

import com.myee.tarot.profile.domain.QRole;
import com.myee.tarot.profile.domain.Role;
import com.myee.tarot.profile.dao.RoleDao;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;

import java.util.List;

@Repository
public class RoleDaoImpl extends GenericEntityDaoImpl<java.lang.Long, Role> implements RoleDao {

    public final static Logger LOGGER = LoggerFactory.getLogger(RoleDaoImpl.class);

    @Override
    public Role getByName(String name) {
        QRole qRole = QRole.role;
        JPQLQuery<Role> query = new JPAQuery(getEntityManager());
        query.from(qRole)
                .where(qRole.roleName.eq(name));
        LOGGER.info("获取总条数{}", query.fetchCount());
        return query.fetchFirst();
    }

    @Override
    public List<Role> listByIds(List<Long> bindList) {
        QRole qRole = QRole.role;
        JPQLQuery<Role> query = new JPAQuery(getEntityManager());
        query.from(qRole).where(qRole.id.in(bindList));
        return query.fetch();
    }
}

