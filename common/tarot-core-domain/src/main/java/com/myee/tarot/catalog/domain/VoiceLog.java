package com.myee.tarot.catalog.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Ray.Fu on 2016/8/10.
 */
@Entity
public class VoiceLog {

    protected String dateTime;

    private String cookieListen;

    private String cookieSpeak;

    private String type;

    protected Long storeId;

    protected String storeName;

    private Long num;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCookieListen() {
        return cookieListen;
    }

    public void setCookieListen(String cookieListen) {
        this.cookieListen = cookieListen;
    }

    public String getCookieSpeak() {
        return cookieSpeak;
    }

    public void setCookieSpeak(String cookieSpeak) {
        this.cookieSpeak = cookieSpeak;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }
}
