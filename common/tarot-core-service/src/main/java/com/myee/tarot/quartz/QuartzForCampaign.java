package com.myee.tarot.quartz;



import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/7/28.
 */
@Component
public class QuartzForCampaign {

    //@Scheduled(cron = "0/1 * *  * * ? ")//每隔1秒隔行一次
    public void run(){
        System.out.println("Hello MyJob  "+
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ").format(new Date()));
    }
}
