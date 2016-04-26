package com.myee.tarot.profile.domain;

import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Martin on 2016/4/14.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "C_CUSTOMER_PASSWORD_TOKEN")
public class CustomerForgotPasswordSecurityToken extends GenericEntity<String, CustomerForgotPasswordSecurityToken> {
    @Id
    @Column(name = "PASSWORD_TOKEN", unique = true, nullable = false)
    protected String id;

    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createDate;

    @Column(name = "TOKEN_USED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date tokenUsedDate;

    @Column(name = "CUSTOMER_ID", nullable = false)
    protected Long customerId;

    @Column(name = "TOKEN_USED_FLAG", nullable = false)
    protected boolean tokenUsedFlag;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Date getTokenUsedDate() {
        return tokenUsedDate;
    }

    public void setTokenUsedDate(Date tokenUsedDate) {
        this.tokenUsedDate = tokenUsedDate;
    }

    public boolean isTokenUsedFlag() {
        return tokenUsedFlag;
    }

    public void setTokenUsedFlag(boolean tokenUsedFlag) {
        this.tokenUsedFlag = tokenUsedFlag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CustomerForgotPasswordSecurityToken that = (CustomerForgotPasswordSecurityToken) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}
