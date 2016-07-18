package com.myee.tarot.pricedraw.service;

import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.pricedraw.domain.MerchantActivity;

import java.util.List;

/**
 * Created by Administrator on 2016/7/11.
 */
public interface MerchantActivityService extends GenericEntityService<Long, MerchantActivity>{
    List<MerchantActivity> findStoreActivity(Long storeId);
}
