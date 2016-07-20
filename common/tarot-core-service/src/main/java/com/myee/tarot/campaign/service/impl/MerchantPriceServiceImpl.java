package com.myee.tarot.campaign.service.impl;

import com.myee.tarot.campaign.dao.MerchantPriceDao;
import com.myee.tarot.campaign.service.MerchantPriceService;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.campaign.domain.MerchantPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016/7/12.
 */
@Service
public class MerchantPriceServiceImpl extends GenericEntityServiceImpl<Long , MerchantPrice> implements MerchantPriceService {

    @Autowired
    private MerchantPriceDao merchantPriceDao;

    @Autowired
    public MerchantPriceServiceImpl(MerchantPriceDao merchantPriceDao) {
        super(merchantPriceDao);
        this.merchantPriceDao = merchantPriceDao;
    }

    @Override
    public List<MerchantPrice> findPriceByActivityId(Long activityId) {
        return merchantPriceDao.findPriceByActivityId(activityId);
    }
}
