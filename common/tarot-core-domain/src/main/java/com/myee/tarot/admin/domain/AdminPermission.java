package com.myee.tarot.admin.domain;

import com.myee.tarot.core.GenericEntity;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Martin on 2016/4/11.
 */
@Entity
@Table(name = "C_ADMIN_PERMISSION")
public class AdminPermission extends GenericEntity<Long, AdminPermission> {

    @Id
    @Column(name = "ADMIN_PERMISSION_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "ADMIN_PERMISSION_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @Column(name = "NAME", nullable = false)
    protected String name;

    @Column(name = "PERMISSION_TYPE", nullable = false)
    protected String type; //权限所属菜单等级

    @Column(name = "DESCRIPTION", nullable = false)
    protected String description;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = AdminRole.class)
    @JoinTable(name = "C_ADMIN_ROLE_PERMISSION_XREF", joinColumns = @JoinColumn(name = "ADMIN_PERMISSION_ID", referencedColumnName = "ADMIN_PERMISSION_ID"), inverseJoinColumns = @JoinColumn(name = "ADMIN_ROLE_ID", referencedColumnName = "ADMIN_ROLE_ID"))
    protected Set<AdminRole> allRoles = new HashSet<AdminRole>();

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = AdminUser.class)
    @JoinTable(name = "C_ADMIN_USER_PERMISSION_XREF", joinColumns = @JoinColumn(name = "ADMIN_PERMISSION_ID", referencedColumnName = "ADMIN_PERMISSION_ID"), inverseJoinColumns = @JoinColumn(name = "ADMIN_USER_ID", referencedColumnName = "ADMIN_USER_ID"))
    protected Set<AdminUser> allUsers = new HashSet<AdminUser>();

    @OneToMany(mappedBy = "adminPermission", targetEntity = AdminPermissionQualifiedEntity.class, cascade = {CascadeType.ALL})
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    protected List<AdminPermissionQualifiedEntity> qualifiedEntities = new ArrayList<AdminPermissionQualifiedEntity>();

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = AdminPermission.class)
    @JoinTable(name = "C_ADMIN_PERMISSION_XREF", joinColumns = @JoinColumn(name = "ADMIN_PERMISSION_ID", referencedColumnName = "ADMIN_PERMISSION_ID"), inverseJoinColumns = @JoinColumn(name = "CHILD_PERMISSION_ID", referencedColumnName = "ADMIN_PERMISSION_ID"))
    protected List<AdminPermission> allChildPermissions = new ArrayList<AdminPermission>();

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = AdminPermission.class)
    @JoinTable(name = "C_ADMIN_PERMISSION_XREF", joinColumns = @JoinColumn(name = "CHILD_PERMISSION_ID", referencedColumnName = "ADMIN_PERMISSION_ID"), inverseJoinColumns = @JoinColumn(name = "ADMIN_PERMISSION_ID", referencedColumnName = "ADMIN_PERMISSION_ID"))
    protected List<AdminPermission> allParentPermissions = new ArrayList<AdminPermission>();

    @Column(name = "IS_FRIENDLY")
    protected boolean isFriendly = false; //是否是管理员权限，true管理员权限，false普通权限和管理员都可有

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

    public boolean isFriendly() {
        return isFriendly;
    }

    public void setIsFriendly(boolean isFriendly) {
        this.isFriendly = isFriendly;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AdminPermissionQualifiedEntity> getQualifiedEntities() {
        return qualifiedEntities;
    }

    public void setQualifiedEntities(List<AdminPermissionQualifiedEntity> qualifiedEntities) {
        this.qualifiedEntities = qualifiedEntities;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<AdminPermission> getAllChildPermissions() {
        return allChildPermissions;
    }

    public List<AdminPermission> getAllParentPermissions() {
        return allParentPermissions;
    }

    public Set<AdminRole> getAllRoles() {
        return allRoles;
    }

    public Set<AdminUser> getAllUsers() {
        return allUsers;
    }

    public void setAllRoles(Set<AdminRole> allRoles) {
        this.allRoles = allRoles;
    }

    public void setAllUsers(Set<AdminUser> allUsers) {
        this.allUsers = allUsers;
    }

    public void setAllChildPermissions(List<AdminPermission> allChildPermissions) {
        this.allChildPermissions = allChildPermissions;
    }

    public void setAllParentPermissions(List<AdminPermission> allParentPermissions) {
        this.allParentPermissions = allParentPermissions;
    }
}
