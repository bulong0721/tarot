package com.myee.tarot.campaign.service.impl;

import com.myee.tarot.campaign.dao.ClientPrizeGetInfoDao;
import com.myee.tarot.campaign.service.ClientPrizeGetInfoService;
import com.myee.tarot.campaign.service.impl.redis.DateTimeUtils;
import com.myee.tarot.clientprize.domain.ClientPrize;
import com.myee.tarot.clientprize.domain.ClientPrizeGetInfo;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/8/29.
 */
@Service
public class ClientPrizeGetInfoServiceImpl extends GenericEntityServiceImpl<Long, ClientPrizeGetInfo> implements ClientPrizeGetInfoService{

    protected ClientPrizeGetInfoDao clientPrizeGetInfoDao;

    @Autowired
    public ClientPrizeGetInfoServiceImpl(ClientPrizeGetInfoDao clientPrizeGetInfoDao) {
        super(clientPrizeGetInfoDao);
        this.clientPrizeGetInfoDao = clientPrizeGetInfoDao;
    }

    @Override
    public boolean isOverThreeTimes(Long phoneNum) {
        Date startTime = DateTimeUtils.startToday();
        Date endTime = DateTimeUtils.endToday();
        long count = clientPrizeGetInfoDao.countByPhoneToday(phoneNum,startTime,endTime);
        if(count > 3) {
            return true;
        }else {
            return false;
        }
    }

    @Override
    public ClientPrizeGetInfo findByIdAndDeskId(Long prizeGetId, String deskId) {
        return clientPrizeGetInfoDao.findByIdAndDeskId(prizeGetId, deskId);
    }

    //检测抽取信息 未领取的超过5分钟的自动撤回
    @Override
    public void overFiveMinGetInfo(){
        List<ClientPrizeGetInfo>  clientPrizeGetInfos = clientPrizeGetInfoDao.listUnGet();
        for (ClientPrizeGetInfo clientPrizeGetInfo : clientPrizeGetInfos) {
            Date getDate = clientPrizeGetInfo.getGetDate();
            Date now = new Date();
            int timeGap = DateTimeUtils.getTimeGap(now,getDate);
            //大于5分钟
            if(timeGap > 5){
                ClientPrize prize = clientPrizeGetInfo.getPrice();
                prize.setLeftNum(prize.getLeftNum() + 1);
                clientPrizeGetInfoDao.delete(clientPrizeGetInfo);

            }
        }
    }

    @Override
    public PageResult<ClientPrizeGetInfo> pageListOfChecked(PageRequest pageRequest, Long storeId) {
        return clientPrizeGetInfoDao.pageListOfChecked(pageRequest,storeId);
    }

    @Override
    public ClientPrizeGetInfo checkClientPriceInfo(Long storeId, String checkCode) {
        return clientPrizeGetInfoDao.checkClientPriceInfo(storeId, checkCode);
    }

}
