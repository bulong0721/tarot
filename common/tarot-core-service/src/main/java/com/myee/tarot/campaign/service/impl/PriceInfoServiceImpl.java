package com.myee.tarot.campaign.service.impl;

import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.campaign.dao.PriceInfoDao;
import com.myee.tarot.campaign.domain.PriceInfo;
import com.myee.tarot.campaign.service.PriceInfoService;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<PriceInfo> findByStatusAndKeyId(Long keyId, int status) {
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


}
