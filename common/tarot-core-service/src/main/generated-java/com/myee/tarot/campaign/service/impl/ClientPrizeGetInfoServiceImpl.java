package com.myee.tarot.campaign.service.impl;

import com.myee.tarot.campaign.dao.ClientPrizeGetInfoDao;
import com.myee.tarot.campaign.service.ClientPrizeGetInfoService;
import com.myee.tarot.campaign.service.impl.redis.DateTimeUtils;
import com.myee.tarot.clientprize.domain.ClientPrizeGetInfo;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by Administrator on 2016/8/29.
 */
@Service
public class ClientPrizeGetInfoServiceImpl extends GenericEntityServiceImpl<Long, ClientPrizeGetInfo> implements ClientPrizeGetInfoService{

    protected ClientPrizeGetInfoDao clientPrizeGetInfoDao;

    @Autowired
    public ClientPrizeGetInfoServiceImpl(ClientPrizeGetInfoDao clientPrizeGetInfoDao) {
        super(clientPrizeGetInfoDao);
        this.clientPrizeGetInfoDao = clientPrizeGetInfoDao;
    }

    @Override
    public boolean isOverThreeTimes(Long phoneNum) {
        Date startTime = DateTimeUtils.startToday();
        Date endTime = DateTimeUtils.endToday();
        long count = clientPrizeGetInfoDao.countByPhoneToday(phoneNum,startTime,endTime);
        if(count > 3) {
            return true;
        }else {
            return false;
        }
    }

    @Override
    public ClientPrizeGetInfo findByIdAndDeskId(Long prizeGetId, String deskId) {
        return clientPrizeGetInfoDao.findByIdAndDeskId(prizeGetId, deskId);
    }

}
