package com.myee.tarot.quartz;

import com.google.common.collect.Lists;
import com.myee.tarot.catalog.domain.VoiceLog;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.DateUtil;
import com.myee.tarot.datacenter.service.WaitTokenService;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.wechat.domain.WxWaitToken;
import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Ray.Fu on 2016/8/22.
 */
@Component
public class QuartzForWaitToken {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuartzForWaitToken.class);

    @Value("${cleverm.push.dirs}")
    private String DOWNLOAD_HOME;

    @Autowired
    private WaitTokenService waitTokenService;

//    @Scheduled(cron = "* 0/2 * * * ? ")  //每天23PM跑定时任务排号数据文件入库并移动文件
    public void parseFileToDb() {
        //获取路径下所有的csv文件
        File[] fileArr = getFiles(new File(DOWNLOAD_HOME + File.separator + Constants.ADMIN_PACK + File.separator + Constants.WAITTOKEN));
        //解析文件List入库
        importCsvDataToDb(fileArr);
    }

    private void importCsvDataToDb(File[] fileArr) {
        try {
            for (final File file : fileArr) {
                if (file.exists()) {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"))) {
                        try (CSVReader csvReader = new CSVReader(br)) {
                            String[] strs = csvReader.readNext();//读一行，第一行是抬头，暂时没用
                            Iterator<String []> iterator = csvReader.iterator();
                            List<VoiceLog> voiceLogList = Lists.newArrayList();
                            MerchantStore merchantStore = new MerchantStore();
                            WxWaitToken wxWaitToken = new WxWaitToken();
                            while(iterator.hasNext()){
                                String[] ss = iterator.next();
                                wxWaitToken.setChannelType(ss[0]);//日期-时间
                                wxWaitToken.setComment(ss[1]);
                                wxWaitToken.setCreated(ss[2] == null ? null : DateUtil.getDateTime(ss[2]));//Cooky- Listen
                                wxWaitToken.setDinnerCount(Integer.parseInt(ss[3]));//Cooky- Speak
                                merchantStore.setId(Long.parseLong(ss[4]));
                                wxWaitToken.setStore(merchantStore);
                                wxWaitToken.setOpenId(ss[5]);
                                wxWaitToken.setPredictWaitingTime(Long.parseLong(ss[6]));
                                wxWaitToken.setState(Integer.parseInt(ss[7]));
                                wxWaitToken.setTableTypeId(Long.parseLong(ss[8]));
                                wxWaitToken.setTimeTook(ss[9] == null ? null : DateUtil.getDateTime(ss[9]));
                                wxWaitToken.setToken(ss[10]);
                                wxWaitToken.setUpdated(ss[11] == null ? null : DateUtil.getDateTime(ss[11]));
                                wxWaitToken.setWaitedCount(Long.parseLong(ss[12]));
                                waitTokenService.update(wxWaitToken);
                            }
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
     * 将文件移动到每个店铺下的waittokenbak路径下
     *
     * @param file
     * @return
     */
    public void moveToRecycle(File file) {
        if (file.exists() && file.isFile()) {
            String storeId = file.getName().substring(0, file.getName().indexOf("_"));
            // Destination directory
            File dirNew = new File(DOWNLOAD_HOME + File.separator + storeId + File.separator + Constants.WAITTOKEN_BAK + File.separator + file.getName().substring(file.getName().indexOf("_")+1));
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
