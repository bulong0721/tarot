package com.myee.tarot.quartz;

import com.alibaba.fastjson.JSON;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.DateUtil;
import com.myee.tarot.metric.domain.AppInfo;
import com.myee.tarot.metric.domain.MetricInfo;
import com.myee.tarot.metric.domain.SystemMetrics;
import com.myee.tarot.remote.service.AppInfoService;
import com.myee.tarot.remote.service.MetricInfoService;
import com.myee.tarot.remote.service.SystemMetricsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ray.Fu on 2016/11/23.
 */
@Component
public class QuartzForCleanMetricsRecords {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzForCleanMetricsRecords.class);

    @Value("${cleverm.push.dirs}")
    private String DOWNLOAD_HOME;

    @Autowired
    private MetricInfoService metricInfoService;
    @Autowired
    private SystemMetricsService systemMetricsService;
    @Autowired
    private AppInfoService appInfoService;
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Scheduled(cron = "0 30 2 * * ?")
    public void cleanMetricRecords() {
        //异步插入数据库
        //用线程池代替原来的new Thread方法
        //导出SystemMetrics表
        taskExecutor.submit(new Runnable() {
            @Override
            public void run() {
                Date date = new Date();
                String content = "";
                boolean isSucceed = false;
                List<SystemMetrics> systemMetricsList = systemMetricsService.listByCreateTime(date);
                exportSystemMetricsRecords(content, isSucceed, systemMetricsList, date);
            }
        });

        //导出MetricInfo表
        taskExecutor.submit(new Runnable() {
            @Override
            public void run() {
                Date date = new Date();
                String content = "";
                boolean isSucceed = false;
                List<MetricInfo> metricInfoList = metricInfoService.listByCreateTime(date);
                exportMetricInfoRecords(content, isSucceed, metricInfoList, date);
            }
        });

        //导出AppInfo表
        taskExecutor.submit(new Runnable() {
            @Override
            public void run() {
                Date date = new Date();
                String content = "";
                boolean isSucceed = false;
                List<AppInfo> appInfoList = appInfoService.listByCreateTime(date);
                exportAppInfoRecords(content, isSucceed, appInfoList, date);
            }
        });

    }

    /**
     * 写入内容到文件
     *
     * @param c
     * @param filename
     * @return
     */
    public boolean writeContent(String c, String dirname,String filename,boolean isAppend) {
        File f = new File(dirname);
        if (!f.exists())
        {
            f.mkdirs();
        }
        try {
            FileOutputStream fos = new FileOutputStream(dirname + File.separator + filename,isAppend);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            writer.write(c);
            writer.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void exportMetricInfoRecords(String content, boolean isSucceed, List<MetricInfo> metricInfoList, Date date) {
        try {
            List<MetricInfo> tempMetricInfoList = new ArrayList<MetricInfo>();
            for (int i = 0; i < metricInfoList.size(); i++) {
                tempMetricInfoList.add(metricInfoList.get(i));
                if(i >= Constants.METRIC_EXPORT_BUFFER_SIZE && i % Constants.METRIC_EXPORT_BUFFER_SIZE == 0) {
                    content = JSON.toJSONString(tempMetricInfoList, true);
                    isSucceed = writeContent(content, DOWNLOAD_HOME + File.separator + Constants.ADMIN_PACK + File.separator + "metricsData", "metricInfo_"+ DateUtil.formatDate(date) + ".txt", true);
                    tempMetricInfoList.clear();
                } else if (i % Constants.METRIC_EXPORT_BUFFER_SIZE !=0 && i == metricInfoList.size() - 1) {
                    content = JSON.toJSONString(tempMetricInfoList, true);
                    isSucceed = writeContent(content, DOWNLOAD_HOME + File.separator + Constants.ADMIN_PACK + File.separator + "metricsData", "metricInfo_"+ DateUtil.formatDate(date) + ".txt", true);
                }
            }
            //删除数据库的数据
            if (isSucceed) {
                metricInfoService.deleteByTime(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void exportSystemMetricsRecords(String content, boolean isSucceed, List<SystemMetrics> systemMetricsList, Date date) {
        try {
            List<SystemMetrics> tempSystemMetricsList = new ArrayList<SystemMetrics>();
            for (int i = 0; i < systemMetricsList.size(); i++) {
                tempSystemMetricsList.add(systemMetricsList.get(i));
                if(i >= Constants.METRIC_EXPORT_BUFFER_SIZE && i % Constants.METRIC_EXPORT_BUFFER_SIZE == 0) {
                    content = JSON.toJSONString(tempSystemMetricsList, true);
                    isSucceed = writeContent(content, DOWNLOAD_HOME + File.separator + Constants.ADMIN_PACK + File.separator + "metricsData", "systemMetrics_"+ DateUtil.formatDate(date) + ".txt", true);
                    tempSystemMetricsList.clear();
                } else if (i % Constants.METRIC_EXPORT_BUFFER_SIZE !=0 && i == systemMetricsList.size() - 1) {
                    content = JSON.toJSONString(tempSystemMetricsList, true);
                    isSucceed = writeContent(content, DOWNLOAD_HOME + File.separator + Constants.ADMIN_PACK + File.separator + "metricsData", "systemMetrics_"+ DateUtil.formatDate(date) + ".txt", true);
                }
            }
            //删除数据库的数据
            if (isSucceed) {
                systemMetricsService.deleteByTime(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportAppInfoRecords(String content, boolean isSucceed, List<AppInfo> appInfoList, Date date) {
        try {
            List<AppInfo> tempAppInfoList = new ArrayList<AppInfo>();
            for (int i = 0; i < appInfoList.size(); i++) {
                tempAppInfoList.add(appInfoList.get(i));
                if(i >= Constants.METRIC_EXPORT_BUFFER_SIZE && i % Constants.METRIC_EXPORT_BUFFER_SIZE == 0) {
                    content = JSON.toJSONString(tempAppInfoList, true);
                    isSucceed = writeContent(content, DOWNLOAD_HOME + File.separator + Constants.ADMIN_PACK + File.separator + "metricsData", "appInfo_"+ DateUtil.formatDate(date) +".txt", true);
                    tempAppInfoList.clear();
                } else if (i % Constants.METRIC_EXPORT_BUFFER_SIZE !=0 && i == appInfoList.size() - 1) {
                    content = JSON.toJSONString(tempAppInfoList, true);
                    isSucceed = writeContent(content, DOWNLOAD_HOME + File.separator + Constants.ADMIN_PACK + File.separator + "metricsData", "appInfo_"+ DateUtil.formatDate(date) +".txt", true);
                }
            }
            //删除数据库的数据
            if (isSucceed) {
                appInfoService.deleteByTime(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
