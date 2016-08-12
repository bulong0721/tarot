package com.myee.tarot.quartz;

import com.myee.tarot.catalog.domain.VoiceLog;
import com.myee.tarot.core.exception.ServiceException;
import com.myee.tarot.device.service.VoiceLogService;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.merchant.service.MerchantStoreService;
import com.opencsv.CSVReader;
import org.apache.commons.lang3.StringUtils;
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
    private VoiceLogService voiceLogService;

    @Autowired
    private MerchantStoreService merchantStoreService;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;

    @Scheduled(cron = "0 0/5 * * * ?")  //每天定时解析文件到入库
    public void parseFileToDb(){
        //获取路径下所有的csv文件
        List<File> fileList = getFiles(new File(DOWNLOAD_HOME));
        //解析文件List入库
        for (File file : fileList) {
            uploadCSV(file);
        }
    }

    /**
     * 导入csv数据到数据库
     * @param file
     * @return
     */
    public void uploadCSV(File file) {
        try {
            String fileKindString = file.getName().substring(file.getName().lastIndexOf(".")).trim().toLowerCase();
            if(!fileKindString.equals(".csv")){
                return;
            }
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
                                Date now = new Date();
                                for (int i = 0; i < list.size(); i++) {
                                    String[] ss = list.get(i);
                                    VoiceLog voiceLog = new VoiceLog();
                                    if (!StringUtils.isBlank(ss[0]) && !StringUtils.isBlank(ss[4])) {
                                        try {
                                            voiceLog.setTime(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(ss[0] + " " + ss[4]));//日期-时间
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    voiceLog.setCookieListen(ss[1] == null ? null : ss[1]);//Cooky- Listen
                                    voiceLog.setCookieSpeak(ss[2] == null ? null : ss[2].toString());//Cooky- Speak
                                    voiceLog.setType(ss[3] == null ? null : ss[3].toString());
                                    try {
                                        voiceLogService.update(voiceLog);
                                    } catch (ServiceException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            csvReader.close();
                            copyToRecycle(fileOne);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }  catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
    }

    /*
     * 通过递归得到某一路径下所有的目录及其文件
    */
    List<File> getFiles(File root) {
        List<File> fileList = new ArrayList<>();
        File[] files = new File[0];
        if (root.isDirectory()) {
            files = root.listFiles();
        }
        List<MerchantStore> merchantStoreList = merchantStoreService.list();
        List<String> merchantStoreIdList = new ArrayList<>();
        for (MerchantStore merchantStore : merchantStoreList) {
            merchantStoreIdList.add(merchantStore.getId().toString());
        }
        for (File file : files) {
            if (file.isDirectory() && merchantStoreIdList.contains(file.getName())) {
                File[] fArr = file.listFiles();
                for (File f : fArr) {
                    //必须是voiceparser的文件夹下的
                    if (f.getName().equals("voiceparser")) {
                        File[] csvFiles = f.listFiles();
                        List<File> csvUnusedFileList = new ArrayList<>();
                        for (File oneF : csvFiles) {
                            //判断该目录下的文件是否为.csv后缀的，如果是则需要扫描，如果不是则不需要
                            if (oneF.getName().substring(oneF.getName().lastIndexOf(".")).trim().toLowerCase().equals(".csv")) {
                                csvUnusedFileList.add(oneF);
                            }
                        }
                        fileList.addAll(csvUnusedFileList);
                    }
                }
            }
        }
        return fileList;
    }

    /**
     * 将文件名改名
     *
     * @param file
     * @return
     */
    public boolean copyToRecycle(File file) {
        try {
            if (file.exists()) {
                if (file.isFile()) {
                    // Destination directory
                    File dirNew = new File(file.getParent() + File.separator + file.getName()+".del");
                    file.renameTo(dirNew);
                } else if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        copyToRecycle(files[i]);
                    }
                }
                return true;
            } else {
                System.out.println("所改名的文件不存在！" + '\n');
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("unable to delete the folder!");
        }
        return false;
    }
}
