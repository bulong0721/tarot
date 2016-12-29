package com.myee.tarot.admin.service;

import com.myee.tarot.admin.domain.AdminPermission;
import com.myee.tarot.admin.domain.AdminRole;
import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.admin.util.PermissionUtil;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.service.TransactionalAspectAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Jeff Fischer
 */
public class AdminUserDetailsServiceImpl implements UserDetailsService, TransactionalAspectAware {
    static final String[] DEFAULT_PERMISSIONS = {"ROLE_AUTH_ADMIN", "ROLE_AUTH_CUSTOMER"};

    @Autowired
    protected AdminUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        AdminUser adminUser = userService.getByLogin(username);
        if (adminUser == null || adminUser.getActiveStatusFlag() == null || !adminUser.getActiveStatusFlag()) {
            throw new UsernameNotFoundException("The user was not found");
        }

        Set<GrantedAuthority> authorities =  new HashSet<GrantedAuthority>();
        authorities = PermissionUtil.listAuthorities(adminUser,authorities);
        /*List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (AdminRole role : adminUser.getAllRoles()) {
            for (AdminPermission permission : role.getAllPermissions()) {
                //角色配置权限的时候，只关联最底层权限，所以关联的权限保证是没有子节点的
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
//                if (permission.isFriendly()) {
//                    for (AdminPermission childPermission : permission.getAllChildPermissions()) {
//                        authorities.add(new SimpleGrantedAuthority(childPermission.getName()));
//                    }
//                } else {
//                    authorities.add(new SimpleGrantedAuthority(permission.getName()));
//                }
            }
        }
        for (AdminPermission permission : adminUser.getAllPermissions()) {
            //配置用户权限的时候，只关联最底层权限，所以关联的权限保证是没有子节点的
            authorities.add(new SimpleGrantedAuthority(permission.getName()));
//            if (permission.isFriendly()) {
//                for (AdminPermission childPermission : permission.getAllChildPermissions()) {
//                    authorities.add(new SimpleGrantedAuthority(childPermission.getName()));
//                }
//            } else {
//                authorities.add(new SimpleGrantedAuthority(permission.getName()));
//            }
        }*/

        for (String perm : DEFAULT_PERMISSIONS) {
            authorities.add(new SimpleGrantedAuthority(perm));
        }
        return new AdminUserDetails(adminUser.getId(), username, adminUser.getPassword(), true, true, true, true, authorities);
    }

}
