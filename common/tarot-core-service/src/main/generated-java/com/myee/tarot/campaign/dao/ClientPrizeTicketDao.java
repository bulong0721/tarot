package com.myee.tarot.campaign.dao;

import com.myee.tarot.clientprize.domain.ClientPrizeTicket;
import com.myee.tarot.core.dao.GenericEntityDao;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/9/9.
 */
public interface ClientPrizeTicketDao extends GenericEntityDao<Long,ClientPrizeTicket>{
    List<ClientPrizeTicket> listLimitTickets(int limit,Long prizeId);

    Long countTickets(Long prizeId,String name,Date startDate,Date endDate);

    List<ClientPrizeTicket> listUnGet();
}
