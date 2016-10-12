package com.myee.tarot.djinn.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.myee.djinn.dto.CommonResult;
import com.myee.djinn.dto.WaitToken;
import com.myee.djinn.rpc.RemoteException;
import com.myee.djinn.server.operations.MealsService;
import com.myee.tarot.cache.entity.MealsCache;
import com.myee.tarot.cache.uitl.RedissonUtil;
import com.myee.tarot.cache.view.WxWaitTokenView;
import com.myee.tarot.core.service.TransactionalAspectAware;
import com.myee.tarot.datacenter.service.WaitTokenService;
import com.myee.tarot.merchant.service.MerchantStoreService;
import com.myee.tarot.wechat.domain.WxWaitToken;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Martin on 2016/9/6.
 */
@Service
public class MealsServiceImpl implements MealsService, TransactionalAspectAware {

    @Autowired
    private WaitTokenService waitTokenService;
    @Autowired
    private MerchantStoreService merchantStoreService;
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public CommonResult takeNumber(WaitToken waitToken) throws RemoteException {
        //保存至数据库 将领号数据加入
        WxWaitToken wxWaitToken = new WxWaitToken();
        wxWaitToken.setTableTypeId(waitToken.getTableTypeId()); //桌类型
        wxWaitToken.setToken(waitToken.getToken()); //获取餐桌代号 例如：A10
        wxWaitToken.setStore(merchantStoreService.findById(waitToken.getShopId())); //商户id
        wxWaitToken.setCreated(new Date()); //取号时间
        wxWaitToken.setState(waitToken.getWaitStatus());
        WxWaitToken updatedToken = waitTokenService.update(wxWaitToken);
        //同时保存至redis中
        String key = waitToken.getShopId() + "_" + waitToken.getTableTypeId();
        MealsCache mealCache = RedissonUtil.mealsCache(redissonClient);
        if(mealCache.getWxWaitTokenCache() !=null){
            List<WxWaitTokenView> wxWaitTokenViewList = mealCache.getWxWaitTokenCache().get(key);
            WxWaitTokenView wxWaitTokenView = changeToView(updatedToken);
            //设置等待桌数
            if(wxWaitTokenViewList == null){
                List<WxWaitTokenView> newWxWaitTokenViewList = Lists.newArrayList();
                wxWaitTokenView.setWaitedCount(0L);
                newWxWaitTokenViewList.add(wxWaitTokenView);
                mealCache.getWxWaitTokenCache().put(key, newWxWaitTokenViewList);
            }else {
                wxWaitTokenView.setWaitedCount((long) wxWaitTokenViewList.size());
                wxWaitTokenViewList.add(wxWaitTokenView);
                mealCache.getWxWaitTokenCache().put(key, wxWaitTokenViewList);
            }
        } else {
            Map<String, List<WxWaitTokenView>> newMap = Maps.newConcurrentMap();
            List<WxWaitTokenView> tokens = Lists.newArrayList();
            WxWaitTokenView wxWaitTokenView = changeToView(updatedToken);
            wxWaitTokenView.setWaitedCount(0L);
            tokens.add(wxWaitTokenView);
            newMap.put(key, tokens);
            mealCache.setWxWaitTokenCache(newMap);
        }
        return new CommonResult(true);
    }

    @Override
    public boolean skipNumber(WaitToken waitToken) throws RemoteException {
        WxWaitToken wxWaitToken = waitTokenService.findByShopIdAndTokenToday(waitToken.getShopId(), waitToken.getToken());
        if(wxWaitToken!= null){
            wxWaitToken.setUpdated(new Date());
            wxWaitToken.setState(waitToken.getWaitStatus());
            waitTokenService.update(wxWaitToken);
            //更新redis
            String key = waitToken.getShopId() + "_" + waitToken.getTableTypeId();
            MealsCache mealCache = RedissonUtil.mealsCache(redissonClient);
            boolean flag = false;
            if(mealCache.getWxWaitTokenCache() !=null){
                Map<String,List<WxWaitTokenView>> mapView = mealCache.getWxWaitTokenCache();
                List<WxWaitTokenView> wxWaitTokenViewList = mapView.get(key);
                Iterator<WxWaitTokenView> wxWaitIter = wxWaitTokenViewList.iterator();
                while (wxWaitIter.hasNext()){
                    WxWaitTokenView wxWaitTokenView = wxWaitIter.next();
                    if(wxWaitTokenView.getId().equals(wxWaitToken.getId())){
                        wxWaitIter.remove();
                        flag = true;
                    }
                    if(flag){
                        wxWaitTokenView.setWaitedCount(wxWaitTokenView.getWaitedCount() - 1);
                    }
                }
                mapView.put(key,wxWaitTokenViewList);
                mealCache.setWxWaitTokenCache(mapView);
            }
        }
        return true;
    }

	@Override
	public boolean cancelNumber(WaitToken waitToken, String reason) throws RemoteException {
        WxWaitToken wxWaitToken = waitTokenService.findByShopIdAndTokenToday(waitToken.getShopId(), waitToken.getToken());
        if(wxWaitToken!= null){
            wxWaitToken.setUpdated(new Date());
            wxWaitToken.setState(waitToken.getWaitStatus());
            waitTokenService.update(wxWaitToken);
            //更新redis
            String key = waitToken.getShopId() + "_" + waitToken.getTableTypeId();
            MealsCache mealCache = RedissonUtil.mealsCache(redissonClient);
            boolean flag = false;
            if(mealCache.getWxWaitTokenCache() !=null){
                Map<String,List<WxWaitTokenView>> mapView = mealCache.getWxWaitTokenCache();
                List<WxWaitTokenView> wxWaitTokenViewList = mapView.get(key);
                Iterator<WxWaitTokenView> wxWaitIter = wxWaitTokenViewList.iterator();
                while (wxWaitIter.hasNext()){
                    WxWaitTokenView wxWaitTokenView = wxWaitIter.next();
                    if(wxWaitTokenView.getId().equals(wxWaitToken.getId())){
                        wxWaitIter.remove();
                        flag = true;
                    }
                    if(flag){
                        wxWaitTokenView.setWaitedCount(wxWaitTokenView.getWaitedCount() - 1);
                    }
                }
                mapView.put(key,wxWaitTokenViewList);
                mealCache.setWxWaitTokenCache(mapView);
            }
        }
        return true;
	}

	@Override
	public boolean doRepast(WaitToken waitToken, long tableId) throws RemoteException {
        WxWaitToken wxWaitToken = waitTokenService.findByShopIdAndTokenToday(waitToken.getShopId(), waitToken.getToken());
        if(wxWaitToken!= null){
            wxWaitToken.setUpdated(new Date());
            wxWaitToken.setState(waitToken.getWaitStatus());
            waitTokenService.update(wxWaitToken);
            //更新redis
            String key = waitToken.getShopId() + "_" + waitToken.getTableTypeId();
            MealsCache mealCache = RedissonUtil.mealsCache(redissonClient);
            boolean flag = false;
            if(mealCache.getWxWaitTokenCache() !=null){
                Map<String,List<WxWaitTokenView>> mapView = mealCache.getWxWaitTokenCache();
                List<WxWaitTokenView> wxWaitTokenViewList = mapView.get(key);
                Iterator<WxWaitTokenView> wxWaitIter = wxWaitTokenViewList.iterator();
                while (wxWaitIter.hasNext()){
                    WxWaitTokenView wxWaitTokenView = wxWaitIter.next();
                    if(wxWaitTokenView.getId().equals(wxWaitToken.getId())){
                        wxWaitIter.remove();
                        flag = true;
                    }
                    if(flag){
                        wxWaitTokenView.setWaitedCount(wxWaitTokenView.getWaitedCount() - 1);
                    }
                }
                mapView.put(key,wxWaitTokenViewList);
                mealCache.setWxWaitTokenCache(mapView);
            }
        }
        return true;
	}

    @Override
    public List<WaitToken> listOfTableType(long l) throws RemoteException {
        return null;
    }

    public WxWaitTokenView changeToView(WxWaitToken wxWaitToken) {
        WxWaitTokenView wxWaitTokenView = new WxWaitTokenView();
        wxWaitTokenView.setId(wxWaitToken.getId());
        wxWaitTokenView.setToken(wxWaitToken.getToken());
        wxWaitTokenView.setTableTypeId(wxWaitToken.getTableTypeId());
        wxWaitTokenView.setIdentityCode(wxWaitToken.getIdentityCode());
        wxWaitTokenView.setCreated(wxWaitToken.getTimeTook());
        wxWaitTokenView.setState(wxWaitToken.getState());
        return wxWaitTokenView;
    }
}
