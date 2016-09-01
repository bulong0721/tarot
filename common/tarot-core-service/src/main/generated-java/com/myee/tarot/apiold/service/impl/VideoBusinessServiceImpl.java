package com.myee.tarot.apiold.service.impl;

import com.myee.tarot.apiold.dao.VideoBusinessDao;
import com.myee.tarot.apiold.domain.VideoBusiness;
import com.myee.tarot.apiold.service.VideoBusinessService;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
@Service
public class VideoBusinessServiceImpl extends GenericEntityServiceImpl<Long, VideoBusiness> implements VideoBusinessService {

    protected VideoBusinessDao videoBusinessDao;

    @Autowired
    public VideoBusinessServiceImpl(VideoBusinessDao videoBusinessDao) {
        super(videoBusinessDao);
        this.videoBusinessDao = videoBusinessDao;
    }

    public List<VideoBusiness> listByTypeStoreTime(Long storeId,int type, Date now){
        return videoBusinessDao.listByTypeStoreTime(storeId,type, now);
    }

    public VideoBusiness findByIdType(Long vBId, Integer type){
        return videoBusinessDao.findByIdType(vBId,type);
    }
}
