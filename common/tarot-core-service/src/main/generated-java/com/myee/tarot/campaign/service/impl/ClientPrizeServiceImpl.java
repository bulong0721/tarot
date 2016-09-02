package com.myee.tarot.campaign.service.impl;

import com.myee.tarot.campaign.dao.ClientPrizeDao;
import com.myee.tarot.campaign.dao.MerchantActivityDao;
import com.myee.tarot.campaign.service.ClientPrizeService;
import com.myee.tarot.campaign.service.impl.redis.DateTimeUtils;
import com.myee.tarot.clientprize.domain.ClientPrize;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/8/29.
 */
@Service
public class ClientPrizeServiceImpl extends GenericEntityServiceImpl<Long, ClientPrize> implements ClientPrizeService{

    protected ClientPrizeDao clientPrizeDao;

    @Autowired
    public ClientPrizeServiceImpl(ClientPrizeDao clientPrizeDao) {
        super(clientPrizeDao);
        this.clientPrizeDao = clientPrizeDao;
    }

    @Override
    public PageResult<ClientPrize> pageList(PageRequest pageRequest, Long storeId) {
        return clientPrizeDao.pageList(pageRequest, storeId);
    }

    @Override
    public List<ClientPrize> listActive(Long storeId) {
        return clientPrizeDao.listActive(storeId);
    }

    // 每天凌晨检测已过期的奖券
    @Override
    public void scanClientPrizeOverDate(){
        List<ClientPrize> clientPrizes = clientPrizeDao.listAllActive();
        for (ClientPrize clientPrize : clientPrizes) {
            Date endDate = clientPrize.getEndDate();
            Date startToday = DateTimeUtils.startToday();
            if (endDate.compareTo(startToday) < 0) {
                clientPrize.setActiveStatus(Constants.CLIENT_PRIZE_ACTIVE_NO);
                clientPrizeDao.update(clientPrize);
            }
        }
    }
}
