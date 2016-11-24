package com.myee.tarot.resource.domain;

import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "C_NOTIFICATION")
public class Notification extends GenericEntity<Long, Notification> {
    @Id
    @Column(name = "NOTIFICATION_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", pkColumnValue = "NOTIFICATION_SEQ_NEXT_VAL", valueColumnName = "SEQ_COUNT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;
    @Column(name = "UNIQUE_NO")
    private String uniqueNo;

    @Column(name = "APP_ID")
    private Integer appId;//推送应用

    @Column(name = "TIME_OUT")
    private Long timeout;

    @Column(name = "STORAGE_PATH")
    private String storagePath;

    @Column(name = "CONTENT", columnDefinition = "TEXT", nullable = true)
    private String content;

    @Column(name = "SUCCESS")
    private Boolean success;

    @Column(name = "CREATE_TIME")
    private Date createTime;

    @ManyToOne(targetEntity = MerchantStore.class, optional = false)
    @JoinColumn(name = "STORE_ID")
    protected MerchantStore store;

    @Column(name = "COMMENT")
    private String comment;

    @ManyToOne(targetEntity = AdminUser.class, optional = false)
    @JoinColumn(name = "ADMIN_USER_ID")
    private AdminUser adminUser;

    @Column(name = "NOTICE_TYPE",length = 100)
    private String noticeType;

    @Column(name = "UPDATE_NOTICE_TYPE")
    private String updateNoticeType;

    @Transient//不持久化到数据库，查询关联用
    private DeviceUsed deviceUsed;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public MerchantStore getStore() {
        return store;
    }

    public void setStore(MerchantStore store) {
        this.store = store;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public AdminUser getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public DeviceUsed getDeviceUsed() {
        return deviceUsed;
    }

    public void setDeviceUsed(DeviceUsed deviceUsed) {
        this.deviceUsed = deviceUsed;
    }

}
