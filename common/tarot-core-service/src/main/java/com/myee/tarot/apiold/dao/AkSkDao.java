package com.myee.tarot.apiold.dao;

import com.myee.tarot.apiold.domain.AkSk;
import com.myee.tarot.core.dao.GenericEntityDao;

/**
 * Created by xiaoni on 2016/8/10.
 */
public interface AkSkDao extends GenericEntityDao<Long, AkSk> {
    AkSk getByToken(String token);
}
