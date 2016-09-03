package com.myee.tarot.campaign.dao.impl;

import com.myee.tarot.campaign.dao.ClientPrizeDao;
import com.myee.tarot.clientprize.domain.ClientPrize;
import com.myee.tarot.clientprize.domain.QClientPrize;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.StringUtil;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2016/8/29.
 */
@Repository
public class ClientPrizeDaoImpl extends GenericEntityDaoImpl<Long, ClientPrize> implements ClientPrizeDao{
    @Override
    public PageResult<ClientPrize> pageList(PageRequest pageRequest, Long storeId) {
        PageResult<ClientPrize> pageList = new PageResult();
        QClientPrize qClientPrize = QClientPrize.clientPrize;
        JPQLQuery<ClientPrize> query = new JPAQuery(getEntityManager());
        query.from(qClientPrize);
        if(!StringUtil.isBlank(pageRequest.getQueryName())){
            query.where(qClientPrize.name.like("%" + pageRequest.getQueryName() + "%"));
        }
        if(storeId != null){
            query.where(qClientPrize.store.id.eq(storeId));
        }
        pageList.setRecordsTotal(query.fetchCount());
        if( pageRequest.getCount() > 0){
            query.offset(pageRequest.getOffset()).limit(pageRequest.getCount());
        }
        pageList.setList(query.fetch());
        return pageList;
    }

    @Override
    public List<ClientPrize> listActive(Long storeId) {
        QClientPrize qClientPrize = QClientPrize.clientPrize;
        JPQLQuery<ClientPrize> query = new JPAQuery(getEntityManager());
        query.from(qClientPrize).where(qClientPrize.store.id.eq(storeId).and(qClientPrize.activeStatus.eq(Constants.CLIENT_PRIZE_ACTIVE_YES)));
        return query.fetch();
    }

    @Override
    public ClientPrize getThankYouPrize(Long storeId) {
        QClientPrize qClientPrize = QClientPrize.clientPrize;
        JPQLQuery<ClientPrize> query = new JPAQuery(getEntityManager());
        query.from(qClientPrize).where(qClientPrize.store.id.eq(storeId).and(qClientPrize.activeStatus.eq(Constants.CLIENT_PRIZE_ACTIVE_YES).and(qClientPrize.type.eq(Constants.CLIENT_PRIZE_TYPE_THANKYOU))));
        return query.fetchFirst();
    }

    @Override
    public List<ClientPrize> listAllActive() {
        QClientPrize qClientPrize = QClientPrize.clientPrize;
        JPQLQuery<ClientPrize> query = new JPAQuery(getEntityManager());
        query.from(qClientPrize).where(qClientPrize.activeStatus.eq(Constants.CLIENT_PRIZE_ACTIVE_YES));
        return query.fetch();
    }
}
