package com.myee.tarot.profile.domain;

import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.reference.domain.Address;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.MapKeyType;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Martin on 2016/4/14.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "C_CUSTOMER_PAYMENT")
public class CustomerPayment extends GenericEntity<Long, CustomerPayment> {
    @Id
    @Column(name = "CUSTOMER_PAYMENT_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "CUSTOMER_PAYMENT_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = Customer.class, optional = false)
    @JoinColumn(name = "CUSTOMER_ID")
    protected Customer customer;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = Address.class, optional = true)
    @JoinColumn(name = "ADDRESS_ID")
    protected Address billingAddress;

    @Column(name = "PAYMENT_TOKEN")
    protected String paymentToken;

    @Column(name = "IS_DEFAULT")
    protected boolean isDefault = false;

    @ElementCollection
    @MapKeyType(@Type(type = "java.lang.String"))
    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    @CollectionTable(name = "C_CUSTOMER_PAYMENT_FIELDS", joinColumns = @JoinColumn(name = "CUSTOMER_PAYMENT_ID"))
    @MapKeyColumn(name = "FIELD_NAME", nullable = false)
    @Column(name = "FIELD_VALUE")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    protected Map<String, String> additionalFields = new HashMap<String, String>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, String> getAdditionalFields() {
        return additionalFields;
    }

    public void setAdditionalFields(Map<String, String> additionalFields) {
        this.additionalFields = additionalFields;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    public String getPaymentToken() {
        return paymentToken;
    }

    public void setPaymentToken(String paymentToken) {
        this.paymentToken = paymentToken;
    }
}
