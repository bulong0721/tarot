package com.myee.tarot.configuration.domain;

import com.myee.tarot.catalog.domain.ProductUsed;
import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ray.Fu on 2016/12/19.
 */
@Entity
@Table(name = "C_RECEIPT_PRINTED")
public class ReceiptPrinted extends GenericEntity<Long, ReceiptPrinted> {

    @Id
    @Column(name = "RECEIPT_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "RECEIPT_PRINTED_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @Column(name = "MODULE_NAME", nullable = false)
    private String moduleName; //模板名称

    @Column(name = "DESCRIPTION")
    private String description; //描述

    @Column(name = "RECEIPT_TYPE")
    private String receiptType; //小票类型

    @Column(name = "UPDATE_TIME")
    private Date updateTime; //修改时间

    @Transient //供前端显示用，不关联查询出来
    protected List<ProductUsed> productUsed = new ArrayList<ProductUsed>();

    @ManyToOne(targetEntity = MerchantStore.class, optional = false)
    @JoinColumn(name = "STORE_ID")
    protected MerchantStore store;

    @OneToMany(cascade = { CascadeType.ALL },mappedBy = "receiptPrinted", targetEntity = ReceiptPrintedItem.class, fetch = FetchType.LAZY)
    protected List<ReceiptPrintedItem> items = new ArrayList<ReceiptPrintedItem>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(String receiptType) {
        this.receiptType = receiptType;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<ProductUsed> getProductUsed() {
        return productUsed;
    }

    public void setProductUsed(List<ProductUsed> productUsed) {
        this.productUsed = productUsed;
    }

    public MerchantStore getStore() {
        return store;
    }

    public void setStore(MerchantStore store) {
        this.store = store;
    }

    public List<ReceiptPrintedItem> getItems() {
        return items;
    }

    public void setItems(List<ReceiptPrintedItem> items) {
        this.items = items;
    }
}
