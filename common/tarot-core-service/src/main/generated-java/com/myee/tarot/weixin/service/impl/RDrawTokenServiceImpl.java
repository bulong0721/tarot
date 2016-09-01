package com.myee.tarot.weixin.service.impl;

import com.myee.tarot.weixin.domain.RDrawToken;
import com.myee.tarot.weixin.dao.RDrawTokenDao;
import com.myee.tarot.weixin.service.RDrawTokenService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.myee.tarot.core.service.GenericEntityServiceImpl;

@Service
public class RDrawTokenServiceImpl extends GenericEntityServiceImpl<java.lang.Long, RDrawToken> implements RDrawTokenService {

    protected RDrawTokenDao dao;

    @Autowired
    public RDrawTokenServiceImpl(RDrawTokenDao dao) {
        super(dao);
        this.dao = dao;
    }

}

