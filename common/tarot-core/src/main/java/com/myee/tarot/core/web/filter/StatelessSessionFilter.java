package com.myee.tarot.core.web.filter;

import com.myee.tarot.core.web.util.RequestUtil;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Martin on 2016/4/14.
 */
public class StatelessSessionFilter extends GenericFilterBean {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        RequestUtil.setOKtoUseSession(new ServletWebRequest((HttpServletRequest) request, (HttpServletResponse) response), Boolean.FALSE);
        SessionlessRequestWrapper wrapper = new SessionlessRequestWrapper((HttpServletRequest) request);
        filterChain.doFilter(wrapper, response);
    }
}

