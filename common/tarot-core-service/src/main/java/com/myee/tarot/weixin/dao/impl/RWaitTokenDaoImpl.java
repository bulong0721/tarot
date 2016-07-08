package com.myee.tarot.weixin.dao.impl;

import com.myee.tarot.core.dao.GenericEntityDaoImpl;
import com.myee.tarot.weixin.dao.RWaitTokenDao;
import com.myee.tarot.weixin.domain.QRWaitToken;
import com.myee.tarot.weixin.domain.RWaitToken;
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
public class RWaitTokenDaoImpl extends GenericEntityDaoImpl<Long, RWaitToken> implements RWaitTokenDao {

    public static Log log = LogFactory.getLog(RWaitTokenDaoImpl.class);

    @Override
    public Integer updateState(Integer state, Long orgId, Long clientId, String token, Long timeTook, Long updateTime) {
        RWaitToken rWaitToken = new RWaitToken();
        rWaitToken.setOrgID(orgId);
        rWaitToken.setClientID(clientId);
        rWaitToken.setToken(token);
        rWaitToken.setTimeTook(new Date(timeTook));
        rWaitToken = getRWaitTokenByProp(rWaitToken);
        rWaitToken.setState(state);
        rWaitToken.setUpdated(new Date(updateTime));
        rWaitToken = this.update(rWaitToken);
        if(rWaitToken != null) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public Integer updateWaitTokenOpenId(String openId, String identityCode, Long date) {
        RWaitToken rWaitToken = new RWaitToken();
        rWaitToken.setIdentityCode(identityCode);
        rWaitToken.setTimeTook(new Date(date));
        rWaitToken = getRWaitTokenByProp(rWaitToken);
        rWaitToken.setOpenId(openId);
        rWaitToken = this.update(rWaitToken);
        if(rWaitToken != null) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public Integer modifyWaitingInfo(Long waitedCount, String identityCode, Long date, Long predictWaitingTime) {
        RWaitToken rWaitToken = new RWaitToken();
        rWaitToken.setIdentityCode(identityCode);
        rWaitToken.setTimeTook(new Date(date));
        rWaitToken = getRWaitTokenByProp(rWaitToken);
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
    public RWaitToken selectTokenByIc(String identityCode, Long beginTime, Long endTime) {
        QRWaitToken qrWaitToken = QRWaitToken.rWaitToken;
        JPQLQuery<RWaitToken> query = new JPAQuery(getEntityManager());
        query.from(qrWaitToken);
        if(identityCode != null){
            query.where(qrWaitToken.identityCode.eq(identityCode));
        }
        if(beginTime != null){
            query.where(qrWaitToken.timeTook.after(new Date(beginTime)));
        }
        if(endTime != null){
            query.where(qrWaitToken.timeTook.before(new Date(endTime)));
        }
        log.info("the result is: " + query.fetchFirst());

        return query.fetchFirst();
    }

    @Override
    public List<RWaitToken> selectAllTokenByInfo(Long clientId, Long orgId, Long tableTypeId, Integer state) {
        QRWaitToken qrWaitToken = QRWaitToken.rWaitToken;
        JPQLQuery<RWaitToken> query = new JPAQuery(getEntityManager());
        query.from(qrWaitToken);
        if (orgId != null) {
            query.where(qrWaitToken.orgID.eq(orgId));
        }
        if(tableTypeId != null) {
            query.where(qrWaitToken.tableTypeId.eq(tableTypeId));
        }
        if(clientId != null) {
            query.where(qrWaitToken.clientID.eq(clientId));
        }
        if(state != null) {
            query.where(qrWaitToken.state.eq(state));
        }
        log.info("the result counts: " + query.fetchCount());
        return query.fetch();
    }

    @Override
    public List<RWaitToken> selectAllTokenOpenIdNotNull(Long clientId, Long orgId, Long tableTypeId, Integer state) {
        QRWaitToken qrWaitToken = QRWaitToken.rWaitToken;
        JPQLQuery<RWaitToken> query = new JPAQuery(getEntityManager());
        query.from(qrWaitToken);
        if (orgId != null) {
            query.where(qrWaitToken.orgID.eq(orgId));
        }
        if(tableTypeId != null) {
            query.where(qrWaitToken.tableTypeId.eq(tableTypeId));
        }
        if(clientId != null) {
            query.where(qrWaitToken.clientID.eq(clientId));
        }
        if(state != null) {
            query.where(qrWaitToken.state.eq(state));
        }
        query.where(qrWaitToken.openId.isNotEmpty());
        log.info("the result counts: " + query.fetchCount());
        return query.fetch();
    }

    @Override
    public List<RWaitToken> selectAllTokenByOpenIdState(String openId, Integer state) {
        QRWaitToken qrWaitToken = QRWaitToken.rWaitToken;
        JPQLQuery<RWaitToken> query = new JPAQuery(getEntityManager());
        query.from(qrWaitToken);
        if (openId != null) {
            query.where(qrWaitToken.openId.eq(openId));
        }
        if(state != null) {
            query.where(qrWaitToken.state.eq(state));
        }
        log.info("the result counts: " + query.fetchCount());
        return query.fetch();
    }

    @Override
    public List<RWaitToken> selectWait(String openId, Long bTime, Long eTime) {
        QRWaitToken qrWaitToken = QRWaitToken.rWaitToken;
        JPQLQuery<RWaitToken> query = new JPAQuery(getEntityManager());
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
        RWaitToken rWaitToken = new RWaitToken();
        rWaitToken.setId(waitQueueId);
        rWaitToken.setState(state);
        this.update(rWaitToken);
        if(rWaitToken != null) {
            return 1;
        } else {
            return 0;
        }
    }

    private RWaitToken getRWaitTokenByProp(RWaitToken rWaitToken) {
        QRWaitToken qrWaitToken = QRWaitToken.rWaitToken;
        JPQLQuery<RWaitToken> query = new JPAQuery(getEntityManager());
        query.from(qrWaitToken);
        if(rWaitToken.getOrgID() != null){
            query.where(qrWaitToken.orgID.eq(rWaitToken.getOrgID()));
        }
        if(rWaitToken.getClientID() != null){
            query.where(qrWaitToken.clientID.eq(rWaitToken.getClientID()));
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
