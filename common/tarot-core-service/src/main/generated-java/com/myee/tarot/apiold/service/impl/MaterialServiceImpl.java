package com.myee.tarot.apiold.service.impl;

import com.myee.tarot.apiold.domain.Material;
import com.myee.tarot.apiold.dao.MaterialDao;
import com.myee.tarot.apiold.service.MaterialService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.myee.tarot.core.service.GenericEntityServiceImpl;

@Service
public class MaterialServiceImpl extends GenericEntityServiceImpl<java.lang.Long, Material> implements MaterialService {

    protected MaterialDao dao;

    @Autowired
    public MaterialServiceImpl(MaterialDao dao) {
        super(dao);
        this.dao = dao;
    }

}

