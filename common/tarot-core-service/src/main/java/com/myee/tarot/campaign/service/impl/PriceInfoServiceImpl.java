package com.myee.tarot.campaign.service.impl;

import com.google.common.collect.Lists;
import com.myee.tarot.campaign.domain.MerchantActivity;
import com.myee.tarot.campaign.domain.MerchantPrice;
import com.myee.tarot.campaign.service.MerchantActivityService;
import com.myee.tarot.campaign.service.impl.redis.RedisUtil;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.exception.ServiceException;
import com.myee.tarot.core.service.GenericEntityServiceImpl;
import com.myee.tarot.campaign.dao.PriceInfoDao;
import com.myee.tarot.campaign.domain.PriceInfo;
import com.myee.tarot.campaign.service.PriceInfoService;
import com.myee.tarot.core.util.AutoNumUtil;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.TimeUtil;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/7/14.
 */
@Service
public class PriceInfoServiceImpl extends GenericEntityServiceImpl<Long, PriceInfo> implements PriceInfoService{

    @Autowired
    private PriceInfoDao priceInfoDao;

    @Autowired
    private MerchantActivityService merchantActivityService;

    @Autowired
    private RedisUtil redisUtil;

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
        return priceInfoDao.pageList(storeId, pageRequest);
    }

    @Override
    public PriceInfo priceCheckCode(Long storeId, String checkCode) {
        return priceInfoDao.priceCheckCode(storeId, checkCode);
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

    @Override
    public AjaxResponse savePriceInfo(String keyId, Long storeId) throws ServiceException {
        AjaxResponse resp = new AjaxResponse();
        //通过keyId和storeId查看此人是否今天已经抽取过奖券
        boolean canDraw = findByStoreIdAndKeyIdToday(storeId,keyId);
        if(!canDraw){
            resp.setErrorString("你今天已抽取过，请明天再来抽取");
            resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
            return resp;
        }
        PriceInfo priceInfo = new PriceInfo();
        priceInfo.setKeyId(keyId);
        priceInfo.setCheckCode(AutoNumUtil.getCode( 6, 3)); //设置6位随机数
        priceInfo.setStatus(Constants.PRICEINFO_UNUSED);
        priceInfo.setGetDate(new Date());
        MerchantActivity activity = merchantActivityService.findStoreActivity(storeId);
        // 从redis中获取抽奖规则list,判定list是否为空，为空后直接修改活动状态，直接为结束
        List<Integer> priceList = redisUtil.getList(Constants.PRICEDRAW + "_"+ storeId,Integer.class);
        if(priceList==null||priceList.size()==0){
            MerchantActivity merchantActivity = merchantActivityService.findStoreActivity(storeId);
            if(merchantActivity.getActivityStatus() != Constants.ACITIVITY_END){
                merchantActivity.setActivityStatus(Constants.ACITIVITY_END);
                merchantActivityService.update(merchantActivity);
            }
            resp.setErrorString("活动已结束");
            resp.setStatus(AjaxResponse.RESPONSE_STATUS_FAIURE);
            return resp;
        }
        PriceInfo info = getRandomPrice(priceInfo,priceList,activity.getPrices());
        // 更新redis的list
        if(priceList.size()==0){
            redisUtil.delete(Constants.PRICEDRAW + "_" + storeId);
        }else{
            redisUtil.set(Constants.PRICEDRAW + "_" + storeId, priceList, 365, TimeUnit.DAYS);
        }
        super.save(priceInfo);
        PriceInfo result = super.findById(priceInfo.getId());
        resp.setStatus(AjaxResponse.RESPONSE_STATUS_SUCCESS);
        resp.addEntry("result", result);
        return resp;
    }

    @Override
    public void updateRedisDrawList() throws ServiceException {
        List<MerchantActivity> activeActivities = merchantActivityService.findActiveActivity();
        for (MerchantActivity activeActivity : activeActivities) {
            List<MerchantPrice> activePrices = Lists.newArrayList();
            List<MerchantPrice> prices = activeActivity.getPrices();
            for (MerchantPrice price : prices) {
                if(price.getActiveStatus() == Constants.PRICE_START){
                    activePrices.add(price);
                }
            }
            List<Integer> priceList = getPriceCountList(activePrices);
            activeActivity.setActivityStatus(Constants.ACTIVITY_ACTIVE);
            merchantActivityService.update(activeActivity);
            if(priceList.size()==0){
                redisUtil.delete(Constants.PRICEDRAW + "_" + activeActivity.getStore().getId());
            }else {
                redisUtil.set(Constants.PRICEDRAW + "_" + activeActivity.getStore().getId(),priceList,365,TimeUnit.DAYS);
            }
        }
    }

    @Override
    public PriceInfo findByIdAndKeyId(Long id, String keyId) {
        return priceInfoDao.findByIdAndKeyId(id,keyId);
    }

    public PriceInfo getRandomPrice(PriceInfo basePriceInfo,List<Integer> priceList,List<MerchantPrice> prices){
        Random random = new Random();
        int index = random.nextInt(priceList.size());
        int priceInt = priceList.get(index);
        List<MerchantPrice> activePrices = Lists.newArrayList();
        for (MerchantPrice price : prices) {
            if(price.getActiveStatus()==Constants.PRICE_START){
                activePrices.add(price);
            }
        }
        int compare = 0;
        for (MerchantPrice activePrice : activePrices) {
            compare+=activePrice.getTotal();
            if(priceInt<= compare){
                MerchantPrice getPrice = activePrice;
                basePriceInfo.setPrice(getPrice);
                break;
            }
        }
        priceList.remove(index);
        return basePriceInfo;
    }

    //重新分配奖券list
    public List<Integer> getPriceCountList(List<MerchantPrice> prices){;
        List<Integer> priceList = Lists.newArrayList();
        int totalAll = 0;
        for (MerchantPrice price : prices) {
            int total = price.getTotal();
            totalAll += total;
        }
        for (int i = 1; i <= totalAll; i++) {
            priceList.add(i);
        }
        return priceList;
    }


}
