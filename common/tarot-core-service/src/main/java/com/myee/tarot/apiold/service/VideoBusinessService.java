package com.myee.tarot.apiold.service;

import com.myee.tarot.apiold.domain.VideoBusiness;
import com.myee.tarot.core.service.GenericEntityService;

import java.util.Date;
import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
public interface VideoBusinessService extends GenericEntityService<Long, VideoBusiness> {
    List<VideoBusiness> listByStore(Long storeId, Date now);

    VideoBusiness findByIdType(Long vBId, Integer type);
}
