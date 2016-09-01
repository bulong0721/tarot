package com.myee.tarot.customer.service.impl;

import com.myee.tarot.customer.domain.CustomerAttribute;
import com.myee.tarot.customer.dao.CustomerAttributeDao;
import com.myee.tarot.customer.service.CustomerAttributeService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.myee.tarot.core.service.GenericEntityServiceImpl;

@Service
public class CustomerAttributeServiceImpl extends GenericEntityServiceImpl<java.lang.Long, CustomerAttribute> implements CustomerAttributeService {

    protected CustomerAttributeDao dao;

    @Autowired
    public CustomerAttributeServiceImpl(CustomerAttributeDao dao) {
        super(dao);
        this.dao = dao;
    }

}

