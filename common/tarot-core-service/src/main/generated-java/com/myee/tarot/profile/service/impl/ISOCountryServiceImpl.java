package com.myee.tarot.profile.service.impl;

import com.myee.tarot.profile.domain.ISOCountry;
import com.myee.tarot.profile.dao.ISOCountryDao;
import com.myee.tarot.profile.service.ISOCountryService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.myee.tarot.core.service.GenericEntityServiceImpl;

@Service
public class ISOCountryServiceImpl extends GenericEntityServiceImpl<java.lang.String, ISOCountry> implements ISOCountryService {

    protected ISOCountryDao dao;

    @Autowired
    public ISOCountryServiceImpl(ISOCountryDao dao) {
        super(dao);
        this.dao = dao;
    }

}

