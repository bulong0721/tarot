package com.myee.tarot.customer.service;

import com.myee.tarot.admin.domain.AdminPermission;
import com.myee.tarot.admin.util.PermissionUtil;
import com.myee.tarot.core.service.TransactionalAspectAware;
import com.myee.tarot.customer.domain.Customer;
import com.myee.tarot.customer.domain.CustomerRole;
import com.myee.tarot.profile.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Martin on 2016/4/11.
 */
@Service
public class CustomerDetailsServiceImpl implements UserDetailsService, TransactionalAspectAware {
    static final String[] DEFAULT_PERMISSIONS = {"ROLE_AUTH_CUSTOMER"};
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

//        List<GrantedAuthority> grantedAuthorities = createGrantedAuthorities(roleService.listByCustomerId(customer.getId()));
//        List<GrantedAuthority> grantedAuthorities = createGrantedAuthorities(customer.getAllRoles());
        Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
        grantedAuthorities = PermissionUtil.listAuthorities(customer,grantedAuthorities);

        for (String perm : DEFAULT_PERMISSIONS) {
            grantedAuthorities.add(new SimpleGrantedAuthority(perm));
        }
        return new CustomerUserDetails(customer.getId(), username, customer.getPassword(), !customer.isDeactivated(), true, !customer.isPasswordChangeRequired(), true, grantedAuthorities);
    }

/*//    protected List<GrantedAuthority> createGrantedAuthorities(List<CustomerRole> customerRoles) {
    protected List<GrantedAuthority> createGrantedAuthorities(Set<Role> customerRoles) {
//        boolean roleUserFound = false;

        if(customerRoles == null || customerRoles.size() == 0){
            return Collections.emptyList();
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        for (Role role : customerRoles) {
            Set<AdminPermission> allPermissions = role.getAllPermissions();
            if(allPermissions == null || allPermissions.size() == 0){
                continue;
            }
            //角色配置权限的时候，只关联最底层权限，所以关联的权限保证是没有子节点的
            for( AdminPermission permission : allPermissions ){
                grantedAuthorities.add(new SimpleGrantedAuthority(permission.getName()));
            }
//            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName()));
//            if (role.getRoleName().equals("ROLE_USER")) {
//                roleUserFound = true;
//            }
        }

//        if (!roleUserFound) {
//            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
//        }

        for (String perm : DEFAULT_PERMISSIONS) {
            grantedAuthorities.add(new SimpleGrantedAuthority(perm));
        }

        return grantedAuthorities;
    }*/

}