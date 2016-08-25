package com.myee.tarot.campaign.service;

import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.campaign.domain.MerchantPrice;

import java.util.List;

/**
 * Created by Administrator on 2016/7/12.
 */
public interface MerchantPriceService extends GenericEntityService<Long , MerchantPrice>{

    List<MerchantPrice> findPriceByActivityId(Long activityId);

    boolean isOnlyPriceName(String priceName, Long storeId);
}
