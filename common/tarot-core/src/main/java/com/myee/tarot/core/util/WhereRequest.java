package com.myee.tarot.core.util;

import com.myee.tarot.core.util.PageRequest;

import java.util.Date;
import java.util.Map;

/**
 * Created by Ray.Fu on 2016/7/20.
 */
public class WhereRequest extends PageRequest {
    private String eventLevel;

    private String moduleObject;

    private String functionObject;

    private String waitState;

    private String beginDate;

    private String endDate;

    private String keyword;

    private String voiceLogType;

    private Long tableId;

    private Long storeId;

    public String getEventLevel() {
        return eventLevel;
    }

    public void setEventLevel(String eventLevel) {
        this.eventLevel = eventLevel;
    }

    public String getModuleObject() {
        return moduleObject;
    }

    public void setModuleObject(String moduleObject) {
        this.moduleObject = moduleObject;
    }

    public String getFunctionObject() {
        return functionObject;
    }

    public void setFunctionObject(String functionObject) {
        this.functionObject = functionObject;
    }

    public String getWaitState() {
        return waitState;
    }

    public void setWaitState(String waitState) {
        this.waitState = waitState;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getVoiceLogType() {
        return voiceLogType;
    }

    public void setVoiceLogType(String voiceLogType) {
        this.voiceLogType = voiceLogType;
    }

    public Long getTableId() {
        return tableId;
    }
    public void setTableId(Long tableId) {

        this.tableId = tableId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }
}
