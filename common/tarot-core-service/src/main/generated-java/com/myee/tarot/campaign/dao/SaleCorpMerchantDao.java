package com.myee.tarot.campaign.dao;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.campaign.domain.SaleCorpMerchant;

/**
 * Created by Administrator on 2016/7/13.
 */
public interface SaleCorpMerchantDao extends GenericEntityDao<Long, SaleCorpMerchant> {
    SaleCorpMerchant findByMerchantId(Long merchantId);
}
