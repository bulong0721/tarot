package com.myee.tarot.remote.service.impl;

import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.metrics.domain.SystemSummary;
import com.myee.tarot.remote.dao.SystemSummaryDao;
import com.myee.tarot.remote.service.SystemSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Chay on 2016/8/10.
 */
@Service
public class SystemSummaryServiceImpl extends GenericEntityServiceImpl<Long, SystemSummary> implements SystemSummaryService {

    protected SystemSummaryDao systemSummaryDao;

    @Autowired
    public SystemSummaryServiceImpl(SystemSummaryDao systemSummaryDao) {
        super(systemSummaryDao);
        this.systemSummaryDao = systemSummaryDao;
    }
}
