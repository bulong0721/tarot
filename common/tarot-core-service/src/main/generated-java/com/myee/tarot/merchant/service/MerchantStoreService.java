package com.myee.tarot.merchant.service;

import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.merchant.domain.MerchantStore;

import java.util.List;

/**
 * Created by Martin on 2016/4/11.
 */
public interface MerchantStoreService extends GenericEntityService<Long, MerchantStore> {

    MerchantStore getByCode(String storeCode);

//    Long getCountById(Long id);

//    List<MerchantStore> listByMerchant(Long id);

    PageResult<MerchantStore> pageListByMerchant(Long id,PageRequest pageRequest );

    Long getCountById(Long merchantStoreId, Long merchantId);

    MerchantStore getByMerchantStoreName(String name);

    List<MerchantStore> listByMerchantId(Long id);
}
