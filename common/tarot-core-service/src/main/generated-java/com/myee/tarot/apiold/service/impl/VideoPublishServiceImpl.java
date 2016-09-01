package com.myee.tarot.apiold.service.impl;

import com.myee.tarot.apiold.dao.VideoPublishDao;
import com.myee.tarot.apiold.domain.VideoPublish;
import com.myee.tarot.apiold.service.VideoPublishService;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
@Service
public class VideoPublishServiceImpl extends GenericEntityServiceImpl<Long, VideoPublish> implements VideoPublishService {

    protected VideoPublishDao videoPublishDao;

    @Autowired
    public VideoPublishServiceImpl(VideoPublishDao videoPublishDao) {
        super(videoPublishDao);
        this.videoPublishDao = videoPublishDao;
    }

    public List<VideoPublish> listByStoreTime(Long storeId, Date now){
        return videoPublishDao.listByStoreTime(storeId, now);
    }
}
