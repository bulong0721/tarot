package com.myee.tarot.catalog.service.impl;

import com.myee.tarot.catalog.domain.ProductUsed;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.catalog.dao.ProductUsedDao;
import com.myee.tarot.catalog.service.ProductUsedService;
import com.myee.tarot.core.util.WhereRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public PageResult<ProductUsed> pageByStore(Long id, WhereRequest whereRequest){
        return productUsedDao.pageByStore(id, whereRequest);
    }

    @Override
    public List<ProductUsed> listByIDs(List<Long> idList){
        return productUsedDao.listByIDs(idList);
    }

    @Override
    public ProductUsed getByCode(String code){
        return productUsedDao.getByCode( code);
    }

}
