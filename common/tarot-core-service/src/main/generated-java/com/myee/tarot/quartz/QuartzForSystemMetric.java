package com.myee.tarot.quartz;

import com.alibaba.fastjson.JSON;
import com.myee.djinn.dto.gather.SystemMetrics;
import com.myee.tarot.catalog.service.DeviceUsedService;
import com.myee.tarot.core.Constants;
import com.myee.tarot.remote.service.SystemMetricsService;
import com.myee.tarot.remote.util.MetricsUtil;
import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.*;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Ray.Fu on 2016/10/13.
 */
@Component
public class QuartzForSystemMetric {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzForSystemMetric.class);

    @Value("${cleverm.push.dirs}")
    private String DOWNLOAD_HOME;

    @Autowired
    private DeviceUsedService deviceUsedService;

    @Autowired
    private SystemMetricsService systemMetricsService;

//    @Scheduled(cron = "0 */5 * * * ?")  //每隔5分钟跑一次
    public void parseFileToDb() {
        //获取路径下所有的csv文件
        File[] fileArr = getFiles(new File(DOWNLOAD_HOME + File.separator + "temp_metric"));
        //解析文件List入库
        importCsvDataToMySql(fileArr);
    }

    /*
     * 通过递归得到某一路径下所有的目录及其文件
    */
    File[] getFiles(File root) {
        File[] files = new File[0];
        // create new filename filter
        FilenameFilter fileNameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.lastIndexOf('.') > 0) {
                    // get last index for '.' char
                    int lastIndex = name.lastIndexOf('.');
                    // get extension
                    String str = name.substring(lastIndex);
                    // match path name extension
                    if (str.equals(".csv")) {
                        return true;
                    }
                }
                return false;
            }
        };
        if (root.isDirectory()) {
            files = root.listFiles(fileNameFilter);
        }
        return files;
    }

    /**
     * 导入csv数据到数据库
     *
     * @param fileArr
     * @return
     */
    public void importCsvDataToMySql(File[] fileArr) {
        try {
            for (final File file : fileArr) {
                if (file.exists()) {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"))) {
                        try (CSVReader csvReader = new CSVReader(br)) {
                            String[] strs = csvReader.readNext();//读一行，第一行是抬头，暂时没用
                            // JSON串转用户对象列表
                            List<SystemMetrics> systemMetricses = JSON.parseArray(strs[0], SystemMetrics.class);
                            //保存入库
                            MetricsUtil.updateSystemMetrics(systemMetricses, deviceUsedService, systemMetricsService);
//                            esUtils.bulkAddList(Constants.ES_QUERY_INDEX, Constants.ES_QUERY_TYPE, voiceLogList);
                            csvReader.close();
                            moveToRecycle(file);
                        }
                    }
                    ;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * 将文件移动到每个店铺下的voicelogbak路径下
     *
     * @param file
     * @return
     */
    public void moveToRecycle(File file) {
        if (file.exists() && file.isFile()) {
            String storeId = file.getName().substring(0, file.getName().indexOf("_"));
            // Destination directory
            File dirNew = new File(DOWNLOAD_HOME + File.separator + storeId + File.separator + Constants.VOICELOG_BAK + File.separator + file.getName().substring(file.getName().indexOf("_")+1));
            if (!dirNew.getParentFile().exists()) {
                dirNew.getParentFile().mkdirs();
            }
            boolean flag = file.renameTo(dirNew);
            if(flag == false) {
                LOGGER.info("同名文件已经存在,移动失败!");
            }
        } else if (file.isDirectory()) {
            File files[] = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                moveToRecycle(files[i]);
            }
        }
    }
}
