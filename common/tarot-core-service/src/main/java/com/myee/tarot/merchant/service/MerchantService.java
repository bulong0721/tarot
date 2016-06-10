package com.myee.tarot.merchant.service;

import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.merchant.domain.Merchant;

/**
 * Created by Chay on 2016/5/25.
 */
public interface MerchantService extends GenericEntityService<Long, Merchant> {

    Long getCountById(Long id);

}
