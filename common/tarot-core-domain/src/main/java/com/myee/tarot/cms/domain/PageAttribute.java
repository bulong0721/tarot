package com.myee.tarot.cms.domain;

import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;

/**
 * Created by Martin on 2016/4/19.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="C_PAGE_ATTRIBUTES")
public class PageAttribute extends GenericEntity<Long, PageAttribute> {

    @Id
    @Column(name = "ATTRIBUTE_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "ATTRIBUTE_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @Column(name = "FIELD_NAME", nullable = false)
    protected String name;

    @Column(name = "FIELD_VALUE")
    protected String value;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PageAttribute that = (PageAttribute) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        return !(page != null ? !page.equals(that.page) : that.page != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (page != null ? page.hashCode() : 0);
        return result;
    }
}
