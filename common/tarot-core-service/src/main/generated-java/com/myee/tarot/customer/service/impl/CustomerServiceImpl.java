package com.myee.tarot.customer.service.impl;

import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.customer.dao.CustomerDao;
import com.myee.tarot.customer.domain.Customer;
import com.myee.tarot.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Martin on 2016/4/21.
 */
@Service
public class CustomerServiceImpl extends GenericEntityServiceImpl<Long, Customer> implements CustomerService {

    protected CustomerDao customerDao;

    @Autowired
    public CustomerServiceImpl(CustomerDao customerDao) {
        super(customerDao);
        this.customerDao = customerDao;
    }

    @Override
    public Customer getByUsername(String username) {
        return customerDao.getByUsername(username);
    }

    @Override
    public PageResult<Customer> pageByStore(Long storeId, PageRequest pageRequest){
        return customerDao.pageByStore(storeId,  pageRequest);
    }
}
