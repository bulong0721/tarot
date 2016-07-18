package com.myee.tarot.pricedraw.service.impl;

import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.pricedraw.dao.SaleCorpMerchantDao;
import com.myee.tarot.pricedraw.domain.SaleCorpMerchant;
import com.myee.tarot.pricedraw.service.SaleCorpMerchantService;
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
