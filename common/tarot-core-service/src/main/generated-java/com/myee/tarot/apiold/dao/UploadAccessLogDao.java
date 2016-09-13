package com.myee.tarot.apiold.dao;

import com.myee.tarot.apiold.domain.UploadAccessLog;
import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.WhereRequest;

/**
 * Created by Chay on 2016/8/10.
 */
public interface UploadAccessLogDao extends GenericEntityDao<Long, UploadAccessLog> {
    PageResult<UploadAccessLog> getLevel1Analysis(WhereRequest whereRequest);
}
