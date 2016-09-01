package com.myee.tarot.weixin.service.impl;

import com.myee.tarot.weixin.domain.RDiner;
import com.myee.tarot.weixin.dao.RDinerDao;
import com.myee.tarot.weixin.service.RDinerService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.myee.tarot.core.service.GenericEntityServiceImpl;

@Service
public class RDinerServiceImpl extends GenericEntityServiceImpl<java.lang.Long, RDiner> implements RDinerService {

    protected RDinerDao dao;

    @Autowired
    public RDinerServiceImpl(RDinerDao dao) {
        super(dao);
        this.dao = dao;
    }

}

