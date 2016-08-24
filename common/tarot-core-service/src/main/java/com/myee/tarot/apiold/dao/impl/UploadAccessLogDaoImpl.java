package com.myee.tarot.apiold.dao.impl;

import com.myee.tarot.apiold.dao.UploadAccessLogDao;
import com.myee.tarot.apiold.domain.UploadAccessLog;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * Created by Chay on 2016/8/10.
 */
@Repository
public class UploadAccessLogDaoImpl extends GenericEntityDaoImpl<Long, UploadAccessLog> implements UploadAccessLogDao {
}
