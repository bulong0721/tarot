package com.myee.tarot.apiold.service.impl;

import com.myee.tarot.apiold.dao.AdvertisementDao;
import com.myee.tarot.apiold.domain.Advertisement;
import com.myee.tarot.apiold.service.AdvertisementService;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Chay on 2016/8/10.
 */
@Service
public class AdvertisementServiceImpl extends GenericEntityServiceImpl<Long, Advertisement> implements AdvertisementService {

    protected AdvertisementDao advertisementDao;

    @Autowired
    public AdvertisementServiceImpl(AdvertisementDao advertisementDao) {
        super(advertisementDao);
        this.advertisementDao = advertisementDao;
    }
}
