package com.myee.tarot.web.filter;

import com.myee.tarot.admin.util.PermissionUtil;
import com.myee.tarot.core.Constants;
import com.myee.tarot.customer.domain.Customer;
import com.myee.tarot.customer.service.CustomerService;
import com.myee.tarot.merchant.domain.Merchant;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.merchant.service.MerchantService;
import com.myee.tarot.core.web.util.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin on 2016/4/20.
 */
public class CustomerFilter extends HandlerInterceptorAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerFilter.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MerchantService merchantService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Customer user = SessionUtil.getSessionAttribute(Constants.CUSTOMER_USER, request);

        String userName = request.getRemoteUser();
        MerchantStore store = (MerchantStore) request.getSession().getAttribute(Constants.CUSTOMER_STORE);

        if (userName == null) {

        } else {
            if (user == null) {
                user = putUserSession(user,userName,request);
            }

            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/shop/unauthorized.html");
                return true;
            }

            if (!user.getUsername().equals(userName)) {
                user = putUserSession(user,userName,request);
            }
        }

        if (store == null && user != null) {
//            store = merchantStoreService.getByCode(storeCode);
            store = user.getMerchantStore();
            if(store == null){
                return false;
            }
            Merchant merchant = merchantService.findById(store.getMerchant().getId());
            request.getSession().setAttribute(Constants.CUSTOMER_STORE, store);
            request.getSession().setAttribute(Constants.CUSTOMER_MERCHANT , merchant);
        }
        request.setAttribute(Constants.CUSTOMER_STORE, store);
        response.setCharacterEncoding("UTF-8");
        return true;
    }

    private Customer putUserSession(Customer user,String userName,HttpServletRequest request) {
        user = customerService.getByUsername(userName);
        SessionUtil.setSessionAttribute(Constants.CUSTOMER_USER, user, request);
        user.getMerchantStore().getName();
        if (user != null) {
//                    storeCode = user.getMerchantStore().getCode();
            //将用户所有权限写入session
            List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
            authorities = PermissionUtil.listAuthorities(user, authorities);
            List<String> result = null;
            if(authorities != null && authorities.size() > 0){
                result = new ArrayList<String>();
                for( GrantedAuthority grantedAuthority : authorities ) {
                    result.add(grantedAuthority.getAuthority());
                }
            }

            request.getSession().setAttribute(Constants.RESPONSE_USER_ALL_PERMISSIONS, result);
        } else {
            LOGGER.warn("User name not found " + userName);
        }
        return user;
    }
}
