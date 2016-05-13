package com.myee.tarot.catalog.domain;

import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;

/**
 * Created by Martin on 2016/4/11.
 */
@Entity
@Table(name = "C_PRODUCT_OPTION_XREF")
public class DeviceOptionXref extends GenericEntity<Long, DeviceOptionXref> {

    @Id
    @Column(name = "PRODUCT_OPTION_XREF_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PRODUCT_OPTION_XREF_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @ManyToOne(targetEntity = Device.class, optional = false, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "PRODUCT_ID")
    protected Device device = new Device();

    @ManyToOne(targetEntity = DeviceOption.class, optional = false)
    @JoinColumn(name = "PRODUCT_OPTION_ID")
    protected DeviceOption deviceOption = new DeviceOption();

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

    public DeviceOption getDeviceOption() {
        return deviceOption;
    }

    public void setDeviceOption(DeviceOption deviceOption) {
        this.deviceOption = deviceOption;
    }
}
