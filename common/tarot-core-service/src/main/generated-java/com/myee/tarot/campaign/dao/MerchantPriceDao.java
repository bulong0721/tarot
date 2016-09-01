package com.myee.tarot.campaign.dao;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.campaign.domain.MerchantPrice;

import java.util.List;

/**
 * Created by Administrator on 2016/7/12.
 */
public interface MerchantPriceDao extends GenericEntityDao<Long , MerchantPrice>{
    List<MerchantPrice> findPriceByActivityId(Long activityId);

    Long countSamePriceName(String priceName, Long storeId);

    Long countSamePriceName(Long id , String priceName, Long storeId);

}
