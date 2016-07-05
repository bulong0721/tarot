package com.myee.tarot.catalog.domain;

import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;

/**
 * Created by Martin on 2016/4/11.
 */
@Entity
@Table(name = "C_DEVICE_USED_ATTRIBUTE")
public class DeviceUsedAttribute extends GenericEntity<Long, DeviceUsedAttribute> {

    @Id
    @Column(name = "DEV_USED_ATTR_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "DEV_USED_ATTR_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @Column(name = "NAME", nullable = false)
    protected String name;

    @Column(name = "VALUE")
    protected String value;

    @Column(name = "SEARCHABLE")
    protected boolean searchable = false;

    @ManyToOne(targetEntity = DeviceUsed.class, optional = false, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "DEVICE_USED_ID",updatable = false)
    @org.hibernate.annotations.Index(name = "DEVICE_USED_ATTRIBUTE_INDEX", columnNames = {"DEVICE_USED_ID"})
    protected DeviceUsed deviceUsed;

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

    public DeviceUsed getDeviceUsed() {
        return deviceUsed;
    }

    public void setDeviceUsed(DeviceUsed deviceUsed) {
        this.deviceUsed = deviceUsed;
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

        DeviceUsedAttribute that = (DeviceUsedAttribute) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        return !(deviceUsed != null ? !deviceUsed.equals(that.deviceUsed) : that.deviceUsed != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (deviceUsed != null ? deviceUsed.hashCode() : 0);
        return result;
    }
}
