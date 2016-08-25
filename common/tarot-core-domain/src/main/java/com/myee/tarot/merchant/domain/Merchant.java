package com.myee.tarot.merchant.domain;

import com.myee.tarot.core.GenericEntity;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

/**
 * Created by Martin on 2016/4/11.
 */

@Entity
@Table(name = "C_MERCHANT")
@DynamicUpdate //hibernate部分更新
public class Merchant extends GenericEntity<Long, Merchant> {

    @Id
    @Column(name = "MERCHANT_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "MERCHANT_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @NotEmpty
    @Column(name = "NAME", length=60)
    private String name;

    @Column(name = "LOGO_URL")
    private String logo;

    @NotEmpty
    @Column(name = "BUSINESS_TYPE", length = 10)
    private String businessType;

    @Column(name = "CUISINE_TYPE", length = 10)
    private String cuisineType;

    @Column(name = "DESCRIPTION")
    private String description;

//    @OneToMany(mappedBy = "merchant", targetEntity = MerchantStore.class, cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
//    @MapKey(name = "name")
//    protected Map<String, MerchantStore> merchantStore = new HashMap<String, MerchantStore>();

//    List<MerchantStore>

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public Map<String, MerchantStore> getMerchantStore() {
//        return merchantStore;
//    }

}
