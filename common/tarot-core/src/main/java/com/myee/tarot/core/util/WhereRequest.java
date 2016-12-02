package com.myee.tarot.core.util;


import com.alibaba.fastjson.JSONObject;

import java.util.List;
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

    private Integer group;

    private Integer analysisLevel;

    private Long actionId;

    private Long merchantId;

    private List<Integer> inList;

    private String queryObj;

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

    public Integer getGroup() {
        return group;
    }

    public void setGroup(Integer group) {
        this.group = group;
    }

    public Integer getAnalysisLevel() {
        return analysisLevel;
    }

    public void setAnalysisLevel(Integer analysisLevel) {
        this.analysisLevel = analysisLevel;
    }

    public Long getActionId() {
        return actionId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public List<Integer> getInList() {
        return inList;
    }

    public void setInList(List<Integer> inList) {
        this.inList = inList;
    }

    public String getQueryObj() {
        return queryObj;
    }

    public void setQueryObj(String queryObj) {
        this.queryObj = queryObj;
    }
}
