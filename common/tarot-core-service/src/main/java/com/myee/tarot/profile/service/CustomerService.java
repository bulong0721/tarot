package com.myee.tarot.profile.service;

import com.myee.tarot.core.service.GenericEntityService;

/**
 * Created by Martin on 2016/4/11.
 */
public interface CustomerService extends GenericEntityService<Long, com.myee.tarot.profile.domain.Customer> {

    com.myee.tarot.profile.domain.Customer getByUsername(String username, boolean b);
}
