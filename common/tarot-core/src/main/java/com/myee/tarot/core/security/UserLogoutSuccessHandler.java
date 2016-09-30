package com.myee.tarot.core.security;

import com.myee.tarot.core.Constants;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Enva on 2016/9/20.
 */
public class UserLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        request.getSession().removeAttribute(Constants.SESSION_SECURITY_CODE);
        String path = request.getServletPath();
        if (path.contains("/admin/")) {
            response.sendRedirect(request.getContextPath() + "/admin/home.html");
        } else if (path.contains("/shop/")) {
            response.sendRedirect(request.getContextPath() + "/shop/home.html");
        } else {
            response.sendRedirect(request.getContextPath() + "/shop/home.html");
        }
    }
}
