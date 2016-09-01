package com.myee.tarot.weixin.service.impl;

import com.myee.tarot.weixin.domain.RInstructionLog;
import com.myee.tarot.weixin.dao.RInstructionLogDao;
import com.myee.tarot.weixin.service.RInstructionLogService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.myee.tarot.core.service.GenericEntityServiceImpl;

@Service
public class RInstructionLogServiceImpl extends GenericEntityServiceImpl<java.lang.Long, RInstructionLog> implements RInstructionLogService {

    protected RInstructionLogDao dao;

    @Autowired
    public RInstructionLogServiceImpl(RInstructionLogDao dao) {
        super(dao);
        this.dao = dao;
    }

}

