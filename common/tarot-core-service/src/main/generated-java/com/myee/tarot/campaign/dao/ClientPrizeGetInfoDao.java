package com.myee.tarot.campaign.dao;

import com.myee.tarot.clientprize.domain.ClientPrizeGetInfo;
import com.myee.tarot.core.dao.GenericEntityDao;

import java.util.Date;

/**
 * Created by Administrator on 2016/8/29.
 */
public interface ClientPrizeGetInfoDao extends GenericEntityDao<Long, ClientPrizeGetInfo> {

    long countByPhoneToday(Long phone,Date startTime,Date endTime);

    ClientPrizeGetInfo findByIdAndDeskId(Long id,String deskId);
}
