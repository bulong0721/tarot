package com.myee.tarot.core.web.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by Martin on 2016/4/14.
 */
@Component
public class EstablishSessionFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        if (HttpServletRequest.class.isAssignableFrom(request.getClass())) {
            ((HttpServletRequest) request).getSession();
        }
        filterChain.doFilter(request, response);
    }
}
