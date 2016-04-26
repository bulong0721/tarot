package com.myee.tarot.admin.web.handler;

import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.admin.service.AdminUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Martin on 2016/4/18.
 */
public class AdminAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminAuthenticationSuccessHandler.class);

    @Autowired
    private AdminUserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // last access timestamp
        String userName = authentication.getName();
        try {
//            AdminUser user = userService.getByUserName(userName);
//
//            Date lastAccess = user.getLoginTime();
//            if (lastAccess == null) {
//                lastAccess = new Date();
//            }
//            user.setLastAccess(lastAccess);
//            user.setLoginTime(new Date());
//
//            userService.saveOrUpdate(user);
            response.sendRedirect(request.getContextPath() + "/admin/home.html");
        } catch (Exception e) {
            LOGGER.error("User authenticationSuccess", e);
        }
    }

}
