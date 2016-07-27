package com.myee.tarot.campaign.service;

import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.campaign.domain.MerchantActivity;

import java.util.List;

/**
 * Created by Administrator on 2016/7/11.
 */
public interface MerchantActivityService extends GenericEntityService<Long, MerchantActivity>{
    MerchantActivity findStoreActivity(Long storeId);
}
