package com.myee.tarot.apiold.service;

import com.myee.tarot.apiold.domain.RollMainPublish;
import com.myee.tarot.core.service.GenericEntityService;

import java.util.Date;
import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
public interface RollMainPublishService extends GenericEntityService<Long, RollMainPublish> {
    List<RollMainPublish> listByStoreTime(Long orgId, Date now);
}
