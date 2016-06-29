package com.myee.tarot.web.filter;

import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.admin.service.AdminUserService;
import com.myee.tarot.core.Constants;
import com.myee.tarot.merchant.domain.Merchant;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.merchant.service.MerchantService;
import com.myee.tarot.merchant.service.MerchantStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Martin on 2016/4/20.
 */
public class AdminFilter extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminFilter.class);

    @Autowired
    private MerchantStoreService merchantStoreService;
    @Autowired
    private MerchantService merchantService;

    @Autowired
    private AdminUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AdminUser user = (AdminUser) request.getSession().getAttribute(Constants.ADMIN_USER);
//        System.out.println("################userSession:"+user.getName());

        String storeCode = MerchantStore.DEFAULT_STORE;
        MerchantStore store = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);


        String userName = request.getRemoteUser();

        if (userName == null) {//** IMPORTANT FOR SPRING SECURITY **//
            //response.sendRedirect(new StringBuilder().append(request.getContextPath()).append("/").append("/admin").toString());
        } else {

            if (user == null) {
                user = userService.getByLogin(userName);
                request.getSession().setAttribute(Constants.ADMIN_USER, user);
                System.out.printf("name" + user.getMerchantStore().getName());
                if (user != null) {
//                    storeCode = user.getMerchantStore().getCode();
                } else {
                    LOGGER.warn("User name not found " + userName);
                }
                store = null;
            }

            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/admin/unauthorized.html");
                return true;
            }

            if (!user.getName().equals(userName)) {
                user = userService.getByLogin(userName);
                if (user != null) {
//                    storeCode = user.getMerchantStore().getCode();
                } else {
                    LOGGER.warn("User name not found " + userName);
                }
                store = null;
            }
        }

        if (store == null && user != null) {
//            store = merchantStoreService.getByCode(storeCode);
            String storeName = user.getMerchantStore().getName();
            store = user.getMerchantStore();
            Merchant merchant = merchantService.findById(store.getMerchant().getId());
            request.getSession().setAttribute(Constants.ADMIN_STORE, store);
            request.getSession().setAttribute(Constants.ADMIN_MERCHANT, merchant);
        }
        request.setAttribute(Constants.ADMIN_STORE, store);

        response.setCharacterEncoding("UTF-8");
        return true;
    }
}
