package com.myee.tarot.campaign.dao.impl;

import com.myee.tarot.campaign.dao.ClientPrizeGetInfoDao;
import com.myee.tarot.clientprize.domain.ClientPrizeGetInfo;
import com.myee.tarot.clientprize.domain.QClientPrizeGetInfo;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created by Administrator on 2016/8/29.
 */
@Repository
public class ClientPrizeGetInfoImpl extends GenericEntityDaoImpl<Long, ClientPrizeGetInfo> implements ClientPrizeGetInfoDao{
    @Override
    public long countByPhoneToday(Long phone, Date startTime, Date endTime) {
        QClientPrizeGetInfo qClientPrizeGetInfo = QClientPrizeGetInfo.clientPrizeGetInfo;
        JPQLQuery<ClientPrizeGetInfo> query = new JPAQuery(getEntityManager());
        long count = query.from(qClientPrizeGetInfo).where(qClientPrizeGetInfo.phoneNum.eq(phone).and(qClientPrizeGetInfo.getDate.between(startTime,endTime))).fetchCount();
        return count;
    }

    @Override
    public ClientPrizeGetInfo findByIdAndDeskId(Long id, String deskId) {
        QClientPrizeGetInfo qClientPrizeGetInfo = QClientPrizeGetInfo.clientPrizeGetInfo;
        JPQLQuery<ClientPrizeGetInfo> query = new JPAQuery(getEntityManager());
        ClientPrizeGetInfo clientPrizeGetInfo = query.from(qClientPrizeGetInfo).where(qClientPrizeGetInfo.id.eq(id).and(qClientPrizeGetInfo.deskId.eq(deskId))).fetchOne();
        return clientPrizeGetInfo;
    }
}
