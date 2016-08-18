package com.myee.tarot.web.datacenter.controller;

import com.google.common.collect.Maps;
import com.myee.tarot.campaign.service.impl.redis.DateTimeUtils;
import com.myee.tarot.catalog.domain.VoiceLog;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.WhereRequest;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.core.web.EntityQueryDto;
import com.myee.tarot.device.service.impl.elasticSearch.ESUtils;
import com.myee.tarot.web.util.StringUtil;
import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(VoiceLogController.class);

    @Value("${cleverm.push.dirs}")
    private String DOWNLOAD_HOME;

    @Value("${cleverm.push.http}")
    private String DOWNLOAD_HTTP;

    @Autowired
    private ESUtils esUtils;

    /**
     * 下载
     *
     * @param whereRequest
     * @param request
     * @return
     */
    @RequestMapping(value = "voiceLog/download", method = {RequestMethod.POST})
    @ResponseBody
    public AjaxResponse download(WhereRequest whereRequest, HttpServletRequest request) {
        CSVWriter writer = null;
        AjaxResponse resp = new AjaxResponse();
        String fileName = null;
        try {
//            PageResult<VoiceLog> pageList = VoiceLogUtil.search(time, type, keyword);
//            List<VoiceLog> voiceLogList = pageList.getList();
            List<VoiceLog> voiceLogList = new ArrayList<VoiceLog>();
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
            for (Integer j = 0; j < 10; j++) {
                VoiceLog voiceLog = new VoiceLog();
                if (j % 2 == 0) {
                    voiceLog.setVoiceType("聊天");
                } else {
                    voiceLog.setVoiceType("脱口秀");
                }
                voiceLog.setCookieListen("wowowo" + j);
                voiceLog.setCookieSpeak("我勒个去" + j);
                voiceLog.setStoreId(100L);
                voiceLog.setStoreName("店铺100" + j);
                voiceLog.setDateTimeStr(new SimpleDateFormat("yyyy-MM-dd hh:MM:ss").format(new Date()));
                voiceLogList.add(voiceLog);
                //写csv文件
                writeCsvFile(writer, voiceLogList.get(j), j);
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
    public AjaxPageableResponse pageDevice(WhereRequest whereRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        Map<String, EntityQueryDto> queries = Maps.newHashMap();
        if (whereRequest.getBeginDate().equals(null) && whereRequest.getEndDate().equals(null)
                && StringUtil.isNullOrEmpty(whereRequest.getKeyword()) && StringUtil.isNullOrEmpty(whereRequest.getVoiceLogType())) {
            queries = null;
        } else {
            EntityQueryDto beginDateDto = new EntityQueryDto();
            beginDateDto.setFieldValue(whereRequest.getBeginDate());
            beginDateDto.setQueryPattern(Constants.ES_QUERY_PATTERN_MUST);

            EntityQueryDto endDateDto = new EntityQueryDto();
            endDateDto.setFieldValue(whereRequest.getEndDate());
            endDateDto.setQueryPattern(Constants.ES_QUERY_PATTERN_MUST);

            EntityQueryDto keywordDto = new EntityQueryDto();
            keywordDto.setFieldValue(whereRequest.getKeyword());
            keywordDto.setQueryPattern(Constants.ES_QUERY_PATTERN_SHOULD);

            EntityQueryDto voiceLogTypeDto = new EntityQueryDto();
            voiceLogTypeDto.setFieldValue(whereRequest.getVoiceLogType());
            voiceLogTypeDto.setQueryPattern(Constants.ES_QUERY_PATTERN_MUST);

            queries.put("beginDate", beginDateDto);
            queries.put("endDate", endDateDto);
            queries.put("voiceType", voiceLogTypeDto);
            queries.put("cookieListen", keywordDto);
            queries.put("cookieSpeak", keywordDto);
        }
        PageResult<VoiceLog> voiceLogList = esUtils.searchPageQueries("log5", "voiceLog5", queries, whereRequest.getPage(), whereRequest.getCount(), VoiceLog.class);
        for (VoiceLog voiceLog : voiceLogList.getList()) {
            resp.addDataEntry(objectToEntry(voiceLog));
        }
        resp.setRecordsTotal(voiceLogList.getRecordsTotal());
        return resp;
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(VoiceLog voiceLog) {
        Map entry = new HashMap();
        entry.put("dateTime", voiceLog.getDateTime());
        entry.put("storeName", voiceLog.getStoreName());
        entry.put("voiceType", voiceLog.getVoiceType());
        entry.put("cookyListen", voiceLog.getCookieListen());
        entry.put("cookySpeak", voiceLog.getCookieSpeak());
        return entry;
    }

    private void writeCsvFile(CSVWriter writer, VoiceLog log, Integer i) {
        writer.writeNext(new String[]{
                i + 1 + "",
                StringUtil.nullToString(log.getDateTime()),
                StringUtil.nullToString(log.getCookieListen()),
                StringUtil.nullToString(log.getCookieSpeak()),
                StringUtil.nullToString(log.getVoiceType()),
                StringUtil.nullToString(log.getStoreName())
        });
    }
}
