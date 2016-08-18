package com.myee.tarot.weixin.dao;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.weixin.domain.WxWaitToken;

import java.util.List;

/**
 * Created by Ray on 2016/7/4.
 */
public interface WxWaitTokenDao extends GenericEntityDao<Long, WxWaitToken> {
    public Integer updateState(Integer state, Long shopId, Long clientId, String token, Long timeTook, Long updateTime);

    public Integer updateWaitTokenOpenId(String openId, String identityCode, Long date);

    public Integer modifyWaitingInfo(Long waitedCount, String identityCode, Long date, Long predictWaitingTime);

    public WxWaitToken selectTokenByIdentityCode(String identityCode, Long beginTime, Long endTime);

    public List<WxWaitToken> selectAllTokenByInfo(Long clientId, Long orgId, Long tableTypeId, Integer state);

    public List<WxWaitToken> selectAllTokenOpenIdNotNull(Long clientId, Long orgId, Long tableTypeId, Integer state);

    public WxWaitToken selectAllTokenByOpenIdState(String openId, Integer state);

    public List<WxWaitToken> selectWait(String openId, Long bTime, Long eTime);

    public Integer modifyWaitingStatus(Integer state, Long waitQueueId);
}



