package com.myee.tarot.remote.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.metrics.domain.SystemSummary;
import com.myee.tarot.remote.dao.SystemSummaryDao;
import org.springframework.stereotype.Repository;

/**
 * Created by Chay on 2016/8/10.
 */
@Repository
public class SystemSummaryDaoImpl extends GenericEntityDaoImpl<Long, SystemSummary> implements SystemSummaryDao {
}
