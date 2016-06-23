package com.myee.tarot.product.dao;

import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.catalog.domain.ProductUsed;
import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;

import java.util.List;
import java.util.Set;

/**
 * Created by Enva on 2016/6/1.
 */
public interface ProductUsedDao extends GenericEntityDao<Long, ProductUsed> {

    PageResult<ProductUsed> pageList(PageRequest pageRequest);

    PageResult<ProductUsed> pageListByStore(PageRequest pageRequest, Long id,int isPaging);

    List<ProductUsed> listByIDs(List<Long>idList);
}
