package com.myee.tarot.weixin.domain;


import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Martin on 2016/1/18.
 */
@Entity
@Table(name = "CA_WAIT_TOKEN")
public class WxWaitToken extends GenericEntity<Long, WxWaitToken> {
    @Id
    @Column(name = "CA_WAIT_TOKEN_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "CA_WAIT_TOKEN_SEQ_NEXT_VAL", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;
    @Column(name = "CA_TABLE_ID")
    private Long tableId;
    @Column(name = "TABLE_TYPE_ID")
    private Long tableTypeId;
    @Column(name = "TOKEN")
    private String token;
    @Column(name = "CHANNEL_TYPE")
    private String channelType;
    @Column(name = "COMMENT")
    private String comment;
    @Column(name = "CA_DINER_ID")
    private Long dinerId;
    @Column(name = "TIME_TOOK")
    private Date timeTook;
    @Column(name = "OPEN_ID")
    private String openId;
    @Column(name = "IDENTITY_CODE")
    private String identityCode;
    @Column(name = "STATE")
    private int state;
    @Column(name = "WAITED_COUNT")
    private Long waitedCount;
    @Column(name = "PREDICT_WAITING_TIME")
    private Long predictWaitingTime;
    @ManyToOne(targetEntity = MerchantStore.class, optional = false)
    @JoinColumn(name = "STORE_ID")
    protected MerchantStore store;
    @Column(name = "ACTIVE")
    private boolean active = true;
    @Column(name = "CREATED")
    private Date created;
    @Column(name = "UPDATED")
    private Date updated;
    @Column(name = "DINNER_COUNT")
    private int dinnerCount;

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
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

    public Long getTableTypeId() {
        return tableTypeId;
    }

    public void setTableTypeId(Long tableTypeId) {
        this.tableTypeId = tableTypeId;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {

    }

    public MerchantStore getStore() {
        return store;
    }

    public void setStore(MerchantStore store) {
        this.store = store;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
