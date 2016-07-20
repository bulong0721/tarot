package com.myee.tarot.campaign.dao;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.campaign.domain.MerchantActivity;

import java.util.List;

/**
 * Created by Administrator on 2016/7/11.
 */
public interface MerchantActivityDao extends GenericEntityDao<Long,MerchantActivity>{
    List<MerchantActivity> findStoreActivity(Long storeId);
}
