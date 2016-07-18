package com.myee.tarot.admin.domain;

import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;

/**
 * Created by Martin on 2016/4/11.
 */
@Entity
@Table(name = "C_ADMIN_PERMISSION_ENTITY")
public class AdminPermissionQualifiedEntity extends GenericEntity<Long, AdminPermissionQualifiedEntity> {

    @Id
    @Column(name = "ADMIN_PERMISSION_ENTITY_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "ADMIN_PERMISSION_ENTITY_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @Column(name = "CEILING_ENTITY", nullable=false)
    protected String ceilingEntityFullyQualifiedName;

    @ManyToOne(targetEntity = AdminPermission.class)
    @JoinColumn(name = "ADMIN_PERMISSION_ID")
    protected AdminPermission adminPermission;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public AdminPermission getAdminPermission() {
        return adminPermission;
    }

    public void setAdminPermission(AdminPermission adminPermission) {
        this.adminPermission = adminPermission;
    }

    public String getCeilingEntityFullyQualifiedName() {
        return ceilingEntityFullyQualifiedName;
    }

    public void setCeilingEntityFullyQualifiedName(String ceilingEntityFullyQualifiedName) {
        this.ceilingEntityFullyQualifiedName = ceilingEntityFullyQualifiedName;
    }
}
