package com.myee.tarot.pricedraw.dao;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.pricedraw.domain.MerchantPrice;

import java.util.List;

/**
 * Created by Administrator on 2016/7/12.
 */
public interface MerchantPriceDao extends GenericEntityDao<Long , MerchantPrice>{
    List<MerchantPrice> findPriceByActivityId(Long activityId);
}
