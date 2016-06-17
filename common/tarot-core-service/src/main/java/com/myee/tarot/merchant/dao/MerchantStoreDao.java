package com.myee.tarot.merchant.dao;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.merchant.domain.MerchantStore;

import java.util.List;

/**
 * Created by Martin on 2016/4/11.
 */
public interface MerchantStoreDao extends GenericEntityDao<Long, MerchantStore> {

    Long getCountById(Long id);

    List<MerchantStore> listByMerchant(Long id);

    PageResult<MerchantStore> pageListByMerchant(PageRequest pageRequest, Long id);

    Long getCountById(Long merchantStoreId, Long merchantId);
}
