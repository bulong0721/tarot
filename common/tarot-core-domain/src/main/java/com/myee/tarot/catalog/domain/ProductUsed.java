package com.myee.tarot.catalog.domain;

import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Martin on 2016/4/18.
 */
@Entity
@Table(name = "C_PRODUCT_USED")
public class ProductUsed extends GenericEntity<Long, ProductUsed> {

    @Id
    @Column(name = "PRODUCT_USED_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PRODUCT_USED_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @NotEmpty
    @Column(name = "CODE", length = 100, nullable = false)
    private String code;

    @ManyToOne(targetEntity = MerchantStore.class, optional = false)
    @JoinColumn(name = "STORE_ID")
    protected MerchantStore store;

    @NotEmpty
    @Column(name = "NAME")
    protected String name;

    @Column(name = "DESCRIPTION")
    protected String description;

    @Column(name = "DEVICE_NUM")
    protected String deviceNum;

    @OneToMany(mappedBy = "productUsed", targetEntity = ProductUsedAttribute.class, cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
    @MapKey(name = "name")
    protected Map<String, ProductUsedAttribute> productUsedAttribute = new HashMap<String, ProductUsedAttribute>();

    @OneToMany(targetEntity = DeviceUsed.class, cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinTable(name = "C_PRODUCT_USED_DEV_XREF",
            joinColumns = {@JoinColumn(name = "PRODUCT_USED_ID", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "DEVICE_USED_ID", nullable = false, updatable = false)}
    )
    @MapKey(name = "name")
    protected Map<String, DeviceUsed> deviceUsed = new HashMap<String, DeviceUsed>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public MerchantStore getStore() {
        return store;
    }

    public void setStore(MerchantStore store) {
        this.store = store;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, ProductUsedAttribute> getProductUsedAttribute() {
        return productUsedAttribute;
    }

    public String getDeviceNum() {
        return deviceNum;
    }

    public void setDeviceNum(String deviceNum) {
        this.deviceNum = deviceNum;
    }

    public Map<String, DeviceUsed> getDeviceUsed() {
        return deviceUsed;
    }

}
