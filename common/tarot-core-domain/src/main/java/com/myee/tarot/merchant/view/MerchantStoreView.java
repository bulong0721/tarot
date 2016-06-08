package com.myee.tarot.merchant.view;

import com.myee.tarot.merchant.domain.MerchantStore;

import java.io.Serializable;


public class MerchantStoreView implements Serializable {

    private Long id;

    private String name;

    public MerchantStoreView(){

    }

    public MerchantStoreView(MerchantStore store){
        this.id = store.getId();
        this.name = store.getName();
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
}
