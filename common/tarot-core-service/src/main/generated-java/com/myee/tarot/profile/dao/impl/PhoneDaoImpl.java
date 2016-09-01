package com.myee.tarot.profile.dao.impl;

import com.myee.tarot.profile.domain.Phone;
import com.myee.tarot.profile.dao.PhoneDao;

import org.springframework.stereotype.Repository;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;

@Repository
public class PhoneDaoImpl extends GenericEntityDaoImpl<java.lang.Long, Phone> implements PhoneDao {

}

