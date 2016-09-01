package com.myee.tarot.catalog.service;

import com.myee.tarot.catalog.domain.ProductUsed;
import com.myee.tarot.catalog.domain.ProductUsedAttribute;
import com.myee.tarot.core.service.GenericEntityService;

import java.util.List;

/**
 * Created by Enva on 2016/6/1.
 */
public interface ProductUsedAttributeService extends GenericEntityService<Long, ProductUsedAttribute> {

    List<ProductUsedAttribute> listByProductId(Long id);

    void deleteByProductUsedId(Long id);
}
