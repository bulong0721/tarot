package com.myee.tarot.campaign.service;

import com.myee.tarot.campaign.domain.ModeSwitch;
import com.myee.tarot.core.service.GenericEntityService;

/**
 * Created by Administrator on 2016/8/2.
 */
public interface ModeSwitchService extends GenericEntityService<Long, ModeSwitch>{

    ModeSwitch findByStoreId(Long storeId);
}
