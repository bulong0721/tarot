package com.myee.tarot.profile.domain;

import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.reference.domain.Role;

import javax.persistence.*;

/**
 * Created by Martin on 2016/4/14.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "C_CUSTOMER_ROLE")
public class CustomerRole extends GenericEntity<Long, CustomerRole> {
    @Id
    @Column(name = "ADDRESS_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "ADDRESS_SEQ_NEXT_VAL",allocationSize=1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @ManyToOne(targetEntity = Customer.class, optional = false)
    @JoinColumn(name = "CUSTOMER_ID")
    protected Customer customer;

    @ManyToOne(targetEntity = Role.class, optional = false)
    @JoinColumn(name = "ROLE_ID")
    protected Role role;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getRoleName() {
        return role.getRoleName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CustomerRole that = (CustomerRole) o;

        if (customer != null ? !customer.equals(that.customer) : that.customer != null) return false;
        return !(role != null ? !role.equals(that.role) : that.role != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (customer != null ? customer.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }
}
