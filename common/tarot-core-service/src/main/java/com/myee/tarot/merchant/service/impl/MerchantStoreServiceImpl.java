package com.myee.tarot.merchant.service.impl;

import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.merchant.dao.MerchantStoreDao;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.merchant.service.MerchantStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Martin on 2016/4/21.
 */
@Service
public class MerchantStoreServiceImpl extends GenericEntityServiceImpl<Long, MerchantStore> implements MerchantStoreService {

    protected MerchantStoreDao storeDaoDao;

    @Autowired
    public MerchantStoreServiceImpl(MerchantStoreDao storeDaoDao) {
        super(storeDaoDao);
        this.storeDaoDao = storeDaoDao;
    }

    @Override
    public Long getCountById(Long id) {
        return storeDaoDao.getCountById(id);
    }

    public Long getCountById(Long merchantStoreId, Long merchantId){
        return storeDaoDao.getCountById(merchantStoreId, merchantId);
    }

    @Override
    public List<MerchantStore> listByMerchant(Long id){
        return storeDaoDao.listByMerchant(id);
    }

    @Override
    public MerchantStore getByCode(String storeCode) {
        return null;
    }

    @Override
    public PageResult<MerchantStore> pageListByMerchant(PageRequest pageRequest, Long id){
        return storeDaoDao.pageListByMerchant(pageRequest, id);
    }
}
