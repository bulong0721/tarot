package com.myee.tarot.core.util.ajax;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AjaxResponse implements Serializable {

    public final static int RESPONSE_STATUS_SUCCESS           = 0;
    public final static int RESPONSE_STATUS_FAIURE            = -1;
    public final static int RESPONSE_STATUS_VALIDATION_FAILED = -2;
    public final static int RESPONSE_OPERATION_COMPLETED      = 9999;
    public final static int CODE_ALREADY_EXIST                = 9998;

    protected int status;
    protected List<Object>        rows               = new ArrayList<Object>();
    protected Map<String, Object> dataMap            = new HashMap<String, Object>();
    protected Map<String, Object> validationMessages = new HashMap<String, Object>();

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

    public List<Object> getRows() {
        return rows;
    }

    public void addDataEntry(Map<String, Object> dataEntry) {
        this.rows.add(dataEntry);
    }

    public AjaxResponse addEntry(String key, Object value) {
        dataMap.put(key, value);
        return this;
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

    public static AjaxResponse success() {
        AjaxResponse response = new AjaxResponse();
        response.setStatus(RESPONSE_STATUS_SUCCESS);
        return response;
    }

    public static AjaxResponse failed(int statusCode) {
        AjaxResponse response = new AjaxResponse();
        response.setStatus(statusCode);
        return response;
    }
}
