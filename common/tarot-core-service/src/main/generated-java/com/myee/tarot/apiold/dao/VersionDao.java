package com.myee.tarot.apiold.dao;

import com.myee.tarot.apiold.domain.VersionInfo;
import com.myee.tarot.core.dao.GenericEntityDao;

/**
 * Created by Chay on 2016/8/10.
 */
public interface VersionDao extends GenericEntityDao<Long, VersionInfo> {
    VersionInfo getByStoreId(Long shopId);
}
