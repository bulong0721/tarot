package com.myee.tarot.catalog.domain;

import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;

/**
 * Created by Martin on 2016/4/11.
 */
@Entity
@Table(name = "C_PRODUCT_USED_ATTRIBUTE")
public class ProductUsedAttribute extends GenericEntity<Long, ProductUsedAttribute> {

    @Id
    @Column(name = "PRO_USED_ATTR_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PRO_USED_ATTR_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @Column(name = "NAME", nullable = false)
    protected String name;

    @Column(name = "VALUE")
    protected String value;

    @Column(name = "SEARCHABLE")
    protected boolean searchable = false;

    @ManyToOne(targetEntity = DeviceUsed.class, optional = false, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "PRODUCT_USED_ID")
    protected ProductUsed productUsed;

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

    public ProductUsed getProductUsed() {
        return productUsed;
    }

    public void setProductUsed(ProductUsed productUsed) {
        this.productUsed = productUsed;
    }

    public boolean isSearchable() {
        return searchable;
    }

    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
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

        ProductUsedAttribute that = (ProductUsedAttribute) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        return !(productUsed != null ? !productUsed.equals(that.productUsed) : that.productUsed != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (productUsed != null ? productUsed.hashCode() : 0);
        return result;
    }
}
