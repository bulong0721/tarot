package com.myee.tarot.web.admin.controller.voicelog;

import com.google.common.collect.Lists;
import com.myee.tarot.campaign.service.impl.redis.DateTimeUtils;
import com.myee.tarot.catalog.domain.VoiceLog;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.web.util.StringUtil;
import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

//    /**
//     * 删除
//     * @param log
//     * @return
//     */
//    @RequestMapping(value = "voiceLog/delete", method = RequestMethod.POST)
//    @ResponseBody
//    public AjaxResponse delete(VoiceLog log) {
//        boolean flag = VoiceLogUtil.delete(log);
//        if(flag) {
//            return AjaxResponse.success("删除成功!");
//        } else {
//            return AjaxResponse.failed(-1, "删除失败");
//        }
//    }
//
//    /**
//     * 查询
//     * @return
//     */
//    @RequestMapping(value = "voiceLog/search", method = RequestMethod.POST)
//    @ResponseBody
//    public AjaxPageableResponse search(@RequestParam(value = "time", required = false) Date time,
//                                       @RequestParam(value = "type", required = false) String type,
//                                       @RequestParam(value = "keyword", required = false) String keyword, PageRequest pageRequest) {
//        AjaxPageableResponse resp = new AjaxPageableResponse();
//        pageRequest.setCount(-1);//不分页，查询所有结果
//        PageResult<VoiceLog> pageList = VoiceLogUtil.search(time, type, keyword);
//        List<VoiceLog> voiceLogList = pageList.getList();
//        resp.addEntry("voiceLogList", voiceLogList);
//        resp.setRecordsTotal(pageList.getRecordsTotal());
//        return resp;
//    }

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
    public AjaxPageableResponse pageDevice(Model model, HttpServletRequest request, PageRequest pageRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        try {
            List<VoiceLog> voiceLogList = Lists.newArrayList();
            for (int i = 0; i < 20; i++) {
                VoiceLog voiceLog = new VoiceLog();
                voiceLog.setNum(Long.valueOf(i));
                voiceLog.setDateTime(new SimpleDateFormat("yyyy-MM-dd hh:MM:ss").format(new Date()));
                voiceLog.setType("脱口秀");
                voiceLog.setCookieListen("测试听");
                voiceLog.setCookieSpeak("测试说");
                voiceLog.setStoreName("店铺" + i);
                voiceLogList.add(voiceLog);
            }
            for (VoiceLog voiceLog : voiceLogList) {
                resp.addDataEntry(objectToEntry(voiceLog));
            }
            resp.setRecordsTotal(20);
        } catch (Exception e) {
            logger.error("Error while paging products", e);
        }
        return resp;
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(VoiceLog voiceLog) {
        Map entry = new HashMap();
        entry.put("num",voiceLog.getNum());
        entry.put("time",voiceLog.getDateTime());
        entry.put("storeName",voiceLog.getStoreName());
        entry.put("type",voiceLog.getType());
        entry.put("cookyListen",voiceLog.getCookieListen());
        entry.put("cookySpeak",voiceLog.getCookieSpeak());
        return entry;
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
