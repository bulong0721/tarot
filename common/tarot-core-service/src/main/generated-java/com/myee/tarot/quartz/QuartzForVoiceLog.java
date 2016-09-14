package com.myee.tarot.quartz;

import com.google.common.collect.Lists;
import com.myee.tarot.catalog.domain.VoiceLog;
import com.myee.tarot.catalog.service.impl.elasticSearch.ESUtils;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.DateUtil;
import com.myee.tarot.core.util.StringUtil;
import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import java.io.*;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Ray.Fu on 2016/8/10.
 */
@Component
public class QuartzForVoiceLog {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzForVoiceLog.class);

    @Value("${cleverm.push.dirs}")
    private String DOWNLOAD_HOME;

    @Autowired
    private ESUtils esUtils;

    private static final String TYPE_CHAT = "1";
    private static final String TYPE_TALKSHOW = "2";


//    @Scheduled(cron = "* 0/2 * * * ? ")  //每天23PM跑定时任务语音日志放到Es并移动文件
    public void parseFileToDb() {
        //获取路径下所有的csv文件
        File[] fileArr = getFiles(new File(DOWNLOAD_HOME + File.separator + Constants.ADMIN_PACK + File.separator + Constants.VOICELOG));
        //解析文件List入库
        importCsvDataToEs(fileArr);
    }

    /**
     * 导入csv数据到数据库
     *
     * @param fileArr
     * @return
     */
    public void importCsvDataToEs(File[] fileArr) {
        try {
            for (final File file : fileArr) {
                if (file.exists()) {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"))) {
                        try (CSVReader csvReader = new CSVReader(br)) {
                            String[] strs = csvReader.readNext();//读一行，第一行是抬头，暂时没用
//                            List<String[]> list = csvReader.readAll();//读剩下全部
                            Iterator<String []> iterator = csvReader.iterator();
                            List<VoiceLog> voiceLogList = Lists.newArrayList();
                            while(iterator.hasNext()){
                                String[] ss = iterator.next();
                                VoiceLog voiceLog = new VoiceLog();
                                if (!StringUtil.isBlank(ss[0]) && !StringUtil.isBlank(ss[4])) {
                                    voiceLog.setDateTimeStr(ss[0] + " " + ss[4]);//日期-时间
                                }
                                voiceLog.setDateTime(DateUtil.getDateTime(voiceLog.getDateTimeStr()));
                                voiceLog.setCookieListen(ss[1] == null ? null : ss[1]);//Cooky- Listen
                                voiceLog.setCookieSpeak(ss[2] == null ? null : ss[2].toString());//Cooky- Speak
                                voiceLog.setVoiceType(ss[3] == null ? null : ss[3].toString().equals(Constants.VOICE_LOG_TYPE_CHAT) ? TYPE_CHAT : TYPE_TALKSHOW);
                                voiceLog.setStoreId(ss[5] == null ? null : Long.valueOf(ss[5]));
                                voiceLog.setStoreName(ss[6] == null ? null : ss[6].toString());
                                voiceLogList.add(voiceLog);
                            }
                            esUtils.bulkAddList(Constants.ES_QUERY_INDEX, Constants.ES_QUERY_TYPE, voiceLogList);
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
