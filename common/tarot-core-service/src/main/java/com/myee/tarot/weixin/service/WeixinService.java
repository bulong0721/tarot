package com.myee.tarot.weixin.service;

import com.myee.djinn.dto.DrawToken;
import com.myee.djinn.dto.ShopDetail;
import com.myee.djinn.dto.WaitToken;
import com.myee.tarot.weixin.domain.WxWaitToken;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Martin on 2016/1/19.
 */
public interface WeixinService {

    Collection<ShopDetail> allStoreOfClient(Long clientId);

    ShopDetail storeOfClient(long clientId, long storeId);

    WaitToken waitOfTableType(long tableTypeId, String token, Long shopId);

    Collection<WaitToken> allWaitOfTableType(long tableTypeId, Long shopId);

    WxWaitToken enqueue(long shopId, String mbNum, int dinerCount, String openId);

    void dequeue(long tableTypeId, String token, Long clientId, Long shopId);

    DrawToken drawOfStore(long storeId, String token);

    /**
     * 查看最新进展，根据clientId,和orgId，openId查询最新进展
     * @param openId
     * @param orgID
     * @return
     */
    Map<String,Object> selectAllTokens(String openId, Long orgID, Long tableTypeId);

    /**
     * 根据identityCode查询最新进展
     * @param identityCode
     * @return
     */
    Map<String,Object> selectProgressByIdentityCode(String identityCode);

    /**
     * 按openId和状态去数据库里查找Token
     * @param openId
     * @param state
     * @return
     */
    WxWaitToken selectTokensByOpenIdState(String openId, Integer state);

    boolean bondQrCodeByScan(String identityCode, String openId);

    List<WxWaitToken> selectWaitList(String openId, Map<String, Date> map);

    int modifyWaitingInfo(Long waitedCount, String identityCode, Long date, Long predictWaitingTime);

    /**
     * 获取排队Id对应的RedisKey
     * @param waitTokenId
     * @return
     */
    String hGetWaitTokenRedisKey(Long waitTokenId);

    /**
     * 删除Redis排队记录
     * @param waitTokenId
     */
    void hDelWaitTokenRedisRecord(Long waitTokenId);

    /**
     * 修改MySQL排队状态
     * @param statusValue
     * @param waitQueueId
     * @return
     */
    int modifyWaitingStatus(int statusValue, long waitQueueId);

    /**
     * 统计两点距离
     * @param storeLongitude
     * @param storeLatitude
     * @param longitude
     * @param latitude
     * @return
     */
    Double calcDistance(Double storeLongitude, Double storeLatitude, Double longitude, Double latitude);

    void sendFeedback(String context, String openId);
}
