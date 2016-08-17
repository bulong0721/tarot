package com.myee.tarot.web.datacenter.controller;

import com.google.common.collect.Maps;
import com.myee.tarot.campaign.service.impl.redis.DateTimeUtils;
import com.myee.tarot.catalog.domain.VoiceLog;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.device.service.impl.elasticSearch.ESUtils;
import com.myee.tarot.web.util.StringUtil;
import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Ray.Fu on 2016/8/12.
 */
@Controller
public class VoiceLogController {

    private static final Logger logger = LoggerFactory.getLogger(VoiceLogController.class);

    @Value("${cleverm.push.dirs}")
    private String DOWNLOAD_HOME;

    @Value("${cleverm.push.http}")
    private String DOWNLOAD_HTTP;

    @Autowired
    private ESUtils esUtils;

    /**
     * 下载
     * @param startDate
     * @param endDate
     * @param type
     * @param keyword
     * @return
     */
    @RequestMapping(value = "voiceLog/download", method = {RequestMethod.POST})
    @ResponseBody
    public AjaxResponse download(@RequestParam(value = "startDate", required = false) String startDate,
                                 @RequestParam(value = "endDate", required = false) String endDate,
                                 @RequestParam(value = "type", required = false) String type,
                                 @RequestParam(value = "keyword", required = false) String keyword, HttpServletRequest request) {
        CSVWriter writer = null;
        AjaxResponse resp = new AjaxResponse();
        String fileName = null;
        try {
//            PageResult<VoiceLog> pageList = VoiceLogUtil.search(time, type, keyword);
//            List<VoiceLog> voiceLogList = pageList.getList();
            List<VoiceLog> voiceLogList = new ArrayList<>();
            for(Integer j = 0; j < 10; j++) {
                VoiceLog voiceLog = new VoiceLog();
                if (j%2 == 0) {
                    voiceLog.setType("聊天");
                } else {
                    voiceLog.setType("脱口秀");
                }
                voiceLog.setNum(Long.valueOf(j));
                voiceLog.setCookieListen("wowowo" + j);
                voiceLog.setCookieSpeak("我勒个去" + j);
                voiceLog.setStoreId(100L);
                voiceLog.setStoreName("店铺100" + j);
                voiceLog.setDateTime(new SimpleDateFormat("yyyy-MM-dd hh:MM:ss").format(new Date()));
                voiceLogList.add(voiceLog);
            }
            String fileParentPath = DOWNLOAD_HOME + File.separator + "temp";
            String fileFullName = DOWNLOAD_HOME + File.separator + "temp" + File.separator + "语音日志" + DateTimeUtils.getNormalNameDateTime() + ".csv";
            File tempFilePath = new File(fileParentPath);
            if (!tempFilePath.isDirectory()) {
                //创建目录
                tempFilePath.mkdirs();
            }
            File tempFile = new File(fileFullName);
            tempFile.createNewFile();
            //用GBK等中文字符集，下载文件后用excel打开不会乱码。用无BOM编码的utf-8字符集，excel乱码，txt不乱码。
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(tempFile.getPath()), "gb2312");
            writer = new CSVWriter(osw, ',');
            writer.writeNext(new String[]{
                    "序号",
                    "日期",
                    "Cooky提问",
                    "Cooky回答",
                    "种类",
                    "商铺"
            });
            for (int i = 0; i < voiceLogList.size(); i++) {
                //写csv文件
                writeCsvFile(writer, voiceLogList.get(i), i);
            }
            fileName = DOWNLOAD_HTTP + File.separator + "temp" + File.separator + "语音日志" + DateTimeUtils.getNormalNameDateTime() + ".csv";
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
        }
        resp.addEntry("filePath", fileName);
        return resp;
    }

    @RequestMapping(value = "voiceLog/paging", method = RequestMethod.GET)
    @ResponseBody
    public AjaxPageableResponse pageDevice(@RequestParam(value = "startDate", required = false) Date startDate,
                                           @RequestParam(value = "endDate", required = false) Date endDate,
                                           @RequestParam(value = "type", required = false) String type,
                                           @RequestParam(value = "keyword", required = false) String keyword, PageRequest pageRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        pageRequest.setCount(-1);//不分页，查询所有结果
        Map<String, String> queries = Maps.newHashMap();
        /*queries.put("startDate",startDate);
        queries.put("endDate",endDate);*/
        queries.put("type", type);
        queries.put("cookieListen", keyword);
        List<VoiceLog> voiceLogList = esUtils.searchPageQueries("log", "voiceLog", queries, 1, 10, VoiceLog.class);
        resp.addEntry("voiceLogList", voiceLogList);
        resp.setRecordsTotal(voiceLogList.size());
        return resp;
    }

    private void writeCsvFile(CSVWriter writer, VoiceLog log, Integer i) {
        writer.writeNext(new String[]{
                i + 1 + "",
                StringUtil.nullToString(log.getDateTime()),
                StringUtil.nullToString(log.getCookieListen()),
                StringUtil.nullToString(log.getCookieSpeak()),
                StringUtil.nullToString(log.getType()),
                StringUtil.nullToString(log.getStoreName())
        });
    }
}