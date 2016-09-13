package com.myee.tarot.apiold.service.impl;

import com.myee.tarot.apiold.dao.UploadAccessLogDao;
import com.myee.tarot.apiold.domain.UploadAccessLog;
import com.myee.tarot.apiold.service.UploadAccessLogService;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.WhereRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Chay on 2016/8/10.
 */
@Service
public class UploadAccessLogServiceImpl extends GenericEntityServiceImpl<Long, UploadAccessLog> implements UploadAccessLogService {

    protected UploadAccessLogDao uploadAccessLogDao;

    @Autowired
    public UploadAccessLogServiceImpl(UploadAccessLogDao uploadAccessLogDao) {
        super(uploadAccessLogDao);
        this.uploadAccessLogDao = uploadAccessLogDao;
    }

    @Override
    public PageResult<UploadAccessLog> getLevel1Analysis(WhereRequest whereRequest) {
        return uploadAccessLogDao.getLevel1Analysis(whereRequest);
    }
}
