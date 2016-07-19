package com.myee.tarot.log.service;

import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.log.domain.SelfCheckLog;

import java.util.List;

/**
 * Created by Ray.Fu on 2016/7/18.
 */
public interface SelfCheckLogService extends GenericEntityService<Long, SelfCheckLog> {

    public void uploadSelfCheckLog(SelfCheckLog selfCheckLog);

    PageResult<SelfCheckLog> pageAll(PageRequest pageRequest);
}
