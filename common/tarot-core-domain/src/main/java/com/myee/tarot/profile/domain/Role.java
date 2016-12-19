package com.myee.tarot.profile.domain;

import com.myee.tarot.admin.domain.AdminPermission;
import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.core.GenericEntity;
import com.myee.tarot.customer.domain.Customer;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Martin on 2016/4/14.
 */
@Entity
@Table(name = "C_ROLE")
public class Role extends GenericEntity<Long, Role> {
    @Id
    @Column(name = "ROLE_ID", unique = true, nullable = false)
    @TableGenerator(name = "TABLE_GEN", table = "C_SEQUENCER", pkColumnName = "SEQ_NAME", valueColumnName = "SEQ_COUNT", pkColumnValue = "ROLE_SEQ_NEXT_VAL", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "TABLE_GEN")
    protected Long id;

    @Column(name = "ROLE_NAME", length = 20, unique = true, nullable = false)
    protected String roleName;

    @Column(name = "DESCRIPTION", length = 255)
    protected String description;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = AdminPermission.class)
    @JoinTable(name = "C_CUSTOMER_ROLE_PERMISSION_XREF", joinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID"), inverseJoinColumns = @JoinColumn(name = "ADMIN_PERMISSION_ID", referencedColumnName = "ADMIN_PERMISSION_ID"))
    protected Set<AdminPermission> allPermissions = new HashSet<AdminPermission>();

    /** All users that have this role */
    @ManyToMany(fetch = FetchType.LAZY, targetEntity = Customer.class)
    @JoinTable(name = "C_CUSTOMER_ROLE_XREF", joinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ROLE_ID"), inverseJoinColumns = @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID"))
    protected Set<Customer> allCustomers = new HashSet<Customer>();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Role role = (Role) o;

        return !(roleName != null ? !roleName.equals(role.roleName) : role.roleName != null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (roleName != null ? roleName.hashCode() : 0);
        return result;
    }

    public Set<AdminPermission> getAllPermissions() {
        return allPermissions;
    }

    public void setAllPermissions(Set<AdminPermission> allPermissions) {
        this.allPermissions = allPermissions;
    }

    public Set<Customer> getAllCustomers() {
        return allCustomers;
    }

    public void setAllCustomers(Set<Customer> allCustomers) {
        this.allCustomers = allCustomers;
    }
}
