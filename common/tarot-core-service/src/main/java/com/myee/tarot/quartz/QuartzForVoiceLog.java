package com.myee.tarot.quartz;

import com.myee.tarot.catalog.domain.VoiceLog;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.StringUtil;
import com.myee.tarot.device.service.impl.elasticSearch.ESUtils;
import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Ray.Fu on 2016/8/10.
 */
@Component
public class QuartzForVoiceLog {

    private static final Logger logger = LoggerFactory.getLogger(QuartzForVoiceLog.class);

    @Value("${cleverm.push.dirs}")
    private String DOWNLOAD_HOME;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Autowired
    private ESUtils esUtils;

//    @Scheduled(cron = "0 0/1 * * * *")  //每天23PM跑定时任务语音日志放到Es并移动文件
    public void parseFileToDb() {
        //获取路径下所有的csv文件
        File[] fileArr = getFiles(new File(DOWNLOAD_HOME + File.separator + Constants.VOICELOG));
        //解析文件List入库
        for (File file : fileArr) {
            uploadCSV(file);
        }
    }

    /**
     * 导入csv数据到数据库
     *
     * @param file
     * @return
     */
    public void uploadCSV(File file) {
        try {
            final File fileOne = file;
            if (file.exists()) {
                //异步插入数据库
                //用线程池代替原来的new Thread方法
                taskExecutor.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileOne), "GBK"));
                            CSVReader csvReader = new CSVReader(br);
                            String[] strs = csvReader.readNext();//读一行，第一行是抬头，暂时没用
                            List<String[]> list = csvReader.readAll();//读剩下全部
                            if (list != null) {
                                for (int i = 0; i < list.size(); i++) {
                                    String[] ss = list.get(i);
                                    VoiceLog voiceLog = new VoiceLog();
                                    if (!StringUtil.isBlank(ss[0]) && !StringUtil.isBlank(ss[4])) {
                                        voiceLog.setDateTime(ss[0] + " " + ss[4]);//日期-时间
                                    }
                                    voiceLog.setCookieListen(ss[1] == null ? null : ss[1]);//Cooky- Listen
                                    voiceLog.setCookieSpeak(ss[2] == null ? null : ss[2].toString());//Cooky- Speak
                                    voiceLog.setType(ss[3] == null ? null : ss[3].toString().equals("聊天")? "1" : "2");
                                    esUtils.addDomain("log", "voiceLog", voiceLog);
                                }
                            }
                            csvReader.close();
                            moveToRecycle(fileOne);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
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
     * 将文件移动到每个店铺下的voicelogbak路径下
     *
     * @param file
     * @return
     */
    public void moveToRecycle(File file) {
        if (file.exists() && file.isFile()) {
            String storeId = Long.valueOf(file.getName().substring(0, 5)).toString();
            // Destination directory
            File dirNew = new File(DOWNLOAD_HOME + File.separator + storeId + File.separator + Constants.VOICELOGBAK + File.separator + file.getName().substring(5));
            if (!dirNew.getParentFile().exists()) {
                dirNew.getParentFile().mkdirs();
            }
            file.renameTo(dirNew);
        } else if (file.isDirectory()) {
            File files[] = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                moveToRecycle(files[i]);
            }
        }
    }

}
