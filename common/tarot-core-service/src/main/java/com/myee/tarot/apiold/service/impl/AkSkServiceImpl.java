package com.myee.tarot.apiold.service.impl;

import com.myee.tarot.apiold.dao.AkSkDao;
import com.myee.tarot.apiold.domain.AkSk;
import com.myee.tarot.apiold.service.AkSkService;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Chay on 2016/8/10.
 */
@Service
public class AkSkServiceImpl extends GenericEntityServiceImpl<Long, AkSk> implements AkSkService {

    protected AkSkDao akSkDao;

    @Autowired
    public AkSkServiceImpl(AkSkDao akSkDao) {
        super(akSkDao);
        this.akSkDao = akSkDao;
    }

    public AkSk getByToken(String token){
        return akSkDao.getByToken(token);
    }
}
