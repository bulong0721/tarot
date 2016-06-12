package com.myee.tarot.product.dao;

import com.myee.tarot.catalog.domain.ProductUsed;
import com.myee.tarot.catalog.domain.ProductUsedAttribute;
import com.myee.tarot.core.dao.GenericEntityDao;

import java.util.List;

/**
 * Created by Enva on 2016/6/1.
 */
public interface ProductUsedAttributeDao extends GenericEntityDao<Long, ProductUsedAttribute> {

    public List<ProductUsedAttribute> listByProductId(Long id);
}
