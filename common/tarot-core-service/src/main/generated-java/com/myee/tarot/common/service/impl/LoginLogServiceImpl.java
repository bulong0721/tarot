package com.myee.tarot.common.service.impl;

import com.myee.tarot.common.dao.LoginLogDao;
import com.myee.tarot.common.domain.LoginLog;
import com.myee.tarot.common.service.LoginLogService;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * Project Name : tarot
 * User: Jelynn
 * Date: 2016/12/16
 * Time: 16:58
 * Describe:
 * Version:1.0
 */
@Service
public class LoginLogServiceImpl extends GenericEntityServiceImpl<Long, LoginLog> implements LoginLogService {

	protected LoginLogDao dao;

	@Autowired
	public LoginLogServiceImpl(LoginLogDao dao) {
		super(dao);
		this.dao = dao;
	}

}
