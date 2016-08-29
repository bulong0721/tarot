package com.myee.tarot.apiold.dao.impl;

import com.myee.tarot.apiold.dao.TasteDao;
import com.myee.tarot.apiold.domain.TasteInfo;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * Created by Chay on 2016/8/10.
 */
@Repository
public class TasteDaoImpl extends GenericEntityDaoImpl<Long, TasteInfo> implements TasteDao {
}
