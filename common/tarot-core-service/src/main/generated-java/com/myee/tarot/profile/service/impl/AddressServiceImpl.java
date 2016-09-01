package com.myee.tarot.profile.service.impl;

import com.myee.tarot.profile.domain.Address;
import com.myee.tarot.profile.dao.AddressDao;
import com.myee.tarot.profile.service.AddressService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.myee.tarot.core.service.GenericEntityServiceImpl;

@Service
public class AddressServiceImpl extends GenericEntityServiceImpl<java.lang.Long, Address> implements AddressService {

    protected AddressDao dao;

    @Autowired
    public AddressServiceImpl(AddressDao dao) {
        super(dao);
        this.dao = dao;
    }

}

