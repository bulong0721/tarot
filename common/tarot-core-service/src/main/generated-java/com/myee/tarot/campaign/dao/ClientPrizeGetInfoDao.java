package com.myee.tarot.campaign.dao;

import com.myee.tarot.clientprize.domain.ClientPrizeGetInfo;
import com.myee.tarot.core.dao.GenericEntityDao;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/8/29.
 */
public interface ClientPrizeGetInfoDao extends GenericEntityDao<Long, ClientPrizeGetInfo> {

    long countByPhoneToday(Long phone,Date startTime,Date endTime);

    ClientPrizeGetInfo findByIdAndDeskId(Long id,String deskId);

    List<ClientPrizeGetInfo> listUnGet();

    PageResult<ClientPrizeGetInfo> pageListOfChecked(PageRequest pageRequest, Long storeId);

    ClientPrizeGetInfo checkClientPriceInfo(Long storeId, String checkCode);

    Integer countUnGetByPrizeId(Long prizeId);
}
