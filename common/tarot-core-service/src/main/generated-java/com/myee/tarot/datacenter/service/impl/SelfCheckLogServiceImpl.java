package com.myee.tarot.datacenter.service.impl;

import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.WhereRequest;
import com.myee.tarot.datacenter.dao.SelfCheckLogDao;
import com.myee.tarot.datacenter.domain.SelfCheckLog;
import com.myee.tarot.datacenter.service.SelfCheckLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Ray.Fu on 2016/7/18.
 */
@Service
public class SelfCheckLogServiceImpl extends GenericEntityServiceImpl<Long, SelfCheckLog> implements SelfCheckLogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SelfCheckLogServiceImpl.class);

    private SelfCheckLogDao selfCheckLogDao;

    @Autowired
    public SelfCheckLogServiceImpl(SelfCheckLogDao selfCheckLogDao) {
        super(selfCheckLogDao);
        this.selfCheckLogDao = selfCheckLogDao;
    }

    @Override
    public void uploadSelfCheckLog(SelfCheckLog selfCheckLog) {
        selfCheckLogDao.uploadSelfCheckLog(selfCheckLog);
    }

    @Override
    public PageResult<SelfCheckLog> page(WhereRequest whereRequest) {
        return selfCheckLogDao.pageAll(whereRequest);
    }
}
