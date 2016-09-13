package com.myee.tarot.apiold.service;

import com.myee.tarot.apiold.domain.UploadAccessLog;
import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.WhereRequest;

/**
 * Created by Chay on 2016/8/10.
 */
public interface UploadAccessLogService extends GenericEntityService<Long, UploadAccessLog> {
    PageResult<UploadAccessLog> getLevel1Analysis(WhereRequest whereRequest);
}
