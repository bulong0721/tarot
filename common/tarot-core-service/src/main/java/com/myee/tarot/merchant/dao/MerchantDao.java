package com.myee.tarot.merchant.dao;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.merchant.domain.Merchant;

/**
 * Created by Chay on 2016/5/25.
 */
public interface MerchantDao extends GenericEntityDao<Long, Merchant> {

    Long getCountById(Merchant merchant);
}
