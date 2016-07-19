package com.myee.tarot.log.dao;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.log.domain.SelfCheckLog;

import java.util.List;

/**
 * Created by Ray.Fu on 2016/7/18.
 */
public interface SelfCheckLogDao extends GenericEntityDao<Long, SelfCheckLog> {

    public void uploadSelfCheckLog(SelfCheckLog selfCheckLog);

    PageResult<SelfCheckLog> pageAll(PageRequest pageRequest);
}
