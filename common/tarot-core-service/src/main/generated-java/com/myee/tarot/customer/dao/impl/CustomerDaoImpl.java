package com.myee.tarot.customer.dao.impl;

import com.myee.tarot.core.Constants;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.StringUtil;
import com.myee.tarot.customer.dao.CustomerDao;
import com.myee.tarot.customer.domain.Customer;
import com.myee.tarot.customer.domain.QCustomer;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Martin on 2016/4/21.
 */
@Component
public class CustomerDaoImpl extends GenericEntityDaoImpl<Long, Customer> implements CustomerDao {

    @Override
    public Customer getByUsername(String username) {
        QCustomer qCustomer = QCustomer.customer;
        JPQLQuery<Customer> query = new JPAQuery<Customer>(getEntityManager());
        query.from(qCustomer);
        query.where(qCustomer.username.eq(username));

        Customer customer = query.fetchFirst();
        return customer;
    }

    @Override
    public Customer getByUsername(String username, Boolean cacheable) {
        return null;
    }

    @Override
    public List<Customer> listByUsername(String username) {
        return null;
    }

    @Override
    public List<Customer> listByUsername(String username, Boolean cacheable) {
        return null;
    }

    @Override
    public Customer getByEmail(String emailAddress) {
        return null;
    }

    @Override
    public List<Customer> listByEmail(String emailAddress) {
        return null;
    }

    @Override
    public PageResult<Customer> pageByStore(Long storeId, PageRequest pageRequest){
        PageResult<Customer> pageList = new PageResult<Customer>();
        QCustomer qCustomer = QCustomer.customer;
        JPQLQuery<Customer> query = new JPAQuery(getEntityManager());
        query.from(qCustomer);
        query.where(qCustomer.merchantStore.id.eq(storeId));

        if (!StringUtil.isBlank(pageRequest.getQueryName())) {
            query.where( (qCustomer.firstName.like("%" + pageRequest.getQueryName() + "%"))
                    .or(qCustomer.lastName.like("%" + pageRequest.getQueryName() + "%"))
                    .or(qCustomer.username.like("%" + pageRequest.getQueryName() + "%")) );
        }
        pageList.setRecordsTotal(query.fetchCount());
        if( pageRequest.getCount() > Constants.COUNT_PAGING_MARK){
            query.offset(pageRequest.getOffset()).limit(pageRequest.getCount());
        }
        pageList.setList(query.fetch());
        return pageList;
    }
}
