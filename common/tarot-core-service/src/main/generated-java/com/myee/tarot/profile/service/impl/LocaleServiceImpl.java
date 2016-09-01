package com.myee.tarot.profile.service.impl;

import com.myee.tarot.profile.domain.Locale;
import com.myee.tarot.profile.dao.LocaleDao;
import com.myee.tarot.profile.service.LocaleService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.myee.tarot.core.service.GenericEntityServiceImpl;

@Service
public class LocaleServiceImpl extends GenericEntityServiceImpl<java.lang.String, Locale> implements LocaleService {

    protected LocaleDao dao;

    @Autowired
    public LocaleServiceImpl(LocaleDao dao) {
        super(dao);
        this.dao = dao;
    }

}

