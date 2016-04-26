package com.myee.tarot.profile.domain;

import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;

/**
 * Created by Martin on 2016/4/14.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "C_COUNTRY_SUB")
public class CountrySubdivision extends GenericEntity<String, CountrySubdivision> {

    @Id
    @Column(name = "ABBREVIATION")
    protected String abbreviation;

    @Column(name = "NAME", nullable = false)
    protected String name;

    @Column(name = "ALT_ABBREVIATION")
    protected String alternateAbbreviation;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = Country.class, optional = false)
    @JoinColumn(name = "COUNTRY")
    protected Country country;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = CountrySubdivisionCategory.class)
    @JoinColumn(name = "COUNTRY_SUB_CAT")
    protected CountrySubdivisionCategory category;

    @Override
    public String getId() {
        return abbreviation;
    }

    @Override
    public void setId(String id) {
        this.abbreviation = id;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getAlternateAbbreviation() {
        return alternateAbbreviation;
    }

    public void setAlternateAbbreviation(String alternateAbbreviation) {
        this.alternateAbbreviation = alternateAbbreviation;
    }

    public CountrySubdivisionCategory getCategory() {
        return category;
    }

    public void setCategory(CountrySubdivisionCategory category) {
        this.category = category;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
