package com.myee.tarot.admin.domain;

import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.core.audit.AuditableListener;
import com.myee.tarot.merchant.domain.MerchantStore;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.*;

/**
 * Created by Martin on 2016/4/11.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "C_ADMIN_USER")
public class AdminUser extends GenericEntity<Long, AdminUser> {

    @Id
    @Column(name = "ADMIN_USER_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "ADMIN_USER_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "LOGIN", nullable = false)
    private String login;

    @NotEmpty
    @Column(name = "PASSWORD", length = 50)
    private String password;

    @Column(name = "PHONE_NUMBER")
    protected String phoneNumber;

    @NotEmpty
    @Column(name = "ADMIN_EMAIL")
    protected String email;

    @Column(name = "ACTIVE_STATUS_FLAG")
    protected Boolean activeStatusFlag = Boolean.TRUE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MERCHANT_ID", nullable = false)
    private MerchantStore merchantStore;

//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "LAST_ACCESS")
//    protected Date lastAccess;
//
//    @Temporal(TemporalType.TIMESTAMP)
//    @Column(name = "LOGIN_ACCESS")
//    protected Date lastLoin;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = AdminRole.class)
    @JoinTable(name = "C_ADMIN_USER_ROLE_XREF", joinColumns = @JoinColumn(name = "ADMIN_USER_ID", referencedColumnName = "ADMIN_USER_ID"), inverseJoinColumns = @JoinColumn(name = "ADMIN_ROLE_ID", referencedColumnName = "ADMIN_ROLE_ID"))
    protected Set<AdminRole> allRoles = new HashSet<AdminRole>();

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = AdminPermission.class)
    @JoinTable(name = "C_ADMIN_USER_PERMISSION_XREF", joinColumns = @JoinColumn(name = "ADMIN_USER_ID", referencedColumnName = "ADMIN_USER_ID"), inverseJoinColumns = @JoinColumn(name = "ADMIN_PERMISSION_ID", referencedColumnName = "ADMIN_PERMISSION_ID"))
    protected Set<AdminPermission> allPermissions = new HashSet<AdminPermission>();

    @OneToMany(mappedBy = "adminUser", targetEntity = AdminUserAttribute.class, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @MapKey(name = "name")
    protected Map<String, AdminUserAttribute> additionalFields = new HashMap<String, AdminUserAttribute>();

    public Map<String, AdminUserAttribute> getAdditionalFields() {
        return additionalFields;
    }

    public Set<AdminPermission> getAllPermissions() {
        return allPermissions;
    }

    public Set<AdminRole> getAllRoles() {
        return allRoles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getActiveStatusFlag() {
        return activeStatusFlag;
    }

    public void setActiveStatusFlag(Boolean activeStatusFlag) {
        this.activeStatusFlag = activeStatusFlag;
    }

    public MerchantStore getMerchantStore() {
        return merchantStore;
    }

    public void setMerchantStore(MerchantStore merchantStore) {
        this.merchantStore = merchantStore;
    }

    //    public Date getLastAccess() {
//        return lastAccess;
//    }
//
//    public void setLastAccess(Date lastAccess) {
//        this.lastAccess = lastAccess;
//    }
//
//    public Date getLastLoin() {
//        return lastLoin;
//    }
//
//    public void setLastLoin(Date lastLoin) {
//        this.lastLoin = lastLoin;
//    }
}
