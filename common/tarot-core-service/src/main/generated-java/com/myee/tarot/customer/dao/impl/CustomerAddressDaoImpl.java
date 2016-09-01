package com.myee.tarot.customer.dao.impl;

import com.myee.tarot.customer.domain.CustomerAddress;
import com.myee.tarot.customer.dao.CustomerAddressDao;

import org.springframework.stereotype.Repository;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;

import java.util.List;

@Repository
public class CustomerAddressDaoImpl extends GenericEntityDaoImpl<Long, CustomerAddress> implements CustomerAddressDao {

    @Override
    public List<CustomerAddress> listActiveByCustomerId(Long customerId) {
        return null;
    }

    @Override
    public void makeDefault(Long customerAddressId, Long customerId) {

    }

    @Override
    public CustomerAddress getDefault(Long customerId) {
        return null;
    }
}

