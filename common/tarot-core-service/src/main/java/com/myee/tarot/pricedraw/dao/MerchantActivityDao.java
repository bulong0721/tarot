package com.myee.tarot.pricedraw.dao;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.pricedraw.domain.MerchantActivity;

import java.util.List;

/**
 * Created by Administrator on 2016/7/11.
 */
public interface MerchantActivityDao extends GenericEntityDao<Long,MerchantActivity>{
    List<MerchantActivity> findStoreActivity(Long storeId);
}
