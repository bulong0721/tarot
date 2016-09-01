package com.myee.tarot.customer.dao.impl;

import com.myee.tarot.customer.domain.CustomerAttribute;
import com.myee.tarot.customer.dao.CustomerAttributeDao;

import org.springframework.stereotype.Repository;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;

@Repository
public class CustomerAttributeDaoImpl extends GenericEntityDaoImpl<java.lang.Long, CustomerAttribute> implements CustomerAttributeDao {

}

