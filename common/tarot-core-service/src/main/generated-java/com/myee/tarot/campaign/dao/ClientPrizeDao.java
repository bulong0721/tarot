package com.myee.tarot.campaign.dao;

import com.myee.tarot.clientprize.domain.ClientPrize;
import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.WhereRequest;

import java.util.List;

/**
 * Created by Administrator on 2016/8/29.
 */
public interface ClientPrizeDao extends GenericEntityDao<Long, ClientPrize> {

    PageResult<ClientPrize> pageList(WhereRequest whereRequest, Long storeId);

    List<ClientPrize> listActive(Long storeId);

    List<ClientPrize> listActiveAndAboveZero(Long storeId);

    ClientPrize getThankYouPrize(Long storeId);

    List<ClientPrize> listAllActive();

}
