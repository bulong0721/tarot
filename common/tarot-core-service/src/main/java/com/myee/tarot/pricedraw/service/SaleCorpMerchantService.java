package com.myee.tarot.pricedraw.service;

import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.pricedraw.domain.SaleCorpMerchant;

/**
 * Created by Administrator on 2016/7/13.
 */
public interface SaleCorpMerchantService extends GenericEntityService<Long , SaleCorpMerchant>{
    SaleCorpMerchant findByMerchantId(Long merchantId);
}
