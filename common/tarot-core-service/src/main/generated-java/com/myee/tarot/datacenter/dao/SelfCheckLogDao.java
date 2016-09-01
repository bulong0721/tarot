package com.myee.tarot.datacenter.dao;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.WhereRequest;
import com.myee.tarot.datacenter.domain.SelfCheckLog;

/**
 * Created by Ray.Fu on 2016/7/18.
 */
public interface SelfCheckLogDao extends GenericEntityDao<Long, SelfCheckLog> {

    public void uploadSelfCheckLog(SelfCheckLog selfCheckLog);

    PageResult<SelfCheckLog> pageAll(WhereRequest whereRequest);
}
