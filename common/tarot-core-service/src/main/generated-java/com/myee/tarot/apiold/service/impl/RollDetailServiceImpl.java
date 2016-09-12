package com.myee.tarot.apiold.service.impl;

import com.myee.tarot.apiold.dao.RollDetailDao;
import com.myee.tarot.apiold.domain.RollDetail;
import com.myee.tarot.apiold.service.RollDetailService;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Chay on 2016/8/10.
 */
@Service
public class RollDetailServiceImpl extends GenericEntityServiceImpl<Long, RollDetail> implements RollDetailService {

    protected RollDetailDao rollDetailDao;

    @Autowired
    public RollDetailServiceImpl(RollDetailDao rollDetailDao) {
        super(rollDetailDao);
        this.rollDetailDao = rollDetailDao;
    }

    public void deleteByRollMain(Long rollMainId){
        this.rollDetailDao.deleteByRollMain(rollMainId);
    }
}
