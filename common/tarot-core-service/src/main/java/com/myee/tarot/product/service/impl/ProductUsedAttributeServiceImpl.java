package com.myee.tarot.product.service.impl;

import com.myee.tarot.catalog.domain.ProductUsed;
import com.myee.tarot.catalog.domain.ProductUsedAttribute;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.product.dao.ProductUsedAttributeDao;
import com.myee.tarot.product.dao.ProductUsedDao;
import com.myee.tarot.product.service.ProductUsedAttributeService;
import com.myee.tarot.product.service.ProductUsedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Enva on 2016/6/1.
 */
@Service
public class ProductUsedAttributeServiceImpl extends GenericEntityServiceImpl<Long, ProductUsedAttribute> implements ProductUsedAttributeService {

    protected ProductUsedAttributeDao productUsedAttributeDao;

    @Autowired
    public ProductUsedAttributeServiceImpl(ProductUsedAttributeDao productUsedAttributeDao) {
        super(productUsedAttributeDao);
        this.productUsedAttributeDao = productUsedAttributeDao;
    }

}
