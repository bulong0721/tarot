package com.myee.tarot.catalog.domain;

import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;
import java.util.*;

/**
 * Created by Martin on 2016/4/11.
 */
@Entity
@Table(name = "C_DEVICE")
public class Device extends GenericEntity<Long, Device> {

    @Id
    @Column(name = "DEVICE_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "DEVICE_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @Column(name = "NAME")
    protected String name;

    @Column(name = "VERSION_NUM")
    protected String versionNum;

    @Column(name = "DESCRIPTION")
    protected String description;

//    @OneToMany(mappedBy = "device", targetEntity = DeviceAttribute.class, cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
//    @MapKey(name = "name")
//    protected Map<String, DeviceAttribute> deviceAttribute = new HashMap<String, DeviceAttribute>();

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

    public String getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(String versionNum) {
        this.versionNum = versionNum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public Map<String, DeviceAttribute> getDeviceAttribute() {
//        return deviceAttribute;
//    }

}
