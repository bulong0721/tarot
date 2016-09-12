package com.myee.tarot.campaign.service.impl;

import com.myee.tarot.campaign.dao.ClientPrizeTicketDao;
import com.myee.tarot.campaign.service.ClientPrizeTicketService;
import com.myee.tarot.clientprize.domain.ClientPrizeTicket;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.core.util.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/9/9.
 */
@Service
public class ClientPrizeTicketServiceImpl extends GenericEntityServiceImpl<Long, ClientPrizeTicket> implements ClientPrizeTicketService{

    protected ClientPrizeTicketDao clientPrizeTicketDao;

    @Autowired
    public ClientPrizeTicketServiceImpl(ClientPrizeTicketDao clientPrizeTicketDao) {
        super(clientPrizeTicketDao);
        this.clientPrizeTicketDao = clientPrizeTicketDao;
    }

    @Override
    public List<ClientPrizeTicket> listLimitTickets(int limit,Long prizeId) {
        return clientPrizeTicketDao.listLimitTickets(limit, prizeId);
    }

    @Override
    public Boolean isExistPrizeTicket(Long prizeId, String name, Date startDate, Date endDate) {
        long countTickets = clientPrizeTicketDao.countTickets(prizeId,name,startDate,endDate);
        return countTickets != 0 ? true: false;
    }

    @Override
    public void checkExpireTickets() {
        //获取所有未领取的电影券
        List<ClientPrizeTicket> tickets = clientPrizeTicketDao.listUnGet();
        for (ClientPrizeTicket ticket : tickets) {
            Date endDate = ticket.getEndDate();
            Date today = DateTimeUtils.startToday();
            //若时间过期，更改状态
            if (endDate.compareTo(today) < 0){
                ticket.setStatus(Constants.CLIENT_PRIZE_TICKET_EXPIRE);
                clientPrizeTicketDao.update(ticket);
            }
        }
    }
}
