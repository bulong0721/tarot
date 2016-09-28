package com.myee.tarot.catalog.domain;

import com.myee.tarot.core.util.DateUtil;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Ray.Fu on 2016/8/10.
 */
public class VoiceLog {
    private String dateTimeStr;

    private String cookieListen;

    private String cookieSpeak;

    private String voiceType;

    protected Long storeId;

    protected String storeName;

    private Date dateTime;

    private Long merchantId; //商户ID

    private String merchantName; //商户名称

    private String deviceName; //设备名称

    private String deviceType; //设备类型

    public String getDateTimeStr() {
        return dateTimeStr;
    }

    public void setDateTimeStr(String dateTimeStr) {
        this.dateTimeStr = dateTimeStr;
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

    public String getVoiceType() {
        return voiceType;
    }

    public void setVoiceType(String voiceType) {
        this.voiceType = voiceType;
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

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
