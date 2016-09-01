package com.myee.tarot.apiold.service.impl;

import com.myee.tarot.apiold.domain.Video;
import com.myee.tarot.apiold.dao.VideoDao;
import com.myee.tarot.apiold.service.VideoService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.myee.tarot.core.service.GenericEntityServiceImpl;

@Service
public class VideoServiceImpl extends GenericEntityServiceImpl<java.lang.Long, Video> implements VideoService {

    protected VideoDao dao;

    @Autowired
    public VideoServiceImpl(VideoDao dao) {
        super(dao);
        this.dao = dao;
    }

}

