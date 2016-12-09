package com.myee.tarot.merchant.service.impl;

import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.WhereRequest;
import com.myee.tarot.merchant.dao.MerchantStoreDao;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.merchant.service.MerchantStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Martin on 2016/4/21.
 */
@Service
public class MerchantStoreServiceImpl extends GenericEntityServiceImpl<Long, MerchantStore> implements MerchantStoreService {

    protected MerchantStoreDao storeDao;

    @Autowired
    public MerchantStoreServiceImpl(MerchantStoreDao storeDao) {
        super(storeDao);
        this.storeDao = storeDao;
    }

//    @Override
//    public Long getCountById(Long id) {
//        return storeDaoDao.getCountById(id);
//    }

    @Override
    public Long getCountById(Long merchantStoreId, Long merchantId) {
        return storeDao.getCountById(merchantStoreId, merchantId);
    }

    @Override
    public MerchantStore getByMerchantStoreName(String name) {
        return storeDao.getByMerchantStoreName(name);
    }

    @Override
    public List<MerchantStore> listByMerchantId(Long id) {
        return storeDao.listByMerchantId(id);
    }

    @Override
    public List<MerchantStore> listByIds(List<Long> bindList) {
        return storeDao.listByIds(bindList);
    }


//    @Override
//    public List<MerchantStore> listByMerchant(Long id){
//        return storeDaoDao.listByMerchant(id);
//    }

    @Override
    public MerchantStore getByCode(String storeCode) {
        return storeDao.getByCode(storeCode);
    }

    @Override
    public PageResult<MerchantStore> pageListByMerchant(Long id, WhereRequest whereRequest) throws InvocationTargetException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException {
        return storeDao.pageListByMerchant(id, whereRequest);
    }
}
