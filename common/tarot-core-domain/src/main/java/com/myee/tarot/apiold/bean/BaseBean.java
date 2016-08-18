package com.myee.tarot.apiold.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * INFO: 基础类
 * User: enva.liang
 * Date: 16-01-25
 * Time: 上午10:31
 * Version: 1.0
 * History: <p>如果有修改过程，请记录</P>
 */
public class BaseBean implements Serializable {

    private Long clientId;

    private Long orgId;

    private int active;

    private long createdBy;
    private Date created;
    private long updatedBy;
    private Date updated;

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
}
