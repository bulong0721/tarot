package com.myee.tarot.catalog.domain;

import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.core.audit.Auditable;
import com.myee.tarot.core.audit.AuditableListener;
import com.myee.tarot.merchant.domain.MerchantStore;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
}
