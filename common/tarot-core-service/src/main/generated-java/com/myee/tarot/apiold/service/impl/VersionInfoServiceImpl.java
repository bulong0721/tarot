package com.myee.tarot.apiold.service.impl;

import com.myee.tarot.apiold.domain.VersionInfo;
import com.myee.tarot.apiold.dao.VersionInfoDao;
import com.myee.tarot.apiold.service.VersionInfoService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.myee.tarot.core.service.GenericEntityServiceImpl;

@Service
public class VersionInfoServiceImpl extends GenericEntityServiceImpl<java.lang.Long, VersionInfo> implements VersionInfoService {

    protected VersionInfoDao dao;

    @Autowired
    public VersionInfoServiceImpl(VersionInfoDao dao) {
        super(dao);
        this.dao = dao;
    }

}

