package com.myee.tarot.catalog.domain;

import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    @Column(name = "DESCRIPTION")
    private String  description;
    @Column(name = "NOTIFICATION_TYPE")
    private String  notificationType;
    @Column(name = "CONTENT")
    @Lob
    private String  content;
    @Column(name = "BROADCAST")
    private boolean broadcast;
    @Column(name = "FEEDBACK")
    private boolean feedback;
    @Column(name = "TIME_EXPIRED")
    private Date    timeExpired;
    @Column(name = "PUSH_STATUS")
    private int     pushStatus;

    @ManyToOne(targetEntity = DeviceUsed.class, optional = false)
    @JoinColumn(name = "DEVICE_USED_ID")
    protected DeviceUsed deviceUsed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public DeviceUsed getDeviceUsed() {
        return deviceUsed;
    }

    public void setDeviceUsed(DeviceUsed deviceUsed) {
        this.deviceUsed = deviceUsed;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isBroadcast() {
        return broadcast;
    }

    public void setBroadcast(boolean broadcast) {
        this.broadcast = broadcast;
    }

    public boolean isFeedback() {
        return feedback;
    }

    public void setFeedback(boolean feedback) {
        this.feedback = feedback;
    }

    public Date getTimeExpired() {
        return timeExpired;
    }

    public void setTimeExpired(Date timeExpired) {
        this.timeExpired = timeExpired;
    }

    public int getPushStatus() {
        return pushStatus;
    }

    public void setPushStatus(int pushStatus) {
        this.pushStatus = pushStatus;
    }

}
