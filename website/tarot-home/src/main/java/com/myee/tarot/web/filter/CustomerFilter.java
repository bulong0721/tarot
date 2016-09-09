package com.myee.tarot.web.filter;

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
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
                user = customerService.getByUsername(userName);
                SessionUtil.setSessionAttribute(Constants.CUSTOMER_USER, user, request);
            }

            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/shop/unauthorized.html");
                return true;
            }

            if (!user.getUsername().equals(userName)) {
                user = customerService.getByUsername(userName);
                SessionUtil.setSessionAttribute(Constants.CUSTOMER_USER, user, request);
            }
        }

        if (store == null && user != null) {
//            store = merchantStoreService.getByCode(storeCode);
            store = user.getMerchantStore();
            Merchant merchant = merchantService.findById(store.getMerchant().getId());
            request.getSession().setAttribute(Constants.CUSTOMER_STORE, store);
            request.getSession().setAttribute(Constants.CUSTOMER_MERCHANT , merchant);
        }
        request.setAttribute(Constants.CUSTOMER_STORE, store);
        response.setCharacterEncoding("UTF-8");
        return true;
    }
}
