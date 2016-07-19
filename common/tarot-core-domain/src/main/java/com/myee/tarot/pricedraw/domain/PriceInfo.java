package com.myee.tarot.pricedraw.domain;

import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/7/11.
 */
@Entity
@Table(name = "P_PRICE_INFO")
public class PriceInfo extends GenericEntity<Long, PriceInfo>{
    @Id
    @Column(name = "ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "CUSTOMER_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @Column(name = "KEY_ID")
    private Long keyId; //微信关联ID

    @Column(name = "STATUS")
    private Integer status; // 0.已使用 1.未使用 2.过期



    @ManyToOne(targetEntity = MerchantPrice.class, optional = false, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "PRICE_ID")
    private MerchantPrice price;  //获取对应的奖项

    public Long getKeyId() {
        return keyId;
    }

    public void setKeyId(Long keyId) {
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
