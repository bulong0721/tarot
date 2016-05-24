package com.myee.tarot.merchant.service.impl;

import com.myee.tarot.admin.dao.AdminUserDao;
import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.admin.service.AdminUserService;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.core.service.GenericResponse;
import com.myee.tarot.merchant.dao.MerchantStoreDao;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.merchant.service.MerchantStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public MerchantStore getByCode(String storeCode) {
        return null;
    }
}
