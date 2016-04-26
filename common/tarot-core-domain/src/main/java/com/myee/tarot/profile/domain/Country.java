package com.myee.tarot.profile.domain;

import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;

/**
 * Created by Martin on 2016/4/14.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "C_COUNTRY")
public class Country extends GenericEntity<Long, Country> {
    @Id
    @Column(name = "ADDRESS_ID", unique = true, nullable = false)
    protected Long abbreviation;

    @Column(name = "NAME", nullable=false)
    protected String name;

    @Override
    public Long getId() {
        return abbreviation;
    }

    @Override
    public void setId(Long id) {
        this.abbreviation = id;
    }

    public Long getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(Long abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Country country = (Country) o;

        if (abbreviation != null ? !abbreviation.equals(country.abbreviation) : country.abbreviation != null) return false;
        return !(name != null ? !name.equals(country.name) : country.name != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (abbreviation != null ? abbreviation.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
