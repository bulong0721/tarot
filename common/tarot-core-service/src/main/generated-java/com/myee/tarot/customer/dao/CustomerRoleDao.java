package com.myee.tarot.customer.dao;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.customer.domain.CustomerRole;

import java.util.List;

/**
 * Created by Martin on 2016/4/11.
 */
public interface CustomerRoleDao extends GenericEntityDao<Long, CustomerRole> {

    List<CustomerRole> listByCustomerId(Long customerId);

    CustomerRole getByName(String roleName);
}
