package com.myee.tarot.customer.service.impl;

import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.customer.dao.CustomerRoleDao;
import com.myee.tarot.customer.domain.CustomerRole;
import com.myee.tarot.customer.service.CustomerRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Martin on 2016/4/21.
 */
@Service
public class CustomerRoleServiceImpl extends GenericEntityServiceImpl<Long, CustomerRole> implements CustomerRoleService {

    protected CustomerRoleDao customerRoleDao;

    @Autowired
    public CustomerRoleServiceImpl(CustomerRoleDao customerRoleDao) {
        super(customerRoleDao);
        this.customerRoleDao = customerRoleDao;
    }

    @Override
    public List<CustomerRole> listByCustomerId(Long customerId) {
        return customerRoleDao.listByCustomerId(customerId);
    }
}
