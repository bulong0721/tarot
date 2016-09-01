package com.myee.tarot.profile.dao.impl;

import com.myee.tarot.profile.domain.Locale;
import com.myee.tarot.profile.dao.LocaleDao;

import org.springframework.stereotype.Repository;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;

@Repository
public class LocaleDaoImpl extends GenericEntityDaoImpl<java.lang.String, Locale> implements LocaleDao {

}

