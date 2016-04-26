package com.myee.tarot.admin.domain;

import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin on 2016/4/11.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "C_ADMIN_SECTION")
public class AdminSection extends GenericEntity<Long, AdminSection> {

    @Id
    @Column(name = "ADMIN_SECTION_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "ADMIN_SECTION_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @Column(name = "NAME", nullable = false)
    @org.hibernate.annotations.Index(name = "ADMINSECTION_NAME_INDEX", columnNames = {"NAME"})
    protected String name;

    @Column(name = "SECTION_KEY", nullable = false, unique = true)
    protected String sectionKey;

    @Column(name = "URL", nullable = true)
    protected String url;

    @ManyToOne(optional = false, targetEntity = AdminModule.class)
    @JoinColumn(name = "ADMIN_MODULE_ID")
    @org.hibernate.annotations.Index(name = "ADMINSECTION_MODULE_INDEX", columnNames = {"ADMIN_MODULE_ID"})
    protected AdminModule module;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = AdminPermission.class)
    @JoinTable(name = "C_ADMIN_SEC_PERM_XREF", joinColumns = @JoinColumn(name = "ADMIN_SECTION_ID", referencedColumnName = "ADMIN_SECTION_ID"), inverseJoinColumns = @JoinColumn(name = "ADMIN_PERMISSION_ID", referencedColumnName = "ADMIN_PERMISSION_ID"))
    protected List<AdminPermission> permissions = new ArrayList<AdminPermission>();

    @Column(name = "CEILING_ENTITY", nullable = true)
    protected String ceilingEntity;

    @Column(name = "DISPLAY_ORDER", nullable = true)
    protected Integer displayOrder;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getCeilingEntity() {
        return ceilingEntity;
    }

    public void setCeilingEntity(String ceilingEntity) {
        this.ceilingEntity = ceilingEntity;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public AdminModule getModule() {
        return module;
    }

    public void setModule(AdminModule module) {
        this.module = module;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AdminPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<AdminPermission> permissions) {
        this.permissions = permissions;
    }

    public String getSectionKey() {
        return sectionKey;
    }

    public void setSectionKey(String sectionKey) {
        this.sectionKey = sectionKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
