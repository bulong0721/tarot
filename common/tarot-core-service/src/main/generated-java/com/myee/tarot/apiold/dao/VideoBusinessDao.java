package com.myee.tarot.apiold.dao;

import com.myee.tarot.apiold.domain.VideoBusiness;
import com.myee.tarot.core.dao.GenericEntityDao;

import java.util.Date;
import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
public interface VideoBusinessDao extends GenericEntityDao<Long, VideoBusiness> {
    List<VideoBusiness> listByStore(Long storeId, Date now);

    VideoBusiness findByIdType(Long vBId, Integer type);
}
