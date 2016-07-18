package com.myee.tarot.pricedraw.service;

import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.pricedraw.domain.MerchantPrice;

import java.util.List;

/**
 * Created by Administrator on 2016/7/12.
 */
public interface MerchantPriceService extends GenericEntityService<Long , MerchantPrice>{

    List<MerchantPrice> findPriceByActivityId(Long activityId);
}
