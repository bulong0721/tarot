package com.myee.tarot.cms.domain;

import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Martin on 2016/4/19.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="C_PGTMPLT_FLDGRP_XREF")
public class PageTemplateFieldGroupXref extends GenericEntity<Long, PageTemplateFieldGroupXref> {

    @Id
    @Column(name = "PG_TMPLT_FLD_GRP_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PG_TMPLT_FLD_GRP_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @ManyToOne(targetEntity = PageTemplate.class, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "PAGE_TMPLT_ID")
    protected PageTemplate pageTemplate;

//    @ManyToOne(targetEntity = FieldGroup.class, cascade = {CascadeType.ALL})
//    @JoinColumn(name = "FLD_GROUP_ID")
//    protected FieldGroup fieldGroup;

    @Column(name = "GROUP_ORDER", precision = 10, scale = 6)
    protected BigDecimal groupOrder;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

//    public FieldGroup getFieldGroup() {
//        return fieldGroup;
//    }
//
//    public void setFieldGroup(FieldGroup fieldGroup) {
//        this.fieldGroup = fieldGroup;
//    }

    public BigDecimal getGroupOrder() {
        return groupOrder;
    }

    public void setGroupOrder(BigDecimal groupOrder) {
        this.groupOrder = groupOrder;
    }

    public PageTemplate getPageTemplate() {
        return pageTemplate;
    }

    public void setPageTemplate(PageTemplate pageTemplate) {
        this.pageTemplate = pageTemplate;
    }
}
