package com.myee.tarot.catalog.domain;

import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;

import javax.persistence.*;

/**
 * Created by Martin on 2016/4/18.
 */
@Entity
@Table(name = "C_DEVICE_USED")
public class DeviceUsed extends GenericEntity<Long, DeviceUsed> {

    @Id
    @Column(name = "DEVICE_USED_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "DEVICE_USED_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @ManyToOne(targetEntity = MerchantStore.class, optional = false)
    @JoinColumn(name = "STORE_ID")
    protected MerchantStore store;

    @ManyToOne(targetEntity = ProductUsed.class, optional = false)
    @JoinTable(name = "C_PRODUCT_USED_DEV_XREF",
            joinColumns = {@JoinColumn(name = "DEVICE_USED_ID", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "PRODUCT_USED_ID", nullable = false, updatable = false)}
    )
    protected ProductUsed productUsed;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public ProductUsed getProductUsed() {
        return productUsed;
    }

    public void setProductUsed(ProductUsed productUsed) {
        this.productUsed = productUsed;
    }

    public MerchantStore getStore() {
        return store;
    }

    public void setStore(MerchantStore store) {
        this.store = store;
    }
}
