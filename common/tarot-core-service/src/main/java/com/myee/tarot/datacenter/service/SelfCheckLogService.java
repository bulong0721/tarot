package com.myee.tarot.datacenter.service;

import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.WhereRequest;
import com.myee.tarot.datacenter.domain.SelfCheckLog;

/**
 * Created by Ray.Fu on 2016/7/18.
 */
public interface SelfCheckLogService extends GenericEntityService<Long, SelfCheckLog> {

    public void uploadSelfCheckLog(SelfCheckLog selfCheckLog);

    PageResult<SelfCheckLog> page(WhereRequest whereRequest);
}
