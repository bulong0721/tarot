package com.myee.tarot.merchant.domain;

import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.profile.domain.Address;
import com.myee.tarot.reference.domain.GeoZone;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

/**
 * Created by Martin on 2016/4/11.
 */
@Entity
@Table(name = "C_MERCHANT_STORE")
public class MerchantStore extends GenericEntity<Long, MerchantStore> {
    public static final String DEFAULT_STORE = "DEFAULT";

    @Id
    @Column(name = "STORE_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "STORE_SEQ_NEXT_VAL")
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

    @Column(name = "STORE_CPP")
    private Integer cpp;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "EXPERIENCE")
    private boolean experience = false;

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
}
