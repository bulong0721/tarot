package com.myee.tarot.customer.service.impl;

import com.myee.tarot.customer.domain.CustomerForgotPasswordSecurityToken;
import com.myee.tarot.customer.dao.CustomerForgotPasswordSecurityTokenDao;
import com.myee.tarot.customer.service.CustomerForgotPasswordSecurityTokenService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.myee.tarot.core.service.GenericEntityServiceImpl;

@Service
public class CustomerForgotPasswordSecurityTokenServiceImpl extends GenericEntityServiceImpl<java.lang.String, CustomerForgotPasswordSecurityToken> implements CustomerForgotPasswordSecurityTokenService {

    protected CustomerForgotPasswordSecurityTokenDao dao;

    @Autowired
    public CustomerForgotPasswordSecurityTokenServiceImpl(CustomerForgotPasswordSecurityTokenDao dao) {
        super(dao);
        this.dao = dao;
    }

}

