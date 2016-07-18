package com.myee.tarot.web.filter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Info: wap请求管理
 * User: zhangxinglong@rui10.com
 * Date: 5/3/15
 * Time: 11:24
 * Version: 1.0
 * History: <p>如果有修改过程，请记  录</P>
 */
public class WapRequestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse) res;
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "sessionId,token,x-requested-with");
        if(request.getMethod().equalsIgnoreCase("OPTIONS")){
            return;
        }
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {

    }
}
