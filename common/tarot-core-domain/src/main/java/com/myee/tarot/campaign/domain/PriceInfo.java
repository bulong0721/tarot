package com.myee.tarot.campaign.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Administrator on 2016/7/11.
 */
@Entity
@Table(name = "P_PRICE_INFO")
public class PriceInfo extends GenericEntity<Long, PriceInfo>{
    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PRICE_INFO_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @Column(name = "KEY_ID")
    private String keyId; //微信关联ID

    @Column(name = "STATUS")
    private Integer status; // 0.已使用 1.未使用 2.过期

    @Column(name = "CHECK_CODE")
    private String checkCode; //验证码

    @ManyToOne(targetEntity = MerchantPrice.class, optional = false, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "PRICE_ID")
    private MerchantPrice price;  //获取对应的奖项

    //快照对应的当时的奖券信息 不因关联
    @Column(name = "PRICE_NAME")
    private String priceName;

    @Column(name = "PRICE_DESCRIPTION")
    private String priceDescription;

    @Column(name = "PRICE_LOGO")
    private String priceLogo;

    @Column(name = "PRICE_START_DATE")
    @JSONField(format = "yyyy-MM-dd")
    private Date priceStartDate;

    @Column(name = "PRICE_END_DATE")
    @JSONField(format = "yyyy-MM-dd")
    private Date priceEndDate;


    @Column(name = "CHECK_DATE")
    @JSONField(format = "yyyy-MM-dd")
    private Date checkDate; //扫描使用时间

    @Column(name = "GET_DATE")
    @JSONField(format = "yyyy-MM-dd")
    private Date getDate; //获取该奖券的时间

    public String getPriceLogo() {
        return priceLogo;
    }

    public void setPriceLogo(String priceLogo) {
        this.priceLogo = priceLogo;
    }

    public String getPriceName() {
        return priceName;
    }

    public void setPriceName(String priceName) {
        this.priceName = priceName;
    }

    public String getPriceDescription() {
        return priceDescription;
    }

    public void setPriceDescription(String priceDescription) {
        this.priceDescription = priceDescription;
    }

    public Date getPriceStartDate() {
        return priceStartDate;
    }

    public void setPriceStartDate(Date priceStartDate) {
        this.priceStartDate = priceStartDate;
    }

    public Date getPriceEndDate() {
        return priceEndDate;
    }

    public void setPriceEndDate(Date priceEndDate) {
        this.priceEndDate = priceEndDate;
    }

    public Date getGetDate() {
        return getDate;
    }

    public void setGetDate(Date getDate) {
        this.getDate = getDate;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public MerchantPrice getPrice() {
        return price;
    }

    public void setPrice(MerchantPrice price) {
        this.price = price;
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
