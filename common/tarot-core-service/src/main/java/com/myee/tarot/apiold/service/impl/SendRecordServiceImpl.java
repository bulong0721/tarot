package com.myee.tarot.apiold.service.impl;

import com.myee.tarot.apiold.dao.SendRecordDao;
import com.myee.tarot.apiold.domain.SendRecord;
import com.myee.tarot.apiold.service.SendRecordService;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xiaoni on 2016/8/10.
 */
@Service
public class SendRecordServiceImpl extends GenericEntityServiceImpl<Long, SendRecord> implements SendRecordService {

    protected SendRecordDao sendRecordDao;

    @Autowired
    public SendRecordServiceImpl(SendRecordDao sendRecordDao) {
        super(sendRecordDao);
        this.sendRecordDao = sendRecordDao;
    }
}
