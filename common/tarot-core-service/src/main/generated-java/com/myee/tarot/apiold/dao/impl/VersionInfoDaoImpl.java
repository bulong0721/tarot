package com.myee.tarot.apiold.dao.impl;

import com.myee.tarot.apiold.domain.VersionInfo;
import com.myee.tarot.apiold.dao.VersionInfoDao;

import org.springframework.stereotype.Repository;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;

@Repository
public class VersionInfoDaoImpl extends GenericEntityDaoImpl<java.lang.Long, VersionInfo> implements VersionInfoDao {

}

