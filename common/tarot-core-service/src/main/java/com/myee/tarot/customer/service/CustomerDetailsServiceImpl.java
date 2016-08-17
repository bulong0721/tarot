package com.myee.tarot.customer.service;

import com.myee.tarot.core.service.TransactionalAspectAware;
import com.myee.tarot.customer.domain.Customer;
import com.myee.tarot.customer.domain.CustomerRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin on 2016/4/11.
 */
@Service
public class CustomerDetailsServiceImpl implements UserDetailsService, TransactionalAspectAware {
    static final String[] DEFAULT_PERMISSIONS = {"ROLE_AUTH", "ROLE_AUTH_CUSTOMER"};
    @Autowired
    protected CustomerService customerServiceService;

    @Autowired
    protected CustomerRoleService roleService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        Customer customer = customerServiceService.getByUsername(username);
        if (customer == null) {
            throw new UsernameNotFoundException("The customer was not found");
        }

        List<GrantedAuthority> grantedAuthorities = createGrantedAuthorities(roleService.listByCustomerId(customer.getId()));
        return new CustomerUserDetails(customer.getId(), username, customer.getPassword(), !customer.isDeactivated(), true, !customer.isPasswordChangeRequired(), true, grantedAuthorities);
    }

    protected List<GrantedAuthority> createGrantedAuthorities(List<CustomerRole> customerRoles) {
        boolean roleUserFound = false;

        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        for (CustomerRole role : customerRoles) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName()));
            if (role.getRoleName().equals("ROLE_USER")) {
                roleUserFound = true;
            }
        }

        if (!roleUserFound) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        for (String perm : DEFAULT_PERMISSIONS) {
            grantedAuthorities.add(new SimpleGrantedAuthority(perm));
        }

        return grantedAuthorities;
    }

}