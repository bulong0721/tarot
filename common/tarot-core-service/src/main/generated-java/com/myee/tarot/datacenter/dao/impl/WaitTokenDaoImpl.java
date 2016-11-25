package com.myee.tarot.datacenter.dao.impl;

import com.myee.tarot.core.util.DateTimeUtils;
import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.StringUtil;
import com.myee.tarot.core.util.WhereRequest;
import com.myee.tarot.datacenter.dao.WaitTokenDao;
import com.myee.tarot.wechat.domain.QWxWaitToken;
import com.myee.tarot.wechat.domain.WxWaitToken;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created by Jelynn on 2016/7/20.
 */
@Repository
public class WaitTokenDaoImpl  extends GenericEntityDaoImpl<Long, WxWaitToken> implements WaitTokenDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(WaitTokenDaoImpl.class);

    @Override
    public PageResult<WxWaitToken> page(WhereRequest whereRequest) {
        PageResult<WxWaitToken> pageList = new PageResult<WxWaitToken>();
        QWxWaitToken qWxWaitToken = QWxWaitToken.wxWaitToken;
        JPQLQuery<WxWaitToken> query = new JPAQuery(getEntityManager());

        if(!StringUtil.isBlank(whereRequest.getWaitState())){
            query.where(qWxWaitToken.state.eq(Integer.parseInt(whereRequest.getWaitState())));
        }
        if(null != whereRequest.getBeginDate()){
            query.where(qWxWaitToken.created.before(DateTimeUtils.getDateByString(whereRequest.getBeginDate())));
        }
        if(null != whereRequest.getEndDate()){
            query.where(qWxWaitToken.created.after(DateTimeUtils.getDateByString(whereRequest.getBeginDate())));
        }
        pageList.setRecordsTotal(query.from(qWxWaitToken).fetchCount());
        query.orderBy(qWxWaitToken.timeTook.asc(),qWxWaitToken.updated.asc());
        if( whereRequest.getCount() > 0){
            query.offset(whereRequest.getOffset()).limit(whereRequest.getCount());
        }
        pageList.setList(query.fetch());
        return pageList;
    }

    @Override
    public WxWaitToken findByShopIdAndTokenToday(Long shopId, String token, Date startToday, Date endToday) {
        QWxWaitToken qWxWaitToken = QWxWaitToken.wxWaitToken;
        JPQLQuery<WxWaitToken> query = new JPAQuery(getEntityManager());
        WxWaitToken wxWaitToken = query.from(qWxWaitToken).where(qWxWaitToken.store.id.eq(shopId).and(qWxWaitToken.token.eq(token).and(qWxWaitToken.created.between(startToday,endToday)))).fetchOne();
        return wxWaitToken;
    }
}
