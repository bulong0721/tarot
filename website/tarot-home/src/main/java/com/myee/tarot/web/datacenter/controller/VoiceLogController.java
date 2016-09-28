package com.myee.tarot.web.datacenter.controller;

import com.google.common.collect.Maps;
import com.myee.tarot.catalog.domain.VoiceLog;
import com.myee.tarot.catalog.service.impl.elasticSearch.EntityQueryDto;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.DateTimeUtils;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.StringUtil;
import com.myee.tarot.core.util.WhereRequest;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.catalog.service.impl.elasticSearch.ESUtils;
import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
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

    private static EntityQueryDto beginDateDto = new EntityQueryDto();
    private static EntityQueryDto endDateDto = new EntityQueryDto();
    private static EntityQueryDto keywordDto = new EntityQueryDto();
    private static EntityQueryDto voiceLogTypeDto = new EntityQueryDto();

    private static final int TYPE_CHAT_NUM = 1;
    private static final int MAX_QUERY_COUNT = 10000;

    /**
     * 下载
     *
     * @param whereRequest
     * @return
     */
    @RequestMapping(value = "voiceLog/download")
    @ResponseBody
    public void download(WhereRequest whereRequest, HttpServletResponse resp) {
        CSVWriter writer = null;
        Map<String, EntityQueryDto> queries = Maps.newHashMap();
        if (!StringUtil.isNullOrEmpty(whereRequest.getBeginDate())) {
            beginDateDto.setFieldValue(DateTimeUtils.toESString(DateTimeUtils.getDateByString(whereRequest.getBeginDate())));
            beginDateDto.setQueryPattern(Constants.ES_QUERY_PATTERN_MUST);
            queries.put("startDate", beginDateDto);
        }
        if (!StringUtil.isNullOrEmpty(whereRequest.getEndDate())) {
            endDateDto.setFieldValue(DateTimeUtils.toESString(DateTimeUtils.getDateByString(whereRequest.getEndDate())));
            endDateDto.setQueryPattern(Constants.ES_QUERY_PATTERN_MUST);
            queries.put("endDate", endDateDto);
        }
        if (!StringUtil.isNullOrEmpty(whereRequest.getKeyword())) {
            keywordDto.setFieldValue(whereRequest.getKeyword());
            keywordDto.setQueryPattern(Constants.ES_QUERY_PATTERN_SHOULD);
            queries.put("cookieListen", keywordDto);
            queries.put("cookieSpeak", keywordDto);
        }
        if (!StringUtil.isNullOrEmpty(whereRequest.getVoiceLogType())) {
            voiceLogTypeDto.setFieldValue(whereRequest.getVoiceLogType());
            voiceLogTypeDto.setQueryPattern(Constants.ES_QUERY_PATTERN_MUST);
            queries.put("voiceType", voiceLogTypeDto);
        }
        try {
            PageResult<VoiceLog> voiceLogPageResult = esUtils.searchPageQueries(Constants.ES_QUERY_INDEX, Constants.ES_QUERY_TYPE, queries, 1, MAX_QUERY_COUNT, VoiceLog.class);//默认上限查询10000条数据
            List<VoiceLog> voiceLogList = voiceLogPageResult.getList();
            resp.setHeader("Content-type", "text/csv;charset=gb2312");
            resp.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(DateTimeUtils.getNormalNameDateTime() + "voiceLog.csv", "utf8"));
            writer = new CSVWriter(resp.getWriter());
            writer.writeNext(new String[]{
                    "序号",
                    "日期",
                    "Cooky提问",
                    "Cooky回答",
                    "种类",
                    "设备类型",
                    "设备名称",
                    "商户"
            });
            for (int i = 0; i < voiceLogList.size(); i++) {
                //写csv文件
                writeCsvFile(writer, voiceLogList.get(i), i);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
        }
    }

    @RequestMapping(value = "voiceLog/paging", method = RequestMethod.GET)
    @ResponseBody
    public AjaxPageableResponse pageDevice(WhereRequest whereRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        Map<String, EntityQueryDto> queries = Maps.newHashMap();
        if (!StringUtil.isNullOrEmpty(whereRequest.getBeginDate())) {
            beginDateDto.setFieldValue(DateTimeUtils.toESString(DateTimeUtils.getDateByStringEs(whereRequest.getBeginDate())));
            beginDateDto.setQueryPattern(Constants.ES_QUERY_PATTERN_MUST);
            queries.put("startDate", beginDateDto);
        }
        if (!StringUtil.isNullOrEmpty(whereRequest.getEndDate())) {
            endDateDto.setFieldValue(DateTimeUtils.toESString(DateTimeUtils.getDateByStringEs(whereRequest.getEndDate())));
            endDateDto.setQueryPattern(Constants.ES_QUERY_PATTERN_MUST);
            queries.put("endDate", endDateDto);
        }
        if (!StringUtil.isNullOrEmpty(whereRequest.getKeyword())) {
            keywordDto.setFieldValue(whereRequest.getKeyword());
            keywordDto.setQueryPattern(Constants.ES_QUERY_PATTERN_SHOULD);
            queries.put("cookieListen", keywordDto);
            queries.put("cookieSpeak", keywordDto);
        }
        if (!StringUtil.isNullOrEmpty(whereRequest.getVoiceLogType())) {
            voiceLogTypeDto.setFieldValue(whereRequest.getVoiceLogType());
            voiceLogTypeDto.setQueryPattern(Constants.ES_QUERY_PATTERN_MUST);
            queries.put("voiceType", voiceLogTypeDto);
        }
        PageResult<VoiceLog> voiceLogList = esUtils.searchPageQueries(Constants.ES_QUERY_INDEX, Constants.ES_QUERY_TYPE, queries, whereRequest.getPage(), whereRequest.getCount(), VoiceLog.class);
        for (VoiceLog voiceLog : voiceLogList.getList()) {
            resp.addDataEntry(objectToEntry(voiceLog));
        }
        resp.setRecordsTotal(voiceLogList.getRecordsTotal());
        return resp;
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(VoiceLog voiceLog) {
        Map entry = new HashMap();
        entry.put("dateTimeStr", voiceLog.getDateTimeStr());
        entry.put("merchantName", voiceLog.getMerchantName());
        entry.put("deviceName",voiceLog.getDeviceName());
        entry.put("deviceType",voiceLog.getDeviceType());
        entry.put("voiceType", voiceLog.getVoiceType().equals(TYPE_CHAT_NUM) ? Constants.VOICE_LOG_TYPE_CHAT : Constants.VOICE_LOG_TYPE_TALKSHOW);
        entry.put("cookyListen", voiceLog.getCookieListen());
        entry.put("cookySpeak", voiceLog.getCookieSpeak());
        return entry;
    }

    private void writeCsvFile(CSVWriter writer, VoiceLog log, Integer i) {
        writer.writeNext(new String[]{
                i + 1 + "",
                StringUtil.nullToString(log.getDeviceType()),
                StringUtil.nullToString(log.getDeviceName()),
                StringUtil.nullToString(log.getDateTimeStr()),
                StringUtil.nullToString(log.getCookieListen()),
                StringUtil.nullToString(log.getCookieSpeak()),
                StringUtil.nullToString(log.getVoiceType()),
                StringUtil.nullToString(log.getMerchantName())
        });
    }
}
