package com.myee.tarot.resource.service.impl;

import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.resource.dao.PushResourceDao;
import com.myee.tarot.resource.domain.PushResource;
import com.myee.tarot.resource.service.PushResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Ray.Fu on 2016/8/10.
 */
@Service
public class PushResourceServiceImpl extends GenericEntityServiceImpl<Long, PushResource> implements PushResourceService {

    protected PushResourceDao pushResourceDao;

    @Autowired
    public PushResourceServiceImpl(PushResourceDao pushResourceDao) {
        super(pushResourceDao);
        this.pushResourceDao = pushResourceDao;
    }
}
