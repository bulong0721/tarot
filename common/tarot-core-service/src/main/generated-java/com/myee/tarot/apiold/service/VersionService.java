package com.myee.tarot.apiold.service;

import com.myee.tarot.apiold.domain.VersionInfo;
import com.myee.tarot.core.service.GenericEntityService;

/**
 * Created by Chay on 2016/8/10.
 */
public interface VersionService extends GenericEntityService<Long, VersionInfo> {
    VersionInfo getByStoreId(Long shopId);
}
