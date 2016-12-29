package com.myee.tarot.admin.util;

import com.myee.tarot.admin.domain.AdminPermission;
import com.myee.tarot.admin.domain.AdminRole;
import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.customer.domain.Customer;
import com.myee.tarot.profile.domain.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by chay on 2016/12/19.
 */
public class PermissionUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionUtil.class);

    public static Set<GrantedAuthority> listAuthorities(AdminUser adminUser,Set<GrantedAuthority> authorities) {
        Set<AdminRole> adminRoles = adminUser.getAllRoles();
        Set<AdminPermission> adminPermissions = adminUser.getAllPermissions();
        if( adminRoles != null && adminRoles.size() > 0 ) {
            for (AdminRole role : adminRoles) {
                Set<AdminPermission> allPermissions = role.getAllPermissions();
                if(allPermissions == null || allPermissions.size() == 0){
                    continue;
                }
                for (AdminPermission permission : role.getAllPermissions()) {
                    //角色配置权限的时候，只关联最底层权限，所以关联的权限保证是没有子节点的
                    authorities.add(new SimpleGrantedAuthority(permission.getName()));
                }
            }
        }
        if( adminPermissions != null && adminPermissions.size() > 0 ) {
            for (AdminPermission permission : adminPermissions) {
                //配置用户权限的时候，只关联最底层权限，所以关联的权限保证是没有子节点的
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            }
        }
        return authorities;
    }

    public static Set<GrantedAuthority> listAuthorities(Customer customer,Set<GrantedAuthority> authorities) {
        Set<Role> customerAllRoles = customer.getAllRoles();
        Set<AdminPermission> customerAllPermissions = customer.getAllPermissions();
        if ( customerAllRoles != null && customerAllRoles.size() > 0 ) {
            for (Role role : customerAllRoles) {
                Set<AdminPermission> allPermissions = role.getAllPermissions();
                if(allPermissions == null || allPermissions.size() == 0){
                    continue;
                }
                //角色配置权限的时候，只关联最底层权限，所以关联的权限保证是没有子节点的
                for( AdminPermission permission : allPermissions ){
                    authorities.add(new SimpleGrantedAuthority(permission.getName()));
                }
            }
        }
        if ( customerAllPermissions != null && customerAllPermissions.size() > 0 ) {
            for (AdminPermission permission : customerAllPermissions) {
                //配置用户权限的时候，只关联最底层权限，所以关联的权限保证是没有子节点的
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            }
        }

        return authorities;
    }
}
