package com.myee.tarot.i18n.domain;

import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;

/**
 * Created by Martin on 2016/4/14.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "C_ISO_COUNTRY")
public class ISOCountry extends GenericEntity<String, ISOCountry> {
    @Id
    @Column(name = "ALPHA_2")
    protected String alpha2;

    @Override
    public String getId() {
        return alpha2;
    }

    @Override
    public void setId(String id) {
        this.alpha2 = id;
    }

    public String getAlpha2() {
        return alpha2;
    }

    public void setAlpha2(String alpha2) {
        this.alpha2 = alpha2;
    }

    @Column(name = "NAME")
    protected String name;

    @Column(name = "ALPHA_3")
    protected String alpha3;

    @Column(name = "NUMERIC_CODE")
    protected Integer numericCode;

    @Column(name = "STATUS")
    protected String status;

    public String getAlpha3() {
        return alpha3;
    }

    public void setAlpha3(String alpha3) {
        this.alpha3 = alpha3;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumericCode() {
        return numericCode;
    }

    public void setNumericCode(Integer numericCode) {
        this.numericCode = numericCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
