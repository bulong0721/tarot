package com.myee.tarot.admin.domain;

import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Martin on 2016/4/11.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "C_ADMIN_PASSWORD_TOKEN")
public class ForgotPasswordSecurityToken extends GenericEntity<String, ForgotPasswordSecurityToken> {

    @Id
    @Column(name = "PASSWORD_TOKEN", nullable = false)
    protected String token;

    @Column(name = "CREATE_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createDate;

    @Column(name = "TOKEN_USED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date tokenUsedDate;

    @Column(name = "ADMIN_USER_ID", nullable = false)
    protected Long adminUserId;

    @Column(name = "TOKEN_USED_FLAG", nullable = false)
    protected boolean tokenUsedFlag;

    @Override
    public String getId() {
        return token;
    }

    @Override
    public void setId(String id) {
        this.token = id;
    }

    public Long getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(Long adminUserId) {
        this.adminUserId = adminUserId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

        ForgotPasswordSecurityToken that = (ForgotPasswordSecurityToken) o;

        return !(token != null ? !token.equals(that.token) : that.token != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (token != null ? token.hashCode() : 0);
        return result;
    }
}
