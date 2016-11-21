package com.myee.tarot.admin.dao.impl;

import com.myee.tarot.admin.dao.AdminUserDao;
import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.admin.domain.QAdminUser;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.StringUtil;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

/**
 * Created by Martin on 2016/4/21.
 */
@Repository
public class AdminUserDaoImpl extends GenericEntityDaoImpl<Long, AdminUser> implements AdminUserDao {

    @Override
    public AdminUser getByUserName(String userName) {
        QAdminUser qUser = QAdminUser.adminUser;

        JPQLQuery<AdminUser> query = new JPAQuery(getEntityManager());

        query.from(qUser)
                .leftJoin(qUser.merchantStore)
                .fetchJoin();
        query.where(qUser.name.eq(userName));

        AdminUser user = query.fetchFirst();

        return user;
    }

    @Override
    public List<AdminUser> listByEmail(String emailAddress) {
        return null;
    }

    @Override
    public List<AdminUser> listByIds(Set<Long> ids) {
        return null;
    }

    @Override
    public AdminUser getByLogin(String login) {
        QAdminUser qUser = QAdminUser.adminUser;

        JPQLQuery<AdminUser> query = new JPAQuery(getEntityManager());

        query.from(qUser)
                .leftJoin(qUser.merchantStore)
                .fetchJoin();
        query.where(qUser.login.eq(login));

        AdminUser user = query.fetchFirst();

        return user;
    }

    @Override
    public PageResult<AdminUser> pageList(PageRequest pageRequest){
        PageResult<AdminUser> pageList = new PageResult<AdminUser>();
        QAdminUser qAdminUser = QAdminUser.adminUser;
        JPQLQuery<AdminUser> query = new JPAQuery(getEntityManager());
        query.from(qAdminUser);

        if(!StringUtil.isBlank(pageRequest.getQueryName())){
            query.where(qAdminUser.name.like("%" + pageRequest.getQueryName() + "%")
                    .or(qAdminUser.login.like("%" + pageRequest.getQueryName() + "%")));
        }
        pageList.setRecordsTotal(query.fetchCount());
        if( pageRequest.getCount() > 0){
            query.offset(pageRequest.getOffset()).limit(pageRequest.getCount());
        }
        pageList.setList(query.fetch());
        return pageList;
    }
}
