package com.myee.tarot.apiold.dao.impl;

import com.myee.tarot.apiold.dao.SendRecordDao;
import com.myee.tarot.apiold.domain.SendRecord;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Chay on 2016/8/10.
 */
@Repository
public class SendRecordDaoImpl extends GenericEntityDaoImpl<Long, SendRecord> implements SendRecordDao {
}
