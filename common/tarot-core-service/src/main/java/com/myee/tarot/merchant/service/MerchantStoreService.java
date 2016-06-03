package com.myee.tarot.merchant.service;

import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.merchant.domain.MerchantStore;

import java.util.List;

/**
 * Created by Martin on 2016/4/11.
 */
public interface MerchantStoreService extends GenericEntityService<Long, MerchantStore> {

    MerchantStore getByCode(String storeCode);

    Long getCountById(MerchantStore merchantStore);

    List<MerchantStore> listByMerchant(Long id);
}
