package com.myee.tarot.customer.dao;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.customer.domain.Customer;

import java.util.List;

/**
 * Created by Martin on 2016/4/11.
 */
public interface CustomerDao extends GenericEntityDao<Long, Customer> {

    Customer getByUsername(String username);

    Customer getByUsername(String username, Boolean cacheable);

    List<Customer> listByUsername(String username);

    List<Customer> listByUsername(String username, Boolean cacheable);

    Customer getByEmail(String emailAddress);

    List<Customer> listByEmail(String emailAddress);

    PageResult<Customer> pageByStore(Long storeId, PageRequest pageRequest);
}
