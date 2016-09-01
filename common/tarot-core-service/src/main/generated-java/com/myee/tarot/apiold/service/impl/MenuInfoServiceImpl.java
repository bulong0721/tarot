package com.myee.tarot.apiold.service.impl;

import com.myee.tarot.apiold.domain.MenuInfo;
import com.myee.tarot.apiold.dao.MenuInfoDao;
import com.myee.tarot.apiold.service.MenuInfoService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.myee.tarot.core.service.GenericEntityServiceImpl;

@Service
public class MenuInfoServiceImpl extends GenericEntityServiceImpl<java.lang.Long, MenuInfo> implements MenuInfoService {

    protected MenuInfoDao dao;

    @Autowired
    public MenuInfoServiceImpl(MenuInfoDao dao) {
        super(dao);
        this.dao = dao;
    }

}

