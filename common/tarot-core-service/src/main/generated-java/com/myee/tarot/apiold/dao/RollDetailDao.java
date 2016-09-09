package com.myee.tarot.apiold.dao;

import com.myee.tarot.apiold.domain.RollDetail;
import com.myee.tarot.core.dao.GenericEntityDao;

/**
 * Created by Chay on 2016/8/10.
 */
public interface RollDetailDao extends GenericEntityDao<Long, RollDetail> {
    void deleteByRollMain(Long rollMainId);
}
