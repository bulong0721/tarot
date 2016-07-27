package com.myee.tarot.campaign.service.impl;

import com.google.common.collect.Lists;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.campaign.dao.PriceInfoDao;
import com.myee.tarot.campaign.domain.PriceInfo;
import com.myee.tarot.campaign.service.PriceInfoService;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/7/14.
 */
@Service
public class PriceInfoServiceImpl extends GenericEntityServiceImpl<Long, PriceInfo> implements PriceInfoService{

    @Autowired
    private PriceInfoDao priceInfoDao;

    @Autowired
    private PriceInfoServiceImpl(PriceInfoDao priceInfoDao){
        super(priceInfoDao);
        this.priceInfoDao = priceInfoDao;
    }

    @Override
    public List<PriceInfo> findByStatusAndKeyId(String keyId, int status) {
        return priceInfoDao.findByStatusAndKeyId(keyId, status);
    }

    @Override
    public PageResult<PriceInfo> pageList(Long storeId,PageRequest pageRequest) {
        return priceInfoDao.pageList(storeId,pageRequest);
    }

    @Override
    public PriceInfo priceCheckCode(Long storeId, String checkCode) {
        return priceInfoDao.priceCheckCode(storeId,checkCode);
    }

    @Override
    public boolean findByStoreIdAndKeyIdToday(Long storeId, String keyId) {
        List<PriceInfo> priceInfo = priceInfoDao.findByStoreIdAndKeyId(storeId,keyId);
        List<PriceInfo> onlyInfo = Lists.newArrayList();
        for (PriceInfo info : priceInfo) {
            Date getDate = info.getGetDate();
            boolean flag = TimeUtil.whetherToday(getDate);
            if(flag){
                onlyInfo.add(info);
            }
        }
        return onlyInfo!=null&&onlyInfo.size()==1 ? false : true;
    }


}
