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

    private Date beginDate;

    private Date endDate;

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

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
