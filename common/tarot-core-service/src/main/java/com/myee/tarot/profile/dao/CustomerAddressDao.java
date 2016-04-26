package com.myee.tarot.profile.dao;


import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.profile.domain.CustomerAddress;

import java.util.List;

/**
 * Created by Martin on 2016/4/11.
 */
public interface CustomerAddressDao extends GenericEntityDao<Long, CustomerAddress> {

    List<CustomerAddress> listActiveByCustomerId(Long customerId);

    void makeDefault(Long customerAddressId, Long customerId);

    CustomerAddress getDefault(Long customerId);

}
