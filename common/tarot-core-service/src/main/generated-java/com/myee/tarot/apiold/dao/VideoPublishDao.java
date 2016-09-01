package com.myee.tarot.apiold.dao;

import com.myee.tarot.apiold.domain.VideoPublish;
import com.myee.tarot.core.dao.GenericEntityDao;

import java.util.Date;
import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
public interface VideoPublishDao extends GenericEntityDao<Long, VideoPublish> {
    List<VideoPublish> listByStoreTime(Long storeId, Date now);
}
