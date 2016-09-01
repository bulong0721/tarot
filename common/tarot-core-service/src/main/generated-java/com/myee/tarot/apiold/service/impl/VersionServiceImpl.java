package com.myee.tarot.apiold.service.impl;

import com.myee.tarot.apiold.dao.VersionDao;
import com.myee.tarot.apiold.domain.VersionInfo;
import com.myee.tarot.apiold.service.VersionService;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Chay on 2016/8/10.
 */
@Service
public class VersionServiceImpl extends GenericEntityServiceImpl<Long, VersionInfo> implements VersionService {

    protected VersionDao versionDao;

    @Autowired
    public VersionServiceImpl(VersionDao versionDao) {
        super(versionDao);
        this.versionDao = versionDao;
    }

    public VersionInfo getByStoreId(Long shopId){
        return  versionDao.getByStoreId(shopId);
    }
}
