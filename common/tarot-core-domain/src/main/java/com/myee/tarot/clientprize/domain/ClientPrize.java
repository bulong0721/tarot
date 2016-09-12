package com.myee.tarot.clientprize.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.merchant.domain.MerchantStore;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/29.
 */
@Entity
@Table(name = "C_CLIENT_PRIZE")
public class ClientPrize extends GenericEntity<Long, ClientPrize>{

    @Id
    @Column(name = "PRIZE_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "CLIENT_PRIZE_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @Column(name= "NAME")
    private String name; //奖券名称

    @Column(name= "DESCRIPTION")
    private String description; //奖券描述

    @Column(name= "TYPE")
    private Integer type; //类型   0为手机 1为二维码

    @Column(name= "START_DATE")
    @JSONField(format = "yyyy-MM-dd")
    private Date startDate; //奖券有效开始时间

    @Column(name= "END_DATE")
    @JSONField(format = "yyyy-MM-dd")
    private Date endDate; //奖券有效结束日期

    @Column(name= "TOTAL")
    private Integer total; //奖券数量

    @Column(name = "LEFT_NUM")
    private Integer leftNum; //奖券剩余数量

    @Transient
    private Integer leftNumCache; //暂时存储

    @ManyToOne(targetEntity = MerchantStore.class, optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "STORE_ID")
    @JSONField(serialize = false)
    private MerchantStore store; //发起活动商店

    @Column(name = "SMALL_PIC")
    private String smallPic; //小图

    @Column(name = "BIG_PIC")
    private String bigPic; //大图

    @Column(name = "ACTIVE_STATUS")
    private Boolean activeStatus = Boolean.TRUE; //启用状态

    @Column(name = "DELETE_STATUS")
    private Boolean deleteStatus = Boolean.TRUE;// 逻辑删除

    @Column(name = "PHONE_PRIZE_TYPE")
    private Integer phonePrizeType; //手机中奖的奖券类型 0为实物 1为电影票

    @Transient
    private Long priceGetId;

    public Integer getPhonePrizeType() {
        return phonePrizeType;
    }

    public void setPhonePrizeType(Integer phonePrizeType) {
        this.phonePrizeType = phonePrizeType;
    }

    public Boolean getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(Boolean deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public Long getPriceGetId() {
        return priceGetId;
    }

    public void setPriceGetId(Long priceGetId) {
        this.priceGetId = priceGetId;
    }

    public Integer getLeftNumCache() {
        return leftNumCache;
    }

    public void setLeftNumCache(Integer leftNumCache) {
        this.leftNumCache = leftNumCache;
    }

    public Boolean getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Boolean activeStatus) {
        this.activeStatus = activeStatus;
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

    public Integer getLeftNum() {
        return leftNum;
    }

    public void setLeftNum(Integer leftNum) {
        this.leftNum = leftNum;
    }

    public MerchantStore getStore() {
        return store;
    }

    public void setStore(MerchantStore store) {
        this.store = store;
    }

    public String getSmallPic() {
        return smallPic;
    }

    public void setSmallPic(String smallPic) {
        this.smallPic = smallPic;
    }

    public String getBigPic() {
        return bigPic;
    }

    public void setBigPic(String bigPic) {
        this.bigPic = bigPic;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
