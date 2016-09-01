package com.myee.tarot.apiold.service.impl;

import com.myee.tarot.apiold.dao.RollMainPublishDao;
import com.myee.tarot.apiold.domain.RollMainPublish;
import com.myee.tarot.apiold.service.RollMainPublishService;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
@Service
public class RollMainPublishServiceImpl extends GenericEntityServiceImpl<Long, RollMainPublish> implements RollMainPublishService {

    protected RollMainPublishDao rollMainPublishDao;

    @Autowired
    public RollMainPublishServiceImpl(RollMainPublishDao rollMainPublishDao) {
        super(rollMainPublishDao);
        this.rollMainPublishDao = rollMainPublishDao;
    }

    public List<RollMainPublish> listByStoreTime(Long orgId, Date now){
        return rollMainPublishDao.listByStoreTime(orgId, now);
    }
}
