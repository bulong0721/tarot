package com.myee.tarot.campaign.service.impl;

import com.myee.tarot.campaign.dao.ModeSwitchDao;
import com.myee.tarot.campaign.domain.ModeSwitch;
import com.myee.tarot.campaign.service.ModeSwitchService;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/8/2.
 */
@Service
public class ModeSwitchServiceImpl extends GenericEntityServiceImpl<Long, ModeSwitch> implements ModeSwitchService{

    @Autowired
    private ModeSwitchDao modeSwitchDao;

    @Autowired
    private ModeSwitchServiceImpl(ModeSwitchDao modeSwitchDao){
        super(modeSwitchDao);
        this.modeSwitchDao = modeSwitchDao;
    }


    @Override
    public ModeSwitch findByStoreId(Long storeId) {
        return modeSwitchDao.findByStoreId(storeId);
    }
}
