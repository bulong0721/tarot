package com.myee.tarot.profile.dao.impl;

import com.myee.tarot.profile.domain.Address;
import com.myee.tarot.profile.dao.AddressDao;

import org.springframework.stereotype.Repository;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;

@Repository
public class AddressDaoImpl extends GenericEntityDaoImpl<java.lang.Long, Address> implements AddressDao {

}

