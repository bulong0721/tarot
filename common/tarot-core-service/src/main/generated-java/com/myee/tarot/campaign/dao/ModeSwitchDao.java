package com.myee.tarot.campaign.dao;

import com.myee.tarot.campaign.domain.ModeSwitch;
import com.myee.tarot.core.dao.GenericEntityDao;

/**
 * Created by Administrator on 2016/8/2.
 */
public interface ModeSwitchDao extends GenericEntityDao<Long, ModeSwitch>{

    ModeSwitch findByStoreId(Long storeId);
}
