package com.myee.tarot.product.service.impl;

import com.myee.tarot.catalog.domain.ProductUsed;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.product.dao.ProductUsedDao;
import com.myee.tarot.product.service.ProductUsedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Enva on 2016/6/1.
 */
@Service
public class ProductUsedServiceImpl extends GenericEntityServiceImpl<Long, ProductUsed> implements ProductUsedService {

    protected ProductUsedDao productUsedDao;

    @Autowired
    public ProductUsedServiceImpl(ProductUsedDao productUsedDao) {
        super(productUsedDao);
        this.productUsedDao = productUsedDao;
    }

    @Override
    public PageResult<ProductUsed> pageList(PageRequest pageRequest){
        return productUsedDao.pageList(pageRequest);
    }

    @Override
    public PageResult<ProductUsed> pageListByStore(PageRequest pageRequest, Long id){
        return productUsedDao.pageListByStore(pageRequest,id);
    }

}
