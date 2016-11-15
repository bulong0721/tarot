package com.myee.tarot.quartz;

import com.myee.tarot.cache.entity.MealsCache;
import com.myee.tarot.cache.util.RedissonUtil;
import com.myee.tarot.cache.view.WxWaitTokenView;
import com.myee.tarot.campaign.service.ClientPrizeGetInfoService;
import com.myee.tarot.campaign.service.ClientPrizeService;
import com.myee.tarot.campaign.service.ClientPrizeTicketService;
import com.myee.tarot.campaign.service.PriceInfoService;
import com.myee.tarot.core.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/7/28.
 */
@Component
public class QuartzForCampaign {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzForCampaign.class);

    @Autowired
    private PriceInfoService priceInfoService;
    @Autowired
    private ClientPrizeGetInfoService clientPrizeGetInfoService;
    @Autowired
    private ClientPrizeService clientPrizeService;
    @Autowired
    private ClientPrizeTicketService clientPrizeTicketService;
    @Autowired
    private RedissonUtil redissonUtil;

    //@Scheduled(cron = "0/5 * *  * * ? ")//测试每隔1秒隔行一次
    public void run(){
        System.out.println("Hello MyJob  " +
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()));
    }

    //大学士抽奖定时器
    @Scheduled(cron = "0 1 0 * * ?")  //每天凌晨0点1分更新奖券
    //@Scheduled(cron = "0 */1 * * * ?")
    public void updateDrawRedisList(){
        LOGGER.info("数据更新开始");
        try {
            priceInfoService.updateRedisDrawList();
        } catch (ServiceException e) {
            LOGGER.error("数据更新失败");
            e.printStackTrace();
        }
        LOGGER.info("数据更新结束");
    }

    //小超人抽奖定时器

    @Scheduled(cron = "0 0 0 * * ?")
    //每天凌晨扫描过期
    public void scanClientPrizeOverDate() {
        LOGGER.info("扫描过期开始");
        try {
            clientPrizeService.scanClientPrizeOverDate();
        } catch (Exception e) {
            LOGGER.error("扫描过期失败");
            e.printStackTrace();
        }
        LOGGER.info("扫描过期结束");
    }

    @Scheduled(cron = "0 0/5 * * * ?") //每隔5分钟执行一次
    //定时清理未领取的奖券
    public void overFiveMinGetInfo() {
        LOGGER.info("检测未领取奖券");
        try {
            clientPrizeGetInfoService.overFiveMinGetInfo();
        } catch (Exception e) {
            LOGGER.error("检测奖券出现错误");
            e.printStackTrace();
        }
        LOGGER.info("检测结束");
    }

    @Scheduled(cron = "0 0 0 * * ?")
    //定时更改电影券
    public void checkExpireTickets() {
        LOGGER.info("检测过期电影券");
        try {
            clientPrizeTicketService.checkExpireTickets();
        } catch (Exception e) {
            LOGGER.error("检测电影券出现错误");
            e.printStackTrace();
        }
        LOGGER.info("检测过期电影券结束");
    }

    @Scheduled(cron = "0 0 0 * * ?")
    //凌晨定时清除就餐排队 缓存
    public void clearMealCache() {
        LOGGER.info("清除就餐排队数据开始");
        try {
            MealsCache mealsCache = redissonUtil.mealsCache();
            if(mealsCache!=null) {
                Map<String,String> openIdInfo = mealsCache.getOpenIdInfo();
                openIdInfo.clear();
                mealsCache.setOpenIdInfo(openIdInfo);
                Map<String,List<WxWaitTokenView>> wxWaitViewMap = mealsCache.getWxWaitTokenCache();
                wxWaitViewMap.clear();
                mealsCache.setWxWaitTokenCache(wxWaitViewMap);
            }
        } catch (Exception e) {
            LOGGER.error("清除就餐排队数据失败");
            e.printStackTrace();
        }
        LOGGER.info("清除就餐排队数据成功");
    }

}
