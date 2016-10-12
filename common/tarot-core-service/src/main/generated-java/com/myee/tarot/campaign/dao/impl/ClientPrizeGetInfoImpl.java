package com.myee.tarot.campaign.dao.impl;

import com.myee.tarot.campaign.dao.ClientPrizeGetInfoDao;
import com.myee.tarot.clientprize.domain.ClientPrizeGetInfo;
import com.myee.tarot.clientprize.domain.QClientPrizeGetInfo;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.StringUtil;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

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

    @Override
    public List<ClientPrizeGetInfo> listUnGet() {
        QClientPrizeGetInfo qClientPrizeGetInfo = QClientPrizeGetInfo.clientPrizeGetInfo;
        JPQLQuery<ClientPrizeGetInfo> query = new JPAQuery(getEntityManager());
        List<ClientPrizeGetInfo> clientPrizeGetInfos = query.from(qClientPrizeGetInfo).where(qClientPrizeGetInfo.status.eq(Constants.CLIENT_PRIZEINFO_STATUS_UNGET)).fetch();
        return clientPrizeGetInfos;
    }

    @Override
    public PageResult<ClientPrizeGetInfo> pageListOfChecked(PageRequest pageRequest, Long storeId) {
        PageResult<ClientPrizeGetInfo> pageList = new PageResult();
        QClientPrizeGetInfo qClientPrizeGetInfo = QClientPrizeGetInfo.clientPrizeGetInfo;
        JPQLQuery<ClientPrizeGetInfo> query = new JPAQuery(getEntityManager());
        query.from(qClientPrizeGetInfo).leftJoin(qClientPrizeGetInfo.price).fetchJoin();
        if(!StringUtil.isBlank(pageRequest.getQueryName())){
            query.where(qClientPrizeGetInfo.phoneNum.like("%" + pageRequest.getQueryName() + "%"));
        }
        if(storeId != null){
            query.where(qClientPrizeGetInfo.price.store.id.eq(storeId));
        }
        query.where(qClientPrizeGetInfo.status.eq(Constants.CLIENT_PRIZEINFO_STATUS_USED)).orderBy(qClientPrizeGetInfo.checkDate.desc());
        pageList.setRecordsTotal(query.fetchCount());
        if( pageRequest.getCount() > 0){
            query.offset(pageRequest.getOffset()).limit(pageRequest.getCount());
        }
        pageList.setList(query.fetch());
        return  pageList;
    }

    @Override
    public ClientPrizeGetInfo checkClientPriceInfo(Long storeId, String checkCode) {
        QClientPrizeGetInfo qClientPrizeGetInfo = QClientPrizeGetInfo.clientPrizeGetInfo;
        JPQLQuery<ClientPrizeGetInfo> query = new JPAQuery(getEntityManager());
        query.from(qClientPrizeGetInfo).leftJoin(qClientPrizeGetInfo.price).fetchJoin();
        ClientPrizeGetInfo clientPrizeGetInfo = query.where(qClientPrizeGetInfo.price.store.id.eq(storeId).and(qClientPrizeGetInfo.checkCode.eq(checkCode))).fetchOne();
        return clientPrizeGetInfo;
    }

    @Override
    public Integer countUnGetByPrizeId(Long prizeId) {
        QClientPrizeGetInfo qClientPrizeGetInfo = QClientPrizeGetInfo.clientPrizeGetInfo;
        JPQLQuery<ClientPrizeGetInfo> query = new JPAQuery(getEntityManager());
        long count = query.from(qClientPrizeGetInfo).where(qClientPrizeGetInfo.price.id.eq(prizeId).and(qClientPrizeGetInfo.status.eq(Constants.CLIENT_PRIZEINFO_STATUS_UNGET))).fetchCount();
        return (int)count;
    }
}
