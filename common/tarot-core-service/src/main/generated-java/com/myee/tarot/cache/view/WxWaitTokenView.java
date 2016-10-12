package com.myee.tarot.cache.view;

import java.util.Date;

/**
 * Created by Administrator on 2016/9/28.
 */
public class WxWaitTokenView {

    private Long id;

    private Long tableId;

    private Long tableTypeId;

    private String token;

    private String channelType;

    private String comment;

    private Long dinerId;

    private Date timeTook;

    private String openId;

    private String identityCode;

    private int state;

    private Long waitedCount;

    private Long predictWaitingTime;

    private boolean active = true;

    private Date created;

    private Date updated;

    private int dinnerCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public Long getTableTypeId() {
        return tableTypeId;
    }

    public void setTableTypeId(Long tableTypeId) {
        this.tableTypeId = tableTypeId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getDinerId() {
        return dinerId;
    }

    public void setDinerId(Long dinerId) {
        this.dinerId = dinerId;
    }

    public Date getTimeTook() {
        return timeTook;
    }

    public void setTimeTook(Date timeTook) {
        this.timeTook = timeTook;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getIdentityCode() {
        return identityCode;
    }

    public void setIdentityCode(String identityCode) {
        this.identityCode = identityCode;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Long getWaitedCount() {
        return waitedCount;
    }

    public void setWaitedCount(Long waitedCount) {
        this.waitedCount = waitedCount;
    }

    public Long getPredictWaitingTime() {
        return predictWaitingTime;
    }

    public void setPredictWaitingTime(Long predictWaitingTime) {
        this.predictWaitingTime = predictWaitingTime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public int getDinnerCount() {
        return dinnerCount;
    }

    public void setDinnerCount(int dinnerCount) {
        this.dinnerCount = dinnerCount;
    }
}

