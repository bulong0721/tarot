package com.myee.tarot.admin.service.impl;

import com.myee.tarot.admin.domain.ForgotPasswordSecurityToken;
import com.myee.tarot.admin.dao.ForgotPasswordSecurityTokenDao;
import com.myee.tarot.admin.service.ForgotPasswordSecurityTokenService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.myee.tarot.core.service.GenericEntityServiceImpl;

@Service
public class ForgotPasswordSecurityTokenServiceImpl extends GenericEntityServiceImpl<java.lang.String, ForgotPasswordSecurityToken> implements ForgotPasswordSecurityTokenService {

    protected ForgotPasswordSecurityTokenDao dao;

    @Autowired
    public ForgotPasswordSecurityTokenServiceImpl(ForgotPasswordSecurityTokenDao dao) {
        super(dao);
        this.dao = dao;
    }

}

