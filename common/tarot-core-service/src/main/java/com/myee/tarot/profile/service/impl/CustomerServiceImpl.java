package com.myee.tarot.profile.service.impl;

import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.profile.dao.CustomerDao;
import com.myee.tarot.profile.domain.Customer;
import com.myee.tarot.profile.service.CustomerService;
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
    public Customer getByUsername(String username, boolean b) {
        return null;
    }
}
