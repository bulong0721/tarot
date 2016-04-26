package com.myee.tarot.admin.domain;

import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;

/**
 * Created by Martin on 2016/4/11.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "C_ADMIN_USER_ADDTL_FIELDS")
public class AdminUserAttribute extends GenericEntity<Long, AdminUserAttribute> {

    @Id
    @Column(name = "ATTRIBUTE_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "ADMIN_USER_ADDTL_FIELDS_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @Column(name = "FIELD_NAME", nullable = false)
    @org.hibernate.annotations.Index(name = "ADMINUSERATTRIBUTE_NAME_INDEX", columnNames = {"NAME"})
    protected String name;

    @Column(name = "FIELD_VALUE")
    protected String value;

    @ManyToOne(targetEntity = AdminUser.class, optional = false)
    @JoinColumn(name = "ADMIN_USER_ID")
    @org.hibernate.annotations.Index(name = "ADMINUSERATTRIBUTE_INDEX", columnNames = {"ADMIN_USER_ID"})
    protected AdminUser adminUser;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public AdminUser getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
