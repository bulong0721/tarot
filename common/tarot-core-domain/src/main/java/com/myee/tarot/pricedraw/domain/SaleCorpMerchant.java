package com.myee.tarot.pricedraw.domain;

import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/7/11.
 */
@Entity
@Table(name = "P_SALE_CORP")
public class SaleCorpMerchant extends GenericEntity<Long, SaleCorpMerchant>{
    @Id
    @Column(name = "SALE_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "CUSTOMER_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @Column(name= "MERCHANT_ID")
    private Long merchantId;

    @Column(name= "RELATED_IDS")
    private String relatedMerchants;

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getRelatedMerchants() {
        return relatedMerchants;
    }

    public void setRelatedMerchants(String relatedMerchants) {
        this.relatedMerchants = relatedMerchants;
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
