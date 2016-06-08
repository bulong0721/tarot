package com.myee.tarot.reference.domain;

import com.myee.tarot.core.GenericEntity;

import javax.persistence.*;

/**
 * Created by Martin on 2016/4/14.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "C_PHONE")
public class Phone extends GenericEntity<Long, Phone> {
    @Id
    @Column(name = "PHONE_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "PHONE_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @Column(name = "COUNTRY_CODE")
    protected String countryCode;

    @Column(name = "PHONE_NUMBER", nullable=false)
    protected String phoneNumber;

    @Column(name = "EXTENSION")
    protected String extension;

    @Column(name = "IS_DEFAULT")
    protected boolean isDefault = false;

    @Column(name = "IS_ACTIVE")
    protected boolean isActive = true;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Phone phone = (Phone) o;

        if (isDefault != phone.isDefault) return false;
        if (isActive != phone.isActive) return false;
        if (countryCode != null ? !countryCode.equals(phone.countryCode) : phone.countryCode != null) return false;
        if (phoneNumber != null ? !phoneNumber.equals(phone.phoneNumber) : phone.phoneNumber != null) return false;
        return !(extension != null ? !extension.equals(phone.extension) : phone.extension != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (countryCode != null ? countryCode.hashCode() : 0);
        result = 31 * result + (phoneNumber != null ? phoneNumber.hashCode() : 0);
        result = 31 * result + (extension != null ? extension.hashCode() : 0);
        result = 31 * result + (isDefault ? 1 : 0);
        result = 31 * result + (isActive ? 1 : 0);
        return result;
    }
}
