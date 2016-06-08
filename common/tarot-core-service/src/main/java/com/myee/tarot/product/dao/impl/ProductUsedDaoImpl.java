package com.myee.tarot.product.dao.impl;

import com.myee.tarot.catalog.domain.ProductUsed;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.product.dao.ProductUsedDao;
import org.springframework.stereotype.Repository;

/**
 * Created by Enva on 2016/6/1.
 */
@Repository
public class ProductUsedDaoImpl extends GenericEntityDaoImpl<Long, ProductUsed> implements ProductUsedDao {

}
