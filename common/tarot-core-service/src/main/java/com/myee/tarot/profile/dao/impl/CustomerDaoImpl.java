package com.myee.tarot.profile.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.profile.dao.CustomerDao;
import com.myee.tarot.profile.domain.Customer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Martin on 2016/4/21.
 */
@Component
public class CustomerDaoImpl  extends GenericEntityDaoImpl<Long, Customer> implements CustomerDao {

    @Override
    public Customer getByUsername(String username) {
        return null;
    }

    @Override
    public Customer getByUsername(String username, Boolean cacheable) {
        return null;
    }

    @Override
    public List<Customer> listByUsername(String username) {
        return null;
    }

    @Override
    public List<Customer> listByUsername(String username, Boolean cacheable) {
        return null;
    }

    @Override
    public Customer getByEmail(String emailAddress) {
        return null;
    }

    @Override
    public List<Customer> listByEmail(String emailAddress) {
        return null;
    }
}
