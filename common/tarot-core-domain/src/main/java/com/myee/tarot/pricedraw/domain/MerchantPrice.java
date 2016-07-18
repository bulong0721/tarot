package com.myee.tarot.pricedraw.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.myee.tarot.catalog.domain.ProductUsedAttribute;
import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.Merchant;
import com.myee.tarot.merchant.domain.MerchantStore;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2016/7/11.
 */
@Entity
@Table(name = "P_MERCHANT_PRICE")
public class MerchantPrice extends GenericEntity<Long, MerchantPrice>{
    @Id
    @Column(name = "PRICE_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "CUSTOMER_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @Column(name= "LEVEL")
    private Integer level; //奖券等级

    @Column(name= "NAME")
    private String name; //奖券名称

    @Column(name= "DESCRIPTION")
    private String description; //奖券描述

    @ManyToOne(targetEntity = MerchantStore.class)
    @JoinColumn(name = "STORE_ID")
    private MerchantStore store; //奖券使用门店

    @Column(name= "START_DATE")
    private Date startDate; //奖券有效开始时间

    @Column(name= "END_DATE")
    private Date endDate; //奖券有效结束日期

    @ManyToOne(targetEntity = MerchantActivity.class, optional = false, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name= "ACTIVITY_ID")
    @JSONField(serialize = false)
    private MerchantActivity activity; //关联活动

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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public MerchantStore getStore() {
        return store;
    }

    public void setStore(MerchantStore store) {
        this.store = store;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public MerchantActivity getActivity() {
        return activity;
    }

    public void setActivity(MerchantActivity activity) {
        this.activity = activity;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
}
