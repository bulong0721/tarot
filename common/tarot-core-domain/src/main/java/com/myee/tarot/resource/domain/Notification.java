package com.myee.tarot.resource.domain;

import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;

import javax.persistence.*;

@Entity
@Table(name = "C_NOTIFICATION")
public class Notification extends GenericEntity<Long, Notification> {
    @Id
    @Column(name = "NOTIFICATION_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", pkColumnValue = "NOTIFICATION_SEQ_NEXT_VAL", valueColumnName = "SEQ_COUNT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long    id;
    @Column(name = "NAME")
    private String  name;
    @Column(name = "TIME_EXPIRED")
    private Long    timeExpired;
    @Column(name = "NOTIFICATION_TYPE")
    private String  notificationType;
    @Column(name = "CONTENT")
    @Lob
    private String  content;
    @ManyToOne(targetEntity = DeviceUsed.class, optional = false, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "DEVICE_USED_ID")
    protected DeviceUsed deviceUsed;
    @Column(name="APP_ID")
    private Integer appId;
    @ManyToOne(targetEntity = MerchantStore.class, optional = false, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "STORE_ID")
    protected MerchantStore merchantStore;
    @Column(name="MESSAGE")
    private String message;
    @Column(name="STATUS")
    private String status;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTimeExpired() {
        return timeExpired;
    }

    public void setTimeExpired(Long timeExpired) {
        this.timeExpired = timeExpired;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public DeviceUsed getDeviceUsed() {
        return deviceUsed;
    }

    public void setDeviceUsed(DeviceUsed deviceUsed) {
        this.deviceUsed = deviceUsed;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public MerchantStore getMerchantStore() {
        return merchantStore;
    }

    public void setMerchantStore(MerchantStore merchantStore) {
        this.merchantStore = merchantStore;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
