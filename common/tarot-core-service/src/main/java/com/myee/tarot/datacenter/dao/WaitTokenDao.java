package com.myee.tarot.datacenter.dao;

import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.WhereRequest;
import com.myee.tarot.weixin.domain.WxWaitToken;
import com.myee.tarot.weixin.domain.WxWaitTokenState;

import java.util.List;

/**
 * Created by Jelynn on 2016/7/20.
 */
public interface WaitTokenDao  extends GenericEntityDao<Long, WxWaitToken> {

     PageResult<WxWaitToken> pageList(WhereRequest whereRequest);

}
