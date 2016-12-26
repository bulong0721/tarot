package com.myee.tarot.common.dao.impl;

import com.myee.tarot.common.dao.LoginLogDao;
import com.myee.tarot.common.domain.LoginLog;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * Project Name : tarot
 * User: Jelynn
 * Date: 2016/12/16
 * Time: 17:00
 * Describe:
 * Version:1.0
 */
@Repository
public class LoginLogDaoImpl extends GenericEntityDaoImpl<Long, LoginLog> implements LoginLogDao {
}
