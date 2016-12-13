package com.myee.tarot.admin.domain;

import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Martin on 2016/4/11.
 */
@Entity
@Table(name = "C_ADMIN_USER")
@DynamicUpdate
public class AdminUser extends GenericEntity<Long, AdminUser> {

    @Id
    @Column(name = "ADMIN_USER_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "ADMIN_USER_SEQ_NEXT_VAL", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @Column(name = "NAME", length = 20, nullable = false)
    private String name;

    @Column(name = "LOGIN", length = 40, nullable = false, unique = true)
    private String login;

    @Column(name = "PASSWORD", nullable = false, length = 40)
    private String password;

    @Column(name = "PHONE_NUMBER", length = 20)
    protected String phoneNumber;

    @NotEmpty
    @Column(name = "ADMIN_EMAIL", length = 60)
    protected String email;

    @Column(name = "ACTIVE_STATUS_FLAG")
    protected Boolean activeStatusFlag = Boolean.TRUE;

    @ManyToOne(fetch = FetchType.LAZY)//懒加载会使用户登录时得到的用户信息中不包含门店，就没办法设置session默认门店__需要调用其中的属性才会加载
    @JoinColumn(name = "STORE_ID")
    private MerchantStore merchantStore;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "LOGIN_ACCESS")
    protected Date lastLogin;

    @Column(name = "LOGIN_IP", length = 20)
    protected String loginIP;

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = MerchantStore.class)
    @JoinTable(name = "C_ADMIN_USER_MERCHANT_STORE_XREF", joinColumns = @JoinColumn(name = "ADMIN_USER_ID", referencedColumnName = "ADMIN_USER_ID"), inverseJoinColumns = @JoinColumn(name = "STORE_ID", referencedColumnName = "STORE_ID"))
    protected Set<MerchantStore> allMerchantStores = new HashSet<MerchantStore>();

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = AdminRole.class)
    @JoinTable(name = "C_ADMIN_USER_ROLE_XREF", joinColumns = @JoinColumn(name = "ADMIN_USER_ID", referencedColumnName = "ADMIN_USER_ID"), inverseJoinColumns = @JoinColumn(name = "ADMIN_ROLE_ID", referencedColumnName = "ADMIN_ROLE_ID"))
    protected Set<AdminRole> allRoles = new HashSet<AdminRole>();

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = AdminPermission.class)
    @JoinTable(name = "C_ADMIN_USER_PERMISSION_XREF", joinColumns = @JoinColumn(name = "ADMIN_USER_ID", referencedColumnName = "ADMIN_USER_ID"), inverseJoinColumns = @JoinColumn(name = "ADMIN_PERMISSION_ID", referencedColumnName = "ADMIN_PERMISSION_ID"))
    protected Set<AdminPermission> allPermissions = new HashSet<AdminPermission>();

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

    public String getLoginIP() {
        return loginIP;
    }

    public void setLoginIP(String loginIP) {
        this.loginIP = loginIP;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Set<MerchantStore> getAllMerchantStores() {
        return allMerchantStores;
    }

    public void setAllMerchantStores(Set<MerchantStore> allMerchantStores) {
        this.allMerchantStores = allMerchantStores;
    }

    public void setAllRoles(Set<AdminRole> allRoles) {
        this.allRoles = allRoles;
    }

    public void setAllPermissions(Set<AdminPermission> allPermissions) {
        this.allPermissions = allPermissions;
    }
}
