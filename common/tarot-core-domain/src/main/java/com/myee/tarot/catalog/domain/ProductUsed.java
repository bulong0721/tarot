package com.myee.tarot.catalog.domain;

import com.myee.tarot.catalog.type.ProductType;
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
    @Column(name = "TYPE")
    protected String type;

    @Column(name = "DESCRIPTION")
    protected String description;

    @Column(name = "PRODUCT_NUM")
    protected String productNum;

//    @OneToMany(mappedBy = "productUsed", targetEntity = ProductUsedAttribute.class)
//    @MapKey(name = "name")
//    protected Map<String, ProductUsedAttribute> productUsedAttribute = new HashMap<String, ProductUsedAttribute>();
//    protected List<ProductUsedAttribute> productUsedAttributeList = new ArrayList<ProductUsedAttribute>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "productUsed")
    private List<ProductUsedAttribute> productUsedAttributeList;

//    @OneToMany(targetEntity = DeviceUsed.class, cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
//    @JoinTable(name = "C_PRODUCT_USED_DEV_XREF",
//            joinColumns = {@JoinColumn(name = "PRODUCT_USED_ID", nullable = false, updatable = false)},
//            inverseJoinColumns = {@JoinColumn(name = "DEVICE_USED_ID", nullable = false, updatable = false)}
//    )
//    @MapKey(name = "name")
//    protected Map<String, DeviceUsed> deviceUsed = new HashMap<String, DeviceUsed>();

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

//    public Map<String, ProductUsedAttribute> getProductUsedAttribute() {
//        return productUsedAttribute;
//    }

    public List<ProductUsedAttribute> getProductUsedAttributeList() {
        return productUsedAttributeList;
    }

    public void setProductUsedAttributeList(List<ProductUsedAttribute> productUsedAttributeList) {
        this.productUsedAttributeList = productUsedAttributeList;
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
