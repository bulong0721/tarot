package com.myee.tarot.campaign.domain;

import com.google.common.collect.Lists;
import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Administrator on 2016/7/11.
 */
@Entity
@Table(name = "P_MERCHANT_ACTIVITY")
public class MerchantActivity extends GenericEntity<Long, MerchantActivity>{
    @Id
    @Column(name = "ACTIVITY_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "MERCHANT_ACTIVITY_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @Column(name = "TITLE")
    private String title; //活动标题

    @Column(name= "CONTENT")
    private String content; //活动内容

    @ManyToOne(targetEntity = MerchantStore.class, optional = false)
    @JoinColumn(name = "STORE_ID",unique = true)
    private MerchantStore store; //发起活动商店 只能存在一个活动

    @OneToMany(targetEntity = MerchantPrice.class, mappedBy = "activity",cascade = {CascadeType.ALL},fetch = FetchType.LAZY)
    @Where(clause="DELETE_STATUS=0")
    private List<MerchantPrice> prices = Lists.newArrayList();

    @Column(name = "deleteStatus")
    private int deleteStatus; //是否被删除   0为启用，1为删除

    @Column(name = "ACTIVITY_STATUS")
    private int activityStatus; //是否启用  0为启用 1为停止

    public int getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(int activityStatus) {
        this.activityStatus = activityStatus;
    }

    public int getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(int deleteStatus) {
        this.deleteStatus = deleteStatus;
    }


    public List<MerchantPrice> getPrices() {
        return prices;
    }

    public void setPrices(List<MerchantPrice> prices) {
        this.prices = prices;
    }

    public MerchantStore getStore() {
        return store;
    }

    public void setStore(MerchantStore store) {
        this.store = store;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
