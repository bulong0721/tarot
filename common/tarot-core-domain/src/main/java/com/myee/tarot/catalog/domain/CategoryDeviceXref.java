package com.myee.tarot.catalog.domain;

import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Martin on 2016/4/11.
 */
@Entity
@Table(name = "C_CATEGORY_DEVICE_XREF")
public class CategoryDeviceXref extends GenericEntity<Long, CategoryDeviceXref> {

    @Id
    @Column(name = "CATEGORY_DEVICE_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PRODUCT_OPTION_XREF_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @ManyToOne(targetEntity = Device.class, optional = false, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "DEVICE_ID")
    protected Device device = new Device();

    @ManyToOne(targetEntity = Category.class, optional = false, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "CATEGORY_ID")
    protected Category category = new Category();

    /**
     * The display order.
     */
    @Column(name = "DISPLAY_ORDER", precision = 10, scale = 6)
    protected BigDecimal displayOrder;

    @Column(name = "DEFAULT_REFERENCE")
    protected Boolean defaultReference;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Boolean getDefaultReference() {
        return defaultReference;
    }

    public void setDefaultReference(Boolean defaultReference) {
        this.defaultReference = defaultReference;
    }

    public BigDecimal getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(BigDecimal displayOrder) {
        this.displayOrder = displayOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CategoryDeviceXref that = (CategoryDeviceXref) o;

        if (device != null ? !device.equals(that.device) : that.device != null) return false;
        return !(category != null ? !category.equals(that.category) : that.category != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (device != null ? device.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        return result;
    }
}
