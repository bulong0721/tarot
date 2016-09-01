package com.myee.tarot.apiold.service;

import com.myee.tarot.apiold.domain.MaterialPublish;
import com.myee.tarot.core.service.GenericEntityService;

import java.util.Date;
import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
public interface MaterialPublishService extends GenericEntityService<Long, MaterialPublish> {
    List<MaterialPublish> listByStoreTime(Long storeId, Date now);
}
