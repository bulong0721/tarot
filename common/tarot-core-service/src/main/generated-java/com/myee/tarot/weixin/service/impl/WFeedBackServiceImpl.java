package com.myee.tarot.weixin.service.impl;

import com.myee.tarot.weixin.domain.WFeedBack;
import com.myee.tarot.weixin.dao.WFeedBackDao;
import com.myee.tarot.weixin.service.WFeedBackService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.myee.tarot.core.service.GenericEntityServiceImpl;

@Service
public class WFeedBackServiceImpl extends GenericEntityServiceImpl<java.lang.Long, WFeedBack> implements WFeedBackService {

    protected WFeedBackDao dao;

    @Autowired
    public WFeedBackServiceImpl(WFeedBackDao dao) {
        super(dao);
        this.dao = dao;
    }

}

