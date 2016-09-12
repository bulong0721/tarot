package com.myee.tarot.campaign.service;

import com.myee.tarot.clientprize.domain.ClientPrizeTicket;
import com.myee.tarot.core.service.GenericEntityService;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/9/9.
 */
public interface ClientPrizeTicketService extends GenericEntityService<Long ,ClientPrizeTicket>{
    List<ClientPrizeTicket> listLimitTickets(int limit, Long prizeId);
    Boolean isExistPrizeTicket(Long prizeId,String name,Date startDate,Date endDate);
}
