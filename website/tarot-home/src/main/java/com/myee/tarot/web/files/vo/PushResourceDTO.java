package com.myee.tarot.web.files.vo;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.List;

public class PushResourceDTO implements Serializable{

    private String uniqueNo;
    private Integer appId;
    private Long timeout;
    private List<ResourceDTO> content;

    public PushResourceDTO() {
    }

    public PushResourceDTO(PushDTO pushDTO) {
        this.uniqueNo = pushDTO.getUniqueNo();
        this.appId = pushDTO.getAppId();
        this.timeout = pushDTO.getTimeout();
    }

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

    public List<ResourceDTO> getContent() {
        return content;
    }

    public void setContent(List<ResourceDTO> content) {
        this.content = content;
    }
}
