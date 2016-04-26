package com.myee.tarot.cms.domain;

import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.core.audit.Auditable;
import com.myee.tarot.core.audit.AuditableListener;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Created by Martin on 2016/4/19.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "C_PAGE_FLD")
@EntityListeners(value = { AuditableListener.class })
public class PageField extends GenericEntity<Long, PageField> {

    @Id
    @Column(name = "PAGE_FLD_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PAGE_FLD_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @Embedded
    protected Auditable auditable = new Auditable();

    @Column(name = "FLD_KEY")
    protected String fieldKey;

    @Column(name = "VALUE")
    protected String stringValue;

    @Column(name = "LOB_VALUE", length = Integer.MAX_VALUE - 1)
    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    protected String lobValue;

    @ManyToOne(targetEntity = Page.class, optional = false, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "PAGE_ID")
    protected Page page;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Auditable getAuditable() {
        return auditable;
    }

    public void setAuditable(Auditable auditable) {
        this.auditable = auditable;
    }

    public String getFieldKey() {
        return fieldKey;
    }

    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }

    public String getLobValue() {
        return lobValue;
    }

    public void setLobValue(String lobValue) {
        this.lobValue = lobValue;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
}
