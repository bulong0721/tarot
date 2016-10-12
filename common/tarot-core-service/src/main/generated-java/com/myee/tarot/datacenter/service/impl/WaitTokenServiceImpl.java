package com.myee.tarot.datacenter.service.impl;

import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.core.util.DateTimeUtils;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.WhereRequest;
import com.myee.tarot.datacenter.dao.WaitTokenDao;
import com.myee.tarot.datacenter.service.WaitTokenService;
import com.myee.tarot.wechat.domain.WxWaitToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Jelynn on 2016/7/20.
 */
@Service
public class WaitTokenServiceImpl extends GenericEntityServiceImpl<Long, WxWaitToken> implements WaitTokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WaitTokenServiceImpl.class);

    protected WaitTokenDao waitTokenDao;

    @Autowired
    public WaitTokenServiceImpl(WaitTokenDao waitTokenDao) {
        super(waitTokenDao);
        this.waitTokenDao = waitTokenDao;
    }

    @Override
    public PageResult<WxWaitToken> page(WhereRequest whereRequest) {
        return waitTokenDao.page(whereRequest);
    }

    @Override
    public WxWaitToken findByShopIdAndTokenToday(Long shopId, String token) {
        Date startToday = DateTimeUtils.startToday();
        Date endToday = DateTimeUtils.endToday();
        WxWaitToken wxWaitToken = waitTokenDao.findByShopIdAndTokenToday(shopId, token, startToday, endToday);
        return wxWaitToken;
    }
}
