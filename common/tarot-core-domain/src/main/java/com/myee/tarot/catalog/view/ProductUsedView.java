package com.myee.tarot.catalog.view;

import com.myee.tarot.catalog.domain.ProductUsed;
import com.myee.tarot.merchant.domain.MerchantStore;

import java.io.Serializable;


public class ProductUsedView implements Serializable {

    private Long id;

    private String name;

    private String code;

    private Long storeId;

    private String storeName;

    private String type;

    private String description;

    private String productNum;

    public ProductUsedView(){}

    public ProductUsedView(ProductUsed productUsed){
        this.id = productUsed.getId();
        this.name = productUsed.getName();
        this.code = productUsed.getCode();
        this.storeId = productUsed.getStore().getId();
        this.storeName = productUsed.getStore().getName();
        this.type = productUsed.getType();
        this.description = productUsed.getDescription();
        this.productNum = productUsed.getProductNum();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }
}
