package com.myee.tarot.quartz;



import com.myee.tarot.campaign.domain.MerchantActivity;
import com.myee.tarot.campaign.service.PriceInfoService;
import com.myee.tarot.core.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/7/28.
 */
@Component
public class QuartzForCampaign {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzForCampaign.class);

    @Autowired
    private PriceInfoService priceInfoService;

    //@Scheduled(cron = "0/1 * *  * * ? ")//测试每隔1秒隔行一次
    public void run(){
        System.out.println("Hello MyJob  "+
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()));
    }

    @Scheduled(cron = "0 0 0 * * ?")  //每天凌晨0点更新redisDrawList
    //@Scheduled(cron = "0 */1 * * * ?")
    public void updateDrawRedisList(){
        LOGGER.info("redis更新开始");
        try {
            priceInfoService.updateRedisDrawList();
        } catch (ServiceException e) {
            LOGGER.error("redis更新失败");
            e.printStackTrace();
        }
        LOGGER.info("redis更新结束");
    }
}
