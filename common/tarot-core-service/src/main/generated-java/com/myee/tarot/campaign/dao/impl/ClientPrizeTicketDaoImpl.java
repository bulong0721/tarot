package com.myee.tarot.campaign.dao.impl;

import com.myee.tarot.campaign.dao.ClientPrizeTicketDao;
import com.myee.tarot.clientprize.domain.ClientPrizeTicket;
import com.myee.tarot.clientprize.domain.QClientPrizeTicket;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/9/9.
 */
@Repository
public class ClientPrizeTicketDaoImpl extends GenericEntityDaoImpl<Long, ClientPrizeTicket> implements ClientPrizeTicketDao{
    @Override
    public List<ClientPrizeTicket> listLimitTickets(int limit, Long prizeId) {
        QClientPrizeTicket qClientPrizeTicket = QClientPrizeTicket.clientPrizeTicket;
        JPQLQuery<ClientPrizeTicket> query = new JPAQuery(getEntityManager());
        List<ClientPrizeTicket> clientPrizeTickets = query.from(qClientPrizeTicket).where(qClientPrizeTicket.status.eq(Constants.CLIENT_PRIZE_TICKET_YES).and(qClientPrizeTicket.prize.id.eq(prizeId))).orderBy(qClientPrizeTicket.id.asc()).limit(limit).fetch();
        return clientPrizeTickets;
    }

    @Override
    public Long countTickets(Long prizeId, String name, Date startDate, Date endDate) {
        QClientPrizeTicket qClientPrizeTicket = QClientPrizeTicket.clientPrizeTicket;
        JPQLQuery<ClientPrizeTicket> query = new JPAQuery(getEntityManager());
        Long count = query.from(qClientPrizeTicket).where(qClientPrizeTicket.prize.id.eq(prizeId).and(qClientPrizeTicket.ticketCode.eq(name).and(qClientPrizeTicket.startDate.eq(startDate).and(qClientPrizeTicket.endDate.eq(endDate))))).fetchCount();
        return count;
    }

    @Override
    public List<ClientPrizeTicket> listUnGet() {
        QClientPrizeTicket qClientPrizeTicket = QClientPrizeTicket.clientPrizeTicket;
        JPQLQuery<ClientPrizeTicket> query = new JPAQuery(getEntityManager());
        List<ClientPrizeTicket> tickets = query.from(qClientPrizeTicket).where(qClientPrizeTicket.status.eq(Constants.CLIENT_PRIZE_TICKET_YES)).fetch();
        return tickets;
    }
}
