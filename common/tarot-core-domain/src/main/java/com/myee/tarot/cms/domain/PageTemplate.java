package com.myee.tarot.cms.domain;

import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.reference.domain.Locale;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin on 2016/4/19.
 */
@Entity
@Table(name="C_PAGE_TMPLT")
public class PageTemplate extends GenericEntity<Long, PageTemplate> {

    @Id
    @Column(name = "PAGE_TMPLT_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PAGE_TMPLT_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @Column(name = "TMPLT_NAME")
    protected String templateName;

    @Column(name = "TMPLT_DESCR")
    protected String templateDescription;

    @Column(name = "TMPLT_PATH")
    protected String templatePath;

    @ManyToOne(targetEntity = Locale.class)
    @JoinColumn(name = "LOCALE_CODE")
    @Deprecated
    protected Locale locale;

    @OneToMany(targetEntity = PageTemplateFieldGroupXref.class, cascade = {CascadeType.ALL}, orphanRemoval = true, mappedBy = "pageTemplate")
    @OrderBy("groupOrder")
    protected List<PageTemplateFieldGroupXref> fieldGroups = new ArrayList<PageTemplateFieldGroupXref>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public List<PageTemplateFieldGroupXref> getFieldGroups() {
        return fieldGroups;
    }

    public void setFieldGroups(List<PageTemplateFieldGroupXref> fieldGroups) {
        this.fieldGroups = fieldGroups;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getTemplateDescription() {
        return templateDescription;
    }

    public void setTemplateDescription(String templateDescription) {
        this.templateDescription = templateDescription;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }
}
