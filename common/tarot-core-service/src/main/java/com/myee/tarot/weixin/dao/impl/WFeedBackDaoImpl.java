package com.myee.tarot.weixin.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.weixin.dao.WFeedBackDao;
import com.myee.tarot.weixin.domain.WFeedBack;
import org.springframework.stereotype.Repository;

/**
 * Created by Ray.Fu on 2016/7/5.
 */
@Repository
public class WFeedBackDaoImpl extends GenericEntityDaoImpl<Long, WFeedBack> implements WFeedBackDao {
}
