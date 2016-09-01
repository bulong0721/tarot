package com.myee.tarot.profile.service.impl;

import com.myee.tarot.profile.domain.Media;
import com.myee.tarot.profile.dao.MediaDao;
import com.myee.tarot.profile.service.MediaService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.myee.tarot.core.service.GenericEntityServiceImpl;

@Service
public class MediaServiceImpl extends GenericEntityServiceImpl<java.lang.Long, Media> implements MediaService {

    protected MediaDao dao;

    @Autowired
    public MediaServiceImpl(MediaDao dao) {
        super(dao);
        this.dao = dao;
    }

}

