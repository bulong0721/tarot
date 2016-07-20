package com.myee.tarot.core.util;

import com.myee.tarot.core.util.PageRequest;

import java.util.Map;

/**
 * Created by Ray.Fu on 2016/7/20.
 */
public class WhereRequest extends PageRequest {
    private String eventLevel;

    private String moduleObject;

    private String functionObject;

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
}
