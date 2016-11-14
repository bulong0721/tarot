package com.myee.tarot.merchant.service.impl;

import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.merchant.dao.MerchantDao;
import com.myee.tarot.merchant.domain.Merchant;
import com.myee.tarot.merchant.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Chay on 2016/5/25.
 */
@Service
public class MerchantServiceImpl extends GenericEntityServiceImpl<Long, Merchant> implements MerchantService {

    protected MerchantDao merchantDao;

    @Autowired
    public MerchantServiceImpl(MerchantDao merchantDao) {
        super(merchantDao);
        this.merchantDao = merchantDao;
    }

    @Override
    public Long getCountById(Long id) {
        return merchantDao.getCountById(id);
    }

    @Override
    public PageResult<Merchant> pageList(PageRequest pageRequest){
        return merchantDao.pageList(pageRequest);
    }

    @Override
    public Merchant getByMerchantName(String name) {
        return merchantDao.getByMerchantName(name);
    }

}
