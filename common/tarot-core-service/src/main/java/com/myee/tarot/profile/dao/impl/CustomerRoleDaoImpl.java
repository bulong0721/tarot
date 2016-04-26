package com.myee.tarot.profile.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.profile.dao.CustomerRoleDao;
import com.myee.tarot.profile.domain.CustomerRole;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Martin on 2016/4/21.
 */
@Service
public class CustomerRoleDaoImpl extends GenericEntityDaoImpl<Long, CustomerRole> implements CustomerRoleDao {

    @Override
    public List<CustomerRole> listByCustomerId(Long customerId) {
        return null;
    }
}
