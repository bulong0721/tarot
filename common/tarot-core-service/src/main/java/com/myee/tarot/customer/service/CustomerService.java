package com.myee.tarot.customer.service;

import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.customer.domain.Customer;

/**
 * Created by Martin on 2016/4/11.
 */
public interface CustomerService extends GenericEntityService<Long, Customer> {

    Customer getByUsername(String username);
}
