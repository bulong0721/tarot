package com.myee.tarot.apiold.service.impl;

import com.myee.tarot.apiold.domain.UploadAccessAction;
import com.myee.tarot.apiold.dao.UploadAccessActionDao;
import com.myee.tarot.apiold.service.UploadAccessActionService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.myee.tarot.core.service.GenericEntityServiceImpl;

@Service
public class UploadAccessActionServiceImpl extends GenericEntityServiceImpl<java.lang.Long, UploadAccessAction> implements UploadAccessActionService {

    protected UploadAccessActionDao dao;

    @Autowired
    public UploadAccessActionServiceImpl(UploadAccessActionDao dao) {
        super(dao);
        this.dao = dao;
    }

}

