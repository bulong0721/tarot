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
@Table(name = "C_ADMIN_MODULE")
public class AdminModule extends GenericEntity<Long, AdminModule> {

    @Id
    @Column(name = "ADMIN_MODULE_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "ADMIN_MODULE_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @Column(name = "NAME", nullable = false)
    protected String name;

    @Column(name = "MODULE_KEY", nullable = false)
    protected String moduleKey;

    @Column(name = "ICON", nullable = true)
    protected String icon;

    @OneToMany(mappedBy = "module", targetEntity = AdminSection.class)
    protected List<AdminSection> sections = new ArrayList<AdminSection>();

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

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getModuleKey() {
        return moduleKey;
    }

    public void setModuleKey(String moduleKey) {
        this.moduleKey = moduleKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AdminSection> getSections() {
        return sections;
    }

    public void setSections(List<AdminSection> sections) {
        this.sections = sections;
    }
}
