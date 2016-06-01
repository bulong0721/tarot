package com.myee.tarot.cms.domain;

import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Martin on 2016/4/19.
 */
@Entity
@Table(name = "C_PAGE")
public class Page extends GenericEntity<Long, Page> {

    @Id
    @Column(name = "PAGE_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PAGE_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @ManyToOne(targetEntity = PageTemplate.class)
    @JoinColumn(name = "PAGE_TMPLT_ID")
    protected PageTemplate pageTemplate;

    @Column(name = "DESCRIPTION")
    protected String description;

    @Column(name = "FULL_URL")
    protected String fullUrl;

    @OneToMany(mappedBy = "page", targetEntity = PageField.class, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @MapKey(name = "fieldKey")
    protected Map<String, PageField> pageFields = new HashMap<String, PageField>();

    @Column(name = "PRIORITY")
    @Deprecated
    protected Integer priority;

    @Column(name = "OFFLINE_FLAG")
    protected Boolean offlineFlag = false;

    @Column(name = "EXCLUDE_FROM_SITE_MAP")
    protected Boolean excludeFromSiteMap;

    @OneToMany(mappedBy = "page", targetEntity = PageAttribute.class, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @MapKey(name = "name")
    protected Map<String, PageAttribute> additionalAttributes = new HashMap<String, PageAttribute>();

    @Column(name = "ACTIVE_START_DATE")
    protected Date activeStartDate;

    @Column(name = "ACTIVE_END_DATE")
    protected Date activeEndDate;

    @Column(name = "META_TITLE")
    protected String metaTitle;

    @Column(name = "META_DESCRIPTION")
    protected String metaDescription;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
