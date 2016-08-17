package com.myee.tarot.resource.domain;

import com.myee.tarot.catalog.domain.DeviceUsed;
import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;

import javax.persistence.*;

/**
 * Created by Ray.Fu on 2016/8/10.
 */
@Entity
@Table(name = "C_PUSH_RESOURCE")
public class PushResource extends GenericEntity<Long, PushResource> {

    @Id
    @Column(name = "PUSH_RESOURCE_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", pkColumnValue = "PUSH_RESOURCE_SEQ_NEXT_VAL", valueColumnName = "SEQ_COUNT", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;
    @Column(name = "UNIQUE_NO")
    private String uniqueNo;

    @Column(name = "APP_ID")
    private Integer appId;

    @Column(name = "TIME_OUT")
    private Long timeout;

    @Column(name = "STORAGE_PATH")
    private String storagePath;

    @Column(name="CONTENT", columnDefinition="TEXT", nullable=true)
    private String content;

    @Column(name = "SUCCESS")
    private Boolean success;

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public void setId(Long id) {

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
}
