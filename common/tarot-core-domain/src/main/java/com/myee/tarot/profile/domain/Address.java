package com.myee.tarot.profile.domain;

import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.reference.domain.ISOCountry;

import javax.persistence.*;

/**
 * Created by Martin on 2016/4/14.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "C_ADDRESS")
public class Address extends GenericEntity<Long, Address> {
    @Id
    @Column(name = "ADDRESS_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "ADDRESS_SEQ_NEXT_VAL")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @Column(name = "FULL_NAME")
    protected String fullName;

    @Column(name = "FIRST_NAME")
    protected String firstName;

    @Column(name = "LAST_NAME")
    protected String lastName;

    @Column(name = "EMAIL_ADDRESS")
    protected String emailAddress;

    @Column(name = "COMPANY_NAME")
    protected String companyName;

    @Column(name = "ADDRESS_LINE1", nullable = false)
    protected String addressLine1;

    @Column(name = "ADDRESS_LINE2")
    protected String addressLine2;

    @Column(name = "ADDRESS_LINE3")
    protected String addressLine3;

    @Column(name = "CITY", nullable = false)
    protected String city;

    @Column(name = "ISO_COUNTRY_SUB")
    protected String isoCountrySubdivision;

    @Column(name = "SUB_STATE_PROV_REG")
    protected String stateProvinceRegion;

    @Column(name = "COUNTY")
    protected String county;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = Country.class)
    @JoinColumn(name = "COUNTRY")
    protected Country country;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = ISOCountry.class)
    @JoinColumn(name = "ISO_COUNTRY_ALPHA2")
    protected ISOCountry isoCountryAlpha2;

    @Column(name = "POSTAL_CODE")
    protected String postalCode;

    @Column(name = "ZIP_FOUR")
    protected String zipFour;

    @ManyToOne(targetEntity = Phone.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "PHONE_PRIMARY_ID")
    protected Phone phonePrimary;

    @ManyToOne(targetEntity = Phone.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "PHONE_SECONDARY_ID")
    protected Phone phoneSecondary;

    @ManyToOne(targetEntity = Phone.class, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "PHONE_FAX_ID")
    protected Phone phoneFax;

    @Column(name = "IS_DEFAULT")
    protected boolean isDefault = false;

    @Column(name = "IS_ACTIVE")
    protected boolean isActive = true;

    @Column(name = "IS_BUSINESS")
    protected boolean isBusiness = false;

    @Column(name = "IS_STREET")
    protected boolean isStreet = false;

    @Column(name = "IS_MAILING")
    protected boolean isMailing = false;

    @Column(name = "TOKENIZED_ADDRESS")
    protected String tokenizedAddress;

    @Column(name = "STANDARDIZED")
    protected boolean standardized = false;

    @Column(name = "VERIFICATION_LEVEL")
    protected String verificationLevel;

    @Column(name = "PRIMARY_PHONE")
    @Deprecated
    protected String primaryPhone;

    @Column(name = "SECONDARY_PHONE")
    @Deprecated
    protected String secondaryPhone;

    @Column(name = "FAX")
    @Deprecated
    protected String fax;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isBusiness() {
        return isBusiness;
    }

    public void setIsBusiness(boolean isBusiness) {
        this.isBusiness = isBusiness;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public boolean isMailing() {
        return isMailing;
    }

    public void setIsMailing(boolean isMailing) {
        this.isMailing = isMailing;
    }

    public ISOCountry getIsoCountryAlpha2() {
        return isoCountryAlpha2;
    }

    public void setIsoCountryAlpha2(ISOCountry isoCountryAlpha2) {
        this.isoCountryAlpha2 = isoCountryAlpha2;
    }

    public String getIsoCountrySubdivision() {
        return isoCountrySubdivision;
    }

    public void setIsoCountrySubdivision(String isoCountrySubdivision) {
        this.isoCountrySubdivision = isoCountrySubdivision;
    }

    public boolean isStreet() {
        return isStreet;
    }

    public void setIsStreet(boolean isStreet) {
        this.isStreet = isStreet;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Phone getPhoneFax() {
        return phoneFax;
    }

    public void setPhoneFax(Phone phoneFax) {
        this.phoneFax = phoneFax;
    }

    public Phone getPhonePrimary() {
        return phonePrimary;
    }

    public void setPhonePrimary(Phone phonePrimary) {
        this.phonePrimary = phonePrimary;
    }

    public Phone getPhoneSecondary() {
        return phoneSecondary;
    }

    public void setPhoneSecondary(Phone phoneSecondary) {
        this.phoneSecondary = phoneSecondary;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public String getSecondaryPhone() {
        return secondaryPhone;
    }

    public void setSecondaryPhone(String secondaryPhone) {
        this.secondaryPhone = secondaryPhone;
    }

    public boolean isStandardized() {
        return standardized;
    }

    public void setStandardized(boolean standardized) {
        this.standardized = standardized;
    }

    public String getStateProvinceRegion() {
        return stateProvinceRegion;
    }

    public void setStateProvinceRegion(String stateProvinceRegion) {
        this.stateProvinceRegion = stateProvinceRegion;
    }

    public String getTokenizedAddress() {
        return tokenizedAddress;
    }

    public void setTokenizedAddress(String tokenizedAddress) {
        this.tokenizedAddress = tokenizedAddress;
    }

    public String getVerificationLevel() {
        return verificationLevel;
    }

    public void setVerificationLevel(String verificationLevel) {
        this.verificationLevel = verificationLevel;
    }

    public String getZipFour() {
        return zipFour;
    }

    public void setZipFour(String zipFour) {
        this.zipFour = zipFour;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Address address = (Address) o;

        if (fullName != null ? !fullName.equals(address.fullName) : address.fullName != null) return false;
        if (firstName != null ? !firstName.equals(address.firstName) : address.firstName != null) return false;
        if (lastName != null ? !lastName.equals(address.lastName) : address.lastName != null) return false;
        if (companyName != null ? !companyName.equals(address.companyName) : address.companyName != null) return false;
        if (addressLine1 != null ? !addressLine1.equals(address.addressLine1) : address.addressLine1 != null) return false;
        if (addressLine2 != null ? !addressLine2.equals(address.addressLine2) : address.addressLine2 != null) return false;
        if (city != null ? !city.equals(address.city) : address.city != null) return false;
        if (county != null ? !county.equals(address.county) : address.county != null) return false;
        if (country != null ? !country.equals(address.country) : address.country != null) return false;
        if (isoCountryAlpha2 != null ? !isoCountryAlpha2.equals(address.isoCountryAlpha2) : address.isoCountryAlpha2 != null) return false;
        return !(postalCode != null ? !postalCode.equals(address.postalCode) : address.postalCode != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
        result = 31 * result + (firstName != null ? firstName.hashCode() : 0);
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (companyName != null ? companyName.hashCode() : 0);
        result = 31 * result + (addressLine1 != null ? addressLine1.hashCode() : 0);
        result = 31 * result + (addressLine2 != null ? addressLine2.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (county != null ? county.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (isoCountryAlpha2 != null ? isoCountryAlpha2.hashCode() : 0);
        result = 31 * result + (postalCode != null ? postalCode.hashCode() : 0);
        return result;
    }
}
