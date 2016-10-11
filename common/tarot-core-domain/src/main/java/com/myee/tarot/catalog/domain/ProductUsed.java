package com.myee.tarot.catalog.domain;

import com.myee.tarot.catalog.type.ProductType;
import com.myee.tarot.catalog.view.ProductUsedView;
import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Martin on 2016/4/18.
 */
@Entity
@Table(name = "C_PRODUCT_USED")
public class ProductUsed extends GenericEntity<Long, ProductUsed> {

    @Id
    @Column(name = "PRODUCT_USED_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PRODUCT_USED_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @NotEmpty
    @Column(name = "CODE", length = 100, nullable = false)
    private String code;

    @ManyToOne(targetEntity = MerchantStore.class, optional = false)
    @JoinColumn(name = "STORE_ID")
    protected MerchantStore store;

    @NotEmpty
    @Column(name = "TYPE", length = 100)
    protected String type;

    @Column(name = "DESCRIPTION", length = 255)
    protected String description;

    @Column(name = "PRODUCT_NUM", length = 100)
    protected String productNum;

    @ManyToMany(targetEntity = DeviceUsed.class, cascade = CascadeType.REFRESH)
    @JoinTable(name = "C_PRODUCT_USED_DEV_XREF",
            joinColumns = {@JoinColumn(name = "PRODUCT_USED_ID", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "DEVICE_USED_ID", nullable = false)}
    )
    protected List<DeviceUsed> deviceUsed;

    @OneToMany(mappedBy = "productUsed", targetEntity = ProductUsedAttribute.class, fetch = FetchType.LAZY)
    protected List<ProductUsedAttribute> attributes = new ArrayList<ProductUsedAttribute>();

//    @OneToMany(targetEntity = DeviceUsed.class, cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
//    @JoinTable(name = "C_PRODUCT_USED_DEV_XREF",
//            joinColumns = {@JoinColumn(name = "PRODUCT_USED_ID", nullable = false, updatable = false)},
//            inverseJoinColumns = {@JoinColumn(name = "DEVICE_USED_ID", nullable = false, updatable = false)}
//    )
//    @MapKey(name = "name")
//    protected Map<String, DeviceUsed> deviceUsed = new HashMap<String, DeviceUsed>();

    public ProductUsed(){}

    public ProductUsed(ProductUsedView productUsedView){
        this.id = productUsedView.getId();
        this.code = productUsedView.getCode();
        this.type = productUsedView.getType();
        this.productNum = productUsedView.getProductNum();
        this.description = productUsedView.getDescription();
    }

    @Override
    public Long getId() {
        return id;
    }

    public List<DeviceUsed> getDeviceUsed() {
        return deviceUsed;
    }

    public void setDeviceUsed(List<DeviceUsed> deviceUsed) {
        this.deviceUsed = deviceUsed;
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

    @Transient
    public String getName() {
        return ProductType.getName(this.type);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ProductUsedAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<ProductUsedAttribute> attributes) {
        this.attributes = attributes;
    }

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
//    public Map<String, DeviceUsed> getDeviceUsed() {
//        return deviceUsed;
//    }

}
