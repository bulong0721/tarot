package com.myee.tarot.weixin.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.merchant.domain.Merchant;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.weixin.dao.WxWaitTokenDao;
import com.myee.tarot.weixin.domain.QWxWaitToken;
import com.myee.tarot.weixin.domain.WxWaitToken;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

/**
 * Created by Ray.Fu on 2016/7/4.
 */
@Repository
public class WxWaitTokenDaoImpl extends GenericEntityDaoImpl<Long, WxWaitToken> implements WxWaitTokenDao {

    public static Log log = LogFactory.getLog(WxWaitTokenDaoImpl.class);

    @Override
    public Integer updateState(Integer state, Long orgId, Long clientId, String token, Date timeTook, Date updateTime) {
        WxWaitToken rWaitToken = new WxWaitToken();
        MerchantStore merchantStore = new MerchantStore();
        merchantStore.setId(orgId);
        rWaitToken.setStore(merchantStore);
        rWaitToken.setToken(token);
        rWaitToken.setTimeTook(timeTook);
        rWaitToken = getWaitTokenByProp(rWaitToken);
        rWaitToken.setState(state);
        rWaitToken.setUpdated(updateTime);
        rWaitToken = this.update(rWaitToken);
        if(rWaitToken != null) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public Integer updateWaitTokenOpenId(String openId, String identityCode, Long date) {
        WxWaitToken rWaitToken = new WxWaitToken();
        rWaitToken.setIdentityCode(identityCode);
        rWaitToken.setTimeTook(new Date(date));
        rWaitToken = getWaitTokenByProp(rWaitToken);
        rWaitToken.setOpenId(openId);
        rWaitToken.setUpdated(new Date());
        rWaitToken = this.update(rWaitToken);
        if(rWaitToken != null) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public Integer modifyWaitingInfo(Long waitedCount, String identityCode, Long date, Long predictWaitingTime) {
        WxWaitToken rWaitToken = new WxWaitToken();
        rWaitToken.setIdentityCode(identityCode);
        rWaitToken.setTimeTook(new Date(date));
        rWaitToken = getWaitTokenByProp(rWaitToken);
        rWaitToken.setWaitedCount(waitedCount);
        rWaitToken.setPredictWaitingTime(predictWaitingTime);
        rWaitToken = this.update(rWaitToken);
        if(rWaitToken != null) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public WxWaitToken getByIdentityCode(String identityCode, Long beginTime, Long endTime) {
        QWxWaitToken qrWaitToken = QWxWaitToken.wxWaitToken;
        JPQLQuery<WxWaitToken> query = new JPAQuery(getEntityManager());
        query.from(qrWaitToken);
        if(identityCode != null){
            query.where(qrWaitToken.identityCode.eq(identityCode));
        }
        if(beginTime != null && endTime != null){
            query.where(qrWaitToken.timeTook.between(new Date(beginTime), new Date(endTime)));
        }
        log.info("the result is: " + query.fetchFirst());

        return query.fetchFirst();
    }

    @Override
    public List<WxWaitToken> listByConditions(Long orgId, Long tableTypeId, Integer state) {
        QWxWaitToken qrWaitToken = QWxWaitToken.wxWaitToken;
        JPQLQuery<WxWaitToken> query = new JPAQuery(getEntityManager());
        query.from(qrWaitToken);
        if (orgId != null) {
            query.where(qrWaitToken.store.id.eq(orgId));
        }
        if(tableTypeId != null) {
            query.where(qrWaitToken.tableTypeId.eq(tableTypeId));
        }
        if(state != null) {
            query.where(qrWaitToken.state.eq(state));
        }
        query.orderBy(qrWaitToken.timeTook.asc());
        log.info("the result counts: " + query.fetchCount());
        return query.fetch();
    }

    @Override
    public WxWaitToken getByCondition(String openId, Integer state) {
        QWxWaitToken qrWaitToken = QWxWaitToken.wxWaitToken;
        JPQLQuery<WxWaitToken> query = new JPAQuery(getEntityManager());
        query.from(qrWaitToken);
        if (openId != null) {
            query.where(qrWaitToken.openId.eq(openId));
        }
        if(state != null) {
            query.where(qrWaitToken.state.eq(state));
        }
        query.orderBy(qrWaitToken.updated.desc());
        log.info("the result counts: " + query.fetchCount());
        return query.fetch().get(0);
    }

    @Override
    public List<WxWaitToken> selectWait(String openId, Long bTime, Long eTime) {
        QWxWaitToken qrWaitToken = QWxWaitToken.wxWaitToken;
        JPQLQuery<WxWaitToken> query = new JPAQuery(getEntityManager());
        query.from(qrWaitToken);
        if (openId != null) {
            query.where(qrWaitToken.openId.eq(openId));
        }
        if(bTime != null) {
            query.where(qrWaitToken.timeTook.after(new Date(bTime)));
        }
        if(eTime != null) {
            query.where(qrWaitToken.timeTook.before(new Date(eTime)));
        }
        log.info("the result counts: " + query.fetchCount());
        return query.fetch();
    }

    @Override
    public Integer modifyWaitingStatus(Integer state, Long waitQueueId) {
        WxWaitToken rWaitToken = new WxWaitToken();
        rWaitToken.setId(waitQueueId);
        rWaitToken.setState(state);
        this.update(rWaitToken);
        if(rWaitToken != null) {
            return 1;
        } else {
            return 0;
        }
    }

    private WxWaitToken getWaitTokenByProp(WxWaitToken rWaitToken) {
        QWxWaitToken qrWaitToken = QWxWaitToken.wxWaitToken;
        JPQLQuery<WxWaitToken> query = new JPAQuery(getEntityManager());
        query.from(qrWaitToken);
        if(rWaitToken.getStore().getId() != null){
            query.where(qrWaitToken.store.id.eq(rWaitToken.getStore().getId()));
    }
        if(rWaitToken.getToken() != null){
            query.where(qrWaitToken.token.eq(rWaitToken.getToken()));
        }
        if(rWaitToken.getIdentityCode() != null){
            query.where(qrWaitToken.identityCode.eq(rWaitToken.getIdentityCode()));
        }
        log.info("the result is: " + query.fetchFirst());

        return query.fetchFirst();
    }
}
