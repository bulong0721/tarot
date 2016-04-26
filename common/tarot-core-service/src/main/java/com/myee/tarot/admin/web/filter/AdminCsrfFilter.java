package com.myee.tarot.admin.web.filter;

import com.myee.tarot.admin.service.ExploitProtectionService;
import com.myee.tarot.core.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Martin on 2016/4/18.
 */
public class AdminCsrfFilter extends GenericFilterBean {

    @Autowired
    protected AuthenticationFailureHandler failureHandler;

    @Autowired
    protected ExploitProtectionService exploitProtectionService;

    protected List<String> excludedRequestPatterns;

    @Override
    public void doFilter(ServletRequest baseRequest, ServletResponse baseResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) baseRequest;
        HttpServletResponse response = (HttpServletResponse) baseResponse;
        try {
            boolean excludedRequestFound = false;
            if (excludedRequestPatterns != null && excludedRequestPatterns.size() > 0) {
                for (String pattern : excludedRequestPatterns) {
                    RequestMatcher matcher = new AntPathRequestMatcher(pattern);
                    if (matcher.matches(request)) {
                        excludedRequestFound = true;
                        break;
                    }
                }
            }

            if (request.getMethod().equals("POST") && !excludedRequestFound) {
                String requestToken = request.getParameter(exploitProtectionService.getCsrfTokenParameter());
                try {
                    exploitProtectionService.compareToken(requestToken);
                } catch (ServiceException e) {
                    throw new ServletException(e);
                }
            }

            chain.doFilter(request, response);
        } catch (ServletException var6) {
            if (!(var6.getCause() instanceof ServiceException)) {
                throw var6;
            }

            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                throw var6;
            }

            request.setAttribute("sessionTimeout", Boolean.valueOf(true));
            this.failureHandler.onAuthenticationFailure(request, response, new SessionAuthenticationException("Session Time Out"));
        }
    }
}
