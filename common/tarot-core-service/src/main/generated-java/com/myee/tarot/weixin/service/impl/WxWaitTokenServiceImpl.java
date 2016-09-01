package com.myee.tarot.weixin.service.impl;

import com.myee.tarot.weixin.domain.WxWaitToken;
import com.myee.tarot.weixin.dao.WxWaitTokenDao;
import com.myee.tarot.weixin.service.WxWaitTokenService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.myee.tarot.core.service.GenericEntityServiceImpl;

@Service
public class WxWaitTokenServiceImpl extends GenericEntityServiceImpl<java.lang.Long, WxWaitToken> implements WxWaitTokenService {

    protected WxWaitTokenDao dao;

    @Autowired
    public WxWaitTokenServiceImpl(WxWaitTokenDao dao) {
        super(dao);
        this.dao = dao;
    }

}

