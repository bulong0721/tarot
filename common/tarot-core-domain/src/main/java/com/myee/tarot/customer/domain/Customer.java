package com.myee.tarot.customer.domain;

import com.myee.tarot.admin.domain.AdminPermission;
import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.core.audit.Auditable;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.profile.domain.Locale;
import com.myee.tarot.profile.domain.Role;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.*;

/**
 * Created by Martin on 2016/4/14.
 */
@Entity
@Table(name = "C_CUSTOMER")
public class Customer extends GenericEntity<Long, Customer> {
    @Id
    @Column(name = "CUSTOMER_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "CUSTOMER_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @Column(name = "USER_NAME",unique = true, length=40)
    protected String username;

    @Column(name = "PASSWORD", length=60)
    protected String password;

    @Column(name = "EMAIL_ADDRESS", length=60)
    protected String emailAddress;

    @Column(name = "FIRST_NAME", length=20)
    protected String firstName;

    @Column(name = "LAST_NAME", length=20)
    protected String lastName;

    @ManyToOne(fetch = FetchType.LAZY)//懒加载会使用户登录时得到的用户信息中不包含门店，就没办法设置session默认门店__需要调用其中的属性才会加载
    @JoinColumn(name = "STORE_ID")
    private MerchantStore merchantStore;

    @ManyToOne(targetEntity = ChallengeQuestion.class)
    @JoinColumn(name = "CHALLENGE_QUESTION_ID")
    protected ChallengeQuestion challengeQuestion;

    @Column(name = "CHALLENGE_ANSWER")
    protected String challengeAnswer;

    @Basic
    @Column(name = "PASSWORD_CHANGE_REQUIRED")
    protected boolean passwordChangeRequired = false;

    @Basic
    @Column(name = "RECEIVE_EMAIL")
    protected boolean receiveEmail = true;

    @Basic
    @Column(name = "IS_REGISTERED")
    protected boolean registered = false;

    @Basic
    @Column(name = "DEACTIVATED")
    protected boolean deactivated = false;

    @ManyToOne(targetEntity = Locale.class)
    @JoinColumn(name = "LOCALE_CODE")
    protected Locale customerLocale;

    @OneToMany(mappedBy = "customer", targetEntity = CustomerAttribute.class, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @MapKey(name = "name")
    protected Map<String, CustomerAttribute> customerAttributes = new HashMap<String, CustomerAttribute>();

    @OneToMany(mappedBy = "customer", targetEntity = CustomerAddress.class, cascade = {CascadeType.ALL})
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
//    @Where(clause = "archived != 'Y'")
    protected List<CustomerAddress> customerAddresses = new ArrayList<CustomerAddress>();

    @OneToMany(mappedBy = "customer", targetEntity = CustomerPhone.class, cascade = {CascadeType.ALL})
    @Cascade(value = {org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    protected List<CustomerPhone> customerPhones = new ArrayList<CustomerPhone>();

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = MerchantStore.class)
    @JoinTable(name = "C_CUSTOMER_MERCHANT_STORE_XREF", joinColumns = @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID"), inverseJoinColumns = @JoinColumn(name = "STORE_ID", referencedColumnName = "STORE_ID"))
    protected Set<MerchantStore> allMerchantStores = new HashSet<MerchantStore>();

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = Role.class)
    @JoinTable(name = "C_CUSTOMER_ROLE_XREF", joinColumns = @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID"))
    protected Set<Role> allRoles = new HashSet<Role>();

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = AdminPermission.class)
    @JoinTable(name = "C_CUSTOMER_PERMISSION_XREF", joinColumns = @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID"), inverseJoinColumns = @JoinColumn(name = "ADMIN_PERMISSION_ID", referencedColumnName = "ADMIN_PERMISSION_ID"))
    protected Set<AdminPermission> allPermissions = new HashSet<AdminPermission>();

    @Column(name = "TAX_EXEMPTION_CODE")
    protected String taxExemptionCode;

    @Transient
    protected String unencodedPassword;

    @Transient
    protected String unencodedChallengeAnswer;

    @Transient
    protected boolean anonymous;

    @Transient
    protected boolean cookied;

    @Transient
    protected boolean loggedIn;

    @Transient
    protected Map<String, Object> transientProperties = new HashMap<String, Object>();

    @Embedded
    protected Auditable auditable = new Auditable();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public MerchantStore getMerchantStore() {
        return merchantStore;
    }

    public void setMerchantStore(MerchantStore merchantStore) {
        this.merchantStore = merchantStore;
    }

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public Auditable getAuditable() {
        return auditable;
    }

    public void setAuditable(Auditable auditable) {
        this.auditable = auditable;
    }

    public String getChallengeAnswer() {
        return challengeAnswer;
    }

    public void setChallengeAnswer(String challengeAnswer) {
        this.challengeAnswer = challengeAnswer;
    }

    public ChallengeQuestion getChallengeQuestion() {
        return challengeQuestion;
    }

    public void setChallengeQuestion(ChallengeQuestion challengeQuestion) {
        this.challengeQuestion = challengeQuestion;
    }

    public boolean isCookied() {
        return cookied;
    }

    public void setCookied(boolean cookied) {
        this.cookied = cookied;
    }

    public List<CustomerAddress> getCustomerAddresses() {
        return customerAddresses;
    }

    public void setCustomerAddresses(List<CustomerAddress> customerAddresses) {
        this.customerAddresses = customerAddresses;
    }

    public Map<String, CustomerAttribute> getCustomerAttributes() {
        return customerAttributes;
    }

    public void setCustomerAttributes(Map<String, CustomerAttribute> customerAttributes) {
        this.customerAttributes = customerAttributes;
    }

    public Locale getCustomerLocale() {
        return customerLocale;
    }

    public void setCustomerLocale(Locale customerLocale) {
        this.customerLocale = customerLocale;
    }

    public List<CustomerPhone> getCustomerPhones() {
        return customerPhones;
    }

    public void setCustomerPhones(List<CustomerPhone> customerPhones) {
        this.customerPhones = customerPhones;
    }

    public boolean isDeactivated() {
        return deactivated;
    }

    public void setDeactivated(boolean deactivated) {
        this.deactivated = deactivated;
    }

    public boolean isPasswordChangeRequired() {
        return passwordChangeRequired;
    }

    public void setPasswordChangeRequired(boolean passwordChangeRequired) {
        this.passwordChangeRequired = passwordChangeRequired;
    }

    public Set<Role> getAllRoles() {
        return allRoles;
    }

    public void setAllRoles(Set<Role> allRoles) {
        this.allRoles = allRoles;
    }

    public boolean isReceiveEmail() {
        return receiveEmail;
    }

    public void setReceiveEmail(boolean receiveEmail) {
        this.receiveEmail = receiveEmail;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRegistered(Boolean registered) {
        this.registered = registered;
    }

    public String getTaxExemptionCode() {
        return taxExemptionCode;
    }

    public void setTaxExemptionCode(String taxExemptionCode) {
        this.taxExemptionCode = taxExemptionCode;
    }

    public Map<String, Object> getTransientProperties() {
        return transientProperties;
    }

    public void setTransientProperties(Map<String, Object> transientProperties) {
        this.transientProperties = transientProperties;
    }

    public String getUnencodedChallengeAnswer() {
        return unencodedChallengeAnswer;
    }

    public void setUnencodedChallengeAnswer(String unencodedChallengeAnswer) {
        this.unencodedChallengeAnswer = unencodedChallengeAnswer;
    }

    public String getUnencodedPassword() {
        return unencodedPassword;
    }

    public void setUnencodedPassword(String unencodedPassword) {
        this.unencodedPassword = unencodedPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<AdminPermission> getAllPermissions() {
        return allPermissions;
    }

    public void setAllPermissions(Set<AdminPermission> allPermissions) {
        this.allPermissions = allPermissions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Customer customer = (Customer) o;

        return !(username != null ? !username.equals(customer.username) : customer.username != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }

    public Set<MerchantStore> getAllMerchantStores() {
        return allMerchantStores;
    }

    public void setAllMerchantStores(Set<MerchantStore> allMerchantStores) {
        this.allMerchantStores = allMerchantStores;
    }
}
