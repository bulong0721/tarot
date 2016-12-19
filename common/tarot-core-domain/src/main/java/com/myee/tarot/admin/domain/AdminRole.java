package com.myee.tarot.admin.domain;

import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Martin on 2016/4/11.
 */
@Entity
@Table(name = "C_ADMIN_ROLE")
public class AdminRole extends GenericEntity<Long, AdminRole> {

    @Id
    @Column(name = "ADMIN_ROLE_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "ADMIN_ROLE_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @Column(name = "NAME", unique = true, nullable = false)
    protected String name;

    @Column(name = "DESCRIPTION", nullable = false)
    protected String description;

    /** All users that have this role */
    @ManyToMany(fetch = FetchType.LAZY, targetEntity = AdminUser.class)
    @JoinTable(name = "C_ADMIN_USER_ROLE_XREF", joinColumns = @JoinColumn(name = "ADMIN_ROLE_ID", referencedColumnName = "ADMIN_ROLE_ID"), inverseJoinColumns = @JoinColumn(name = "ADMIN_USER_ID", referencedColumnName = "ADMIN_USER_ID"))
    protected Set<AdminUser> allUsers = new HashSet<AdminUser>();

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = AdminPermission.class)
    @JoinTable(name = "C_ADMIN_ROLE_PERMISSION_XREF", joinColumns = @JoinColumn(name = "ADMIN_ROLE_ID", referencedColumnName = "ADMIN_ROLE_ID"), inverseJoinColumns = @JoinColumn(name = "ADMIN_PERMISSION_ID", referencedColumnName = "ADMIN_PERMISSION_ID"))
    protected Set<AdminPermission> allPermissions = new HashSet<AdminPermission>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<AdminPermission> getAllPermissions() {
        return allPermissions;
    }

    public Set<AdminUser> getAllUsers() {
        return allUsers;
    }

    public void setAllPermissions(Set<AdminPermission> allPermissions) {
        this.allPermissions = allPermissions;
    }

    public void setAllUsers(Set<AdminUser> allUsers) {
        this.allUsers = allUsers;
    }
}
