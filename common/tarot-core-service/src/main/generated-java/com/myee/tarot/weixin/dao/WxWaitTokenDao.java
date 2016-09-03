package com.myee.tarot.weixin.dao;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.weixin.domain.WxWaitToken;

import java.util.Date;
import java.util.List;

/**
 * Created by Ray on 2016/7/4.
 */
public interface WxWaitTokenDao extends GenericEntityDao<Long, WxWaitToken> {
    public Integer updateState(Integer state, Long shopId, Long clientId, String token, Date timeTook, Date updateTime);

    public Integer updateWaitTokenOpenId(String openId, String identityCode, Long date);

    public Integer modifyWaitingInfo(long waitedCount, String identityCode, Long date, Long predictWaitingTime, Long tableTypeId);

    public WxWaitToken getByIdentityCode(String identityCode, Long beginTime, Long endTime);

    public List<WxWaitToken> listByConditions(Long orgId, Long tableTypeId, Integer state);

    public WxWaitToken getByCondition(String openId, Integer state);

    public List<WxWaitToken> selectWait(String openId, Long bTime, Long eTime);

    public Integer modifyWaitingStatus(Integer state, Long waitQueueId);
}



