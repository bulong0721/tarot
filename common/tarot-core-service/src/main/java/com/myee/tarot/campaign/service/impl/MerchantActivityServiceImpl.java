package com.myee.tarot.campaign.service.impl;

import com.myee.tarot.campaign.dao.MerchantActivityDao;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.campaign.domain.MerchantActivity;
import com.myee.tarot.campaign.service.MerchantActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2016/7/11.
 */
@Service
public class MerchantActivityServiceImpl extends GenericEntityServiceImpl<Long, MerchantActivity> implements MerchantActivityService{

    protected MerchantActivityDao merchantActivityDao;

    @Autowired
    public MerchantActivityServiceImpl(MerchantActivityDao merchantActivityDao) {
        super(merchantActivityDao);
        this.merchantActivityDao = merchantActivityDao;
    }

    @Override
    public List<MerchantActivity> findStoreActivity(Long storeId) {
        return merchantActivityDao.findStoreActivity(storeId);
    }
}
