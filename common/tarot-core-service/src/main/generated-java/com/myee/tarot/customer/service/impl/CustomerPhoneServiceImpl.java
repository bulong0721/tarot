package com.myee.tarot.customer.service.impl;

import com.myee.tarot.customer.domain.CustomerPhone;
import com.myee.tarot.customer.dao.CustomerPhoneDao;
import com.myee.tarot.customer.service.CustomerPhoneService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.myee.tarot.core.service.GenericEntityServiceImpl;

@Service
public class CustomerPhoneServiceImpl extends GenericEntityServiceImpl<java.lang.Long, CustomerPhone> implements CustomerPhoneService {

    protected CustomerPhoneDao dao;

    @Autowired
    public CustomerPhoneServiceImpl(CustomerPhoneDao dao) {
        super(dao);
        this.dao = dao;
    }

}

