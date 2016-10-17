package com.myee.tarot.remote.service.impl;

import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.metrics.domain.AppInfo;
import com.myee.tarot.remote.dao.AppInfoDao;
import com.myee.tarot.remote.service.AppInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Chay on 2016/8/10.
 */
@Service
public class AppInfoServiceImpl extends GenericEntityServiceImpl<Long, AppInfo> implements AppInfoService {

    protected AppInfoDao appInfoDao;

    @Autowired
    public AppInfoServiceImpl(AppInfoDao appInfoDao) {
        super(appInfoDao);
        this.appInfoDao = appInfoDao;
    }
}
