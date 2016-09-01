package com.myee.tarot.apiold.service;

import com.myee.tarot.apiold.domain.AdvertisementPublish;
import com.myee.tarot.core.service.GenericEntityService;

import java.util.Date;
import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
public interface AdvertisementPublishService extends GenericEntityService<Long, AdvertisementPublish> {
    List<AdvertisementPublish> listByStoreTime(Long storeId, Date now);
}
