package com.myee.tarot.merchant.service;

import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.WhereRequest;
import com.myee.tarot.merchant.domain.MerchantStore;
import org.springframework.security.access.prepost.PreAuthorize;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Martin on 2016/4/11.
 */
public interface MerchantStoreService extends GenericEntityService<Long, MerchantStore> {

    MerchantStore getByCode(String storeCode);

//    Long getCountById(Long id);

//    List<MerchantStore> listByMerchant(Long id);

    PageResult<MerchantStore> pageListByMerchant(Long id,WhereRequest whereRequest ) throws InvocationTargetException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException;

    //@PreAuthorize("#merchantId < 1")//测试security，确实在方法上有效
    Long getCountById(Long merchantStoreId, Long merchantId);

    MerchantStore getByMerchantStoreName(String name);

    List<MerchantStore> listByMerchantId(Long id);

    List<MerchantStore> listByIds(List<Long> bindList);
}
