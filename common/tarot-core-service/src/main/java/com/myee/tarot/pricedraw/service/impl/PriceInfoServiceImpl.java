package com.myee.tarot.pricedraw.service.impl;

import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.pricedraw.dao.PriceInfoDao;
import com.myee.tarot.pricedraw.domain.PriceInfo;
import com.myee.tarot.pricedraw.service.PriceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/7/14.
 */
@Service
public class PriceInfoServiceImpl extends GenericEntityServiceImpl<Long, PriceInfo> implements PriceInfoService{

    @Autowired
    private PriceInfoDao priceInfoDao;

    @Autowired
    private PriceInfoServiceImpl(PriceInfoDao priceInfoDao){
        super(priceInfoDao);
        this.priceInfoDao = priceInfoDao;
    }
}
