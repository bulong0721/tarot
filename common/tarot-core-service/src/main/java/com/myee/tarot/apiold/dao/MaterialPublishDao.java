package com.myee.tarot.apiold.dao;

import com.myee.tarot.apiold.domain.MaterialPublish;
import com.myee.tarot.core.dao.GenericEntityDao;

import java.util.Date;
import java.util.List;

/**
 * Created by Chay on 2016/8/10.
 */
public interface MaterialPublishDao extends GenericEntityDao<Long, MaterialPublish> {
    List<MaterialPublish> listByStore(Long storeId,Date now);
}
