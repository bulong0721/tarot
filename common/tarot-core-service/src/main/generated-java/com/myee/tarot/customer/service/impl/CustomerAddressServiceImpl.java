package com.myee.tarot.customer.service.impl;

import com.myee.tarot.customer.domain.CustomerAddress;
import com.myee.tarot.customer.dao.CustomerAddressDao;
import com.myee.tarot.customer.service.CustomerAddressService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.myee.tarot.core.service.GenericEntityServiceImpl;

@Service
public class CustomerAddressServiceImpl extends GenericEntityServiceImpl<java.lang.Long, CustomerAddress> implements CustomerAddressService {

    protected CustomerAddressDao dao;

    @Autowired
    public CustomerAddressServiceImpl(CustomerAddressDao dao) {
        super(dao);
        this.dao = dao;
    }

}

