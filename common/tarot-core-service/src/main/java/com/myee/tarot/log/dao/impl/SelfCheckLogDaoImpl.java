package com.myee.tarot.log.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.log.dao.SelfCheckLogDao;
import com.myee.tarot.log.domain.SelfCheckLog;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Ray.Fu on 2016/7/18.
 */
@Repository
public class SelfCheckLogDaoImpl extends GenericEntityDaoImpl<Long, SelfCheckLog> implements SelfCheckLogDao {

    @Override
    public void uploadSelfCheckLog(SelfCheckLog selfCheckLog) {
        this.update(selfCheckLog);
    }
}
