package com.myee.tarot.merchant.dao;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.WhereRequest;
import com.myee.tarot.merchant.domain.MerchantStore;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Martin on 2016/4/11.
 */
public interface MerchantStoreDao extends GenericEntityDao<Long, MerchantStore> {

//    Long getCountById(Long id);

//    List<MerchantStore> listByMerchant(Long id);

    PageResult<MerchantStore> pageListByMerchant(Long id ,WhereRequest whereRequest) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException;

    Long getCountById(Long merchantStoreId, Long merchantId);

    public MerchantStore findById(Long merchanStoreId);

    public List<MerchantStore> listByMerchantId(Long merchantId);

    MerchantStore getByCode(String storeCode);

    MerchantStore getByMerchantStoreName(String name);
}
