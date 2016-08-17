package com.myee.tarot.customer.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.customer.dao.CustomerRoleDao;
import com.myee.tarot.customer.domain.CustomerRole;
import com.myee.tarot.customer.domain.QCustomerRole;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Martin on 2016/4/21.
 */
@Service
public class CustomerRoleDaoImpl extends GenericEntityDaoImpl<Long, CustomerRole> implements CustomerRoleDao {

    @Override
    public List<CustomerRole> listByCustomerId(Long customerId) {
        QCustomerRole qCustomerRole = QCustomerRole.customerRole;
        JPQLQuery<CustomerRole> query = new JPAQuery<CustomerRole>(getEntityManager());
        query.from(qCustomerRole);
        if (null != customerId) {
            query.where(qCustomerRole.customer.id.eq(customerId));
        }
        return query.fetch();
    }
}
