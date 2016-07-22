package com.myee.tarot.datacenter.service.impl;

import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.WhereRequest;
import com.myee.tarot.datacenter.dao.WaitTokenDao;
import com.myee.tarot.datacenter.service.WaitTokenService;
import com.myee.tarot.weixin.domain.WxWaitToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Jelynn on 2016/7/20.
 */
@Service
public class WaitTokenServiceImpl extends GenericEntityServiceImpl<Long, WxWaitToken> implements WaitTokenService {

    protected WaitTokenDao waitTokenDao;

    @Autowired
    public WaitTokenServiceImpl(WaitTokenDao waitTokenDao) {
        super(waitTokenDao);
        this.waitTokenDao = waitTokenDao;
    }

    @Override
    public PageResult<WxWaitToken> pageList(WhereRequest whereRequest) {
        return waitTokenDao.pageList(whereRequest);
    }
}
