package com.myee.tarot.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Martin on 2016/4/26.
 */
public class GenericResponse {
    private List<String>              errorCodes   = new ArrayList<String>();
    private Map<String, List<String>> errorCodeMap = new HashMap<String, List<String>>();

    public boolean getHasErrors() {
        return errorCodes.size() > 0;
    }

    public List<String> getErrorCodesList() {
        return errorCodes;
    }

    public void addErrorCode(String errorCode) {
        errorCodes.add(errorCode);
        errorCodeMap.put(errorCode, new ArrayList<String>());
    }

    public void addErrorCode(String errorCode, List<String> arguments) {
        errorCodes.add(errorCode);
        errorCodeMap.put(errorCode, arguments);
    }

    public Object[] getErrorCodeArguments(String errorCode) {
        List<String> errorCodes = errorCodeMap.get(errorCode);
        if (errorCodes == null) {
            return new Object[0];
        } else {
            return errorCodes.toArray(new String[0]);
        }
    }
}
