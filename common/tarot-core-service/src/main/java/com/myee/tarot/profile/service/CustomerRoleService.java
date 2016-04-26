package com.myee.tarot.profile.service;

import com.myee.tarot.core.service.GenericEntityService;

import java.util.List;

/**
 * Created by Martin on 2016/4/11.
 */
public interface CustomerRoleService extends GenericEntityService<Long, com.myee.tarot.profile.domain.CustomerRole> {

    List<com.myee.tarot.profile.domain.CustomerRole> listByCustomerId(Long customerId);
}
