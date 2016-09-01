package com.myee.tarot.apiold.service.impl;

import com.myee.tarot.apiold.domain.MenuInfo;
import com.myee.tarot.apiold.dao.TasteInfoDao;
import com.myee.tarot.apiold.service.TasteInfoService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.myee.tarot.core.service.GenericEntityServiceImpl;

@Service
public class TasteInfoServiceImpl extends GenericEntityServiceImpl<java.lang.Long, MenuInfo> implements TasteInfoService {

    protected TasteInfoDao dao;

    @Autowired
    public TasteInfoServiceImpl(TasteInfoDao dao) {
        super(dao);
        this.dao = dao;
    }

}

