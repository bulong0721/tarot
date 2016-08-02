package com.myee.tarot.web.files.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class PushDTO implements Serializable{

    private String uniqueNo;
    private Integer appId;
    private Long timeout;
    private String content;


    public String getUniqueNo() {
        return uniqueNo;
    }

    public void setUniqueNo(String uniqueNo) {
        this.uniqueNo = uniqueNo;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
