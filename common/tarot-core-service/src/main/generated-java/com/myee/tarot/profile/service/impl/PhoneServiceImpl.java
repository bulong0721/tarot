package com.myee.tarot.profile.service.impl;

import com.myee.tarot.profile.domain.Phone;
import com.myee.tarot.profile.dao.PhoneDao;
import com.myee.tarot.profile.service.PhoneService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.myee.tarot.core.service.GenericEntityServiceImpl;

@Service
public class PhoneServiceImpl extends GenericEntityServiceImpl<java.lang.Long, Phone> implements PhoneService {

    protected PhoneDao dao;

    @Autowired
    public PhoneServiceImpl(PhoneDao dao) {
        super(dao);
        this.dao = dao;
    }

}

