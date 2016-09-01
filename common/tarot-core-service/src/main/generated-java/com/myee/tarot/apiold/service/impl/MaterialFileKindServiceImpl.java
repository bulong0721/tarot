package com.myee.tarot.apiold.service.impl;

import com.myee.tarot.apiold.domain.MaterialFileKind;
import com.myee.tarot.apiold.dao.MaterialFileKindDao;
import com.myee.tarot.apiold.service.MaterialFileKindService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.myee.tarot.core.service.GenericEntityServiceImpl;

@Service
public class MaterialFileKindServiceImpl extends GenericEntityServiceImpl<java.lang.Long, MaterialFileKind> implements MaterialFileKindService {

    protected MaterialFileKindDao dao;

    @Autowired
    public MaterialFileKindServiceImpl(MaterialFileKindDao dao) {
        super(dao);
        this.dao = dao;
    }

}

