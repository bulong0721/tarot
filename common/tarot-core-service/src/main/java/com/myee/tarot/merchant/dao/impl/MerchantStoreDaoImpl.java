package com.myee.tarot.merchant.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.merchant.dao.MerchantStoreDao;
import com.myee.tarot.merchant.domain.MerchantStore;
import org.springframework.stereotype.Repository;

/**
 * Created by Martin on 2016/4/21.
 */
@Repository
public class MerchantStoreDaoImpl extends GenericEntityDaoImpl<Long, MerchantStore> implements MerchantStoreDao {
}
