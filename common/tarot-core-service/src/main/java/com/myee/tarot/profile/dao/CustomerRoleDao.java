package com.myee.tarot.profile.dao;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.profile.domain.CustomerRole;

import java.util.List;

/**
 * Created by Martin on 2016/4/11.
 */
public interface CustomerRoleDao extends GenericEntityDao<Long, CustomerRole> {

    List<CustomerRole> listByCustomerId(Long customerId);
}
