package com.myee.tarot.apiold.service;

import com.myee.tarot.apiold.domain.VideoPublish;
import com.myee.tarot.core.service.GenericEntityService;

import java.util.Date;
import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
public interface VideoPublishService extends GenericEntityService<Long, VideoPublish> {
    List<VideoPublish> listByStore(Long storeId, Date now);
}
