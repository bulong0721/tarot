package com.myee.tarot.datacenter.service;

import com.myee.tarot.core.service.GenericEntityService;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.WhereRequest;
import com.myee.tarot.wechat.domain.WxWaitToken;

/**
 * Created by Jelynn on 2016/7/20.
 */
public interface WaitTokenService extends GenericEntityService<Long, WxWaitToken> {

    PageResult<WxWaitToken> page(WhereRequest whereRequest);

    WxWaitToken findByShopIdAndTokenToday(Long shopId, String token);
}
