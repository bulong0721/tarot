package com.myee.tarot.datacenter.dao;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.datacenter.domain.EventLevel;

import java.util.List;

/**
 * Created by Ray.Fu on 2016/7/19.
 */
public interface EventLevelLogDao extends GenericEntityDao<Long, EventLevel> {

    public List getEventLevelList();
}
