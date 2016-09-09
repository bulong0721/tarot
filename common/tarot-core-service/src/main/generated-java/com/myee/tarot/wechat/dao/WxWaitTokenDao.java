package com.myee.tarot.wechat.dao;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.wechat.domain.WxWaitToken;

import java.util.Date;
import java.util.List;

/**
 * Created by Ray on 2016/7/4.
 */
public interface WxWaitTokenDao extends GenericEntityDao<Long, WxWaitToken> {
    Integer updateState(Integer state, Long shopId, Long clientId, String token, Date timeTook, Date updateTime);

    Integer updateWaitTokenOpenId(String openId, String identityCode, Long date);

    Integer modifyWaitingInfo(long waitedCount, String identityCode, Long date, Long predictWaitingTime, Long tableTypeId);

    WxWaitToken getByIdentityCode(String identityCode, Long beginTime, Long endTime);

    List<WxWaitToken> listByConditions(Long orgId, Long tableTypeId, Integer state);

    WxWaitToken getByCondition(String openId, Integer state);

    List<WxWaitToken> selectWait(String openId, Long bTime, Long eTime);

    Integer modifyWaitingStatus(Integer state, Long waitQueueId);
}



