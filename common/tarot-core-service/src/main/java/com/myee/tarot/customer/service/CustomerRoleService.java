package com.myee.tarot.customer.service;

import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.customer.domain.CustomerRole;

import java.util.List;

/**
 * Created by Martin on 2016/4/11.
 */
public interface CustomerRoleService extends GenericEntityService<Long, CustomerRole> {

    List<CustomerRole> listByCustomerId(Long customerId);
}
