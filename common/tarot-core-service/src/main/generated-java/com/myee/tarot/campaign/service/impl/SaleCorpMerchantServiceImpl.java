package com.myee.tarot.campaign.service.impl;

import com.myee.tarot.campaign.dao.SaleCorpMerchantDao;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.campaign.domain.SaleCorpMerchant;
import com.myee.tarot.campaign.service.SaleCorpMerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/7/13.
 */
@Service
public class SaleCorpMerchantServiceImpl extends GenericEntityServiceImpl<Long, SaleCorpMerchant> implements SaleCorpMerchantService{

    @Autowired
    private SaleCorpMerchantDao saleCorpMerchantDao;

    @Autowired
    public SaleCorpMerchantServiceImpl(SaleCorpMerchantDao saleCorpMerchantDao) {
        super(saleCorpMerchantDao);
        this.saleCorpMerchantDao = saleCorpMerchantDao;
    }

    @Override
    public SaleCorpMerchant findByMerchantId(Long merchantId) {
        return saleCorpMerchantDao.findByMerchantId(merchantId);
    }
}
