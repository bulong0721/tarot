package com.myee.tarot.core.util.ajax;

import com.alibaba.fastjson.JSON;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.*;

public class AjaxResponse implements Serializable {

    public final static int RESPONSE_STATUS_SUCCESS           = 0;
    public final static int RESPONSE_STATUS_FAIURE            = -1;
    public final static int RESPONSE_STATUS_VALIDATION_FAILED = -2;
    public final static int RESPONSE_OPERATION_COMPLETED      = 9999;
    public final static int CODE_ALREADY_EXIST                = 9998;

    private int status;
    private List<Map<String, Object>> data               = new ArrayList<Map<String, Object>>();
    private Map<String, Object>       dataMap            = new HashMap<String, Object>();
    private Map<String, Object>       validationMessages = new HashMap<String, Object>();

    public Map<String, Object> getValidationMessages() {
        return validationMessages;
    }

    public void setValidationMessages(Map<String, Object> validationMessages) {
        this.validationMessages = validationMessages;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Map<String,Object>> getData() {
        return data;
    }

    public void addDataEntry(Map<String,Object> dataEntry) {
        this.data.add(dataEntry);
    }

    public void addEntry(String key, String value) {
        dataMap.put(key, value);
    }

    public void setErrorMessage(Throwable t) {
        this.setStatusMessage(t.getMessage());
    }

    public void setErrorString(String t) {
        this.setStatusMessage(t);
    }

    public void addValidationMessage(String fieldName, String message) {
        this.validationMessages.put(fieldName, message);
    }

    private String statusMessage = null;

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }

}
