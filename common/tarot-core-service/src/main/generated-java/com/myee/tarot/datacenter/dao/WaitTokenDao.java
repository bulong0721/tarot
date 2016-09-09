package com.myee.tarot.datacenter.dao;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.WhereRequest;
import com.myee.tarot.wechat.domain.WxWaitToken;

/**
 * Created by Jelynn on 2016/7/20.
 */
public interface WaitTokenDao  extends GenericEntityDao<Long, WxWaitToken> {

     PageResult<WxWaitToken> page(WhereRequest whereRequest);

}
