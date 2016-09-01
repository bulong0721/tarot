package com.myee.tarot.apiold.service.impl;

import com.myee.tarot.apiold.dao.AdvertisementPublishDao;
import com.myee.tarot.apiold.domain.AdvertisementPublish;
import com.myee.tarot.apiold.service.AdvertisementPublishService;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
@Service
public class AdvertisementPublishServiceImpl extends GenericEntityServiceImpl<Long, AdvertisementPublish> implements AdvertisementPublishService {

    protected AdvertisementPublishDao advertisementPublishDao;

    @Autowired
    public AdvertisementPublishServiceImpl(AdvertisementPublishDao advertisementPublishDao) {
        super(advertisementPublishDao);
        this.advertisementPublishDao = advertisementPublishDao;
    }

    public List<AdvertisementPublish> listByStoreTime(Long storeId, Date now){
        return advertisementPublishDao.listByStoreTime(storeId,now);
    }
}
