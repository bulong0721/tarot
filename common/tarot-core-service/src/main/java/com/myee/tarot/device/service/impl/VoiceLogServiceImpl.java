package com.myee.tarot.device.service.impl;

import com.myee.tarot.catalog.domain.VoiceLog;
import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.device.dao.VoiceLogDao;
import com.myee.tarot.device.service.VoiceLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Ray.Fu on 2016/8/11.
 */
@Service
public class VoiceLogServiceImpl extends GenericEntityServiceImpl<Long, VoiceLog> implements VoiceLogService {

    private VoiceLogDao voiceLogDao;

    @Autowired
    public VoiceLogServiceImpl(VoiceLogDao voiceLogDao) {
        super(voiceLogDao);
        this.voiceLogDao = voiceLogDao;
    }
}
