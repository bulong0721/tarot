package com.myee.tarot.log.service.impl;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.log.dao.SelfCheckLogDao;
import com.myee.tarot.log.domain.SelfCheckLog;
import com.myee.tarot.log.service.SelfCheckLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Ray.Fu on 2016/7/18.
 */
@Service
public class SelfCheckLogServiceImpl extends GenericEntityServiceImpl<Long, SelfCheckLog> implements SelfCheckLogService {

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
}
