package com.myee.tarot.campaign.service;

import com.myee.tarot.clientprize.domain.ClientPrizeGetInfo;
import com.myee.tarot.core.service.GenericEntityService;

/**
 * Created by Administrator on 2016/8/29.
 */
public interface ClientPrizeGetInfoService extends GenericEntityService<Long, ClientPrizeGetInfo>{

    boolean isOverThreeTimes(Long phoneNum);

    ClientPrizeGetInfo findByIdAndDeskId(Long prizeGetId, String deskId);

}
