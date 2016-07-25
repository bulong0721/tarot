package com.myee.tarot.merchant.domain;

import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.profile.domain.Address;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * Created by Martin on 2016/4/11.
 */
@Entity
@Table(name = "C_MERCHANT_STORE")
@DynamicUpdate //hibernate部分更新
public class MerchantStore extends GenericEntity<Long, MerchantStore> {
    public static final String DEFAULT_STORE = "DEFAULT";

    @Id
    @Column(name = "STORE_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "STORE_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    private Long id;

    @NotEmpty
    @Column(name = "NAME", length = 60)
    private String name;

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9_]*$")
    @Column(name = "STORE_CODE", nullable = false, unique = true, length = 100)
    private String code;

    @Column(name = "PHONE", length = 20)
    private String phone;

    @Column(name = "POSTAL_CODE", length = 20)
    private String postalCode;

    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = Address.class)
    @JoinColumn(name = "STORE_ADDRESS")
    private Address address;

    @Column(name = "RATINGS")
    private Float ratings;

    //人均消费
    @Column(name = "STORE_CPP")
    private Integer cpp;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "EXPERIENCE")
    private boolean experience = false;

    @Column(name = "STORE_TYPE", length = 20)
    private String storeType;

    @Column(name = "TIME_OPEN")
    private Date timeOpen;
    @Column(name = "TIME_CLOSE")
    private Date   timeClose;

    @Column(name = "SCORE")
    private Float  score;

    //关联join的门店详细信息
    @ManyToOne(targetEntity = Merchant.class, optional = false, cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "MERCHANT_ID",referencedColumnName = "MERCHANT_ID")
    protected Merchant merchant;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Integer getCpp() {
        return cpp;
    }

    public void setCpp(Integer cpp) {
        this.cpp = cpp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isExperience() {
        return experience;
    }

    public void setExperience(boolean experience) {
        this.experience = experience;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Float getRatings() {
        return ratings;
    }

    public void setRatings(Float ratings) {
        this.ratings = ratings;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public Date getTimeOpen() {
        return timeOpen;
    }

    public void setTimeOpen(Date timeOpen) {
        this.timeOpen = timeOpen;
    }

    public Date getTimeClose() {
        return timeClose;
    }

    public void setTimeClose(Date timeClose) {
        this.timeClose = timeClose;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

}
