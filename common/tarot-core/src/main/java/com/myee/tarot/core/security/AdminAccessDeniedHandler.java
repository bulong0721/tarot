package com.myee.tarot.core.security;

import com.alibaba.fastjson.JSON;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class AdminAccessDeniedHandler implements AccessDeniedHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminAccessDeniedHandler.class);

    @Value("${response.accessDeni}")
    private int RESPONSE_ACCESS_DENI;
    @Value("${response.accessDeniName}")
    private String RESPONSE_ACCESS_DENI_NAME;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = null;
        AjaxResponse resp = new AjaxResponse();
        resp.setStatus(RESPONSE_ACCESS_DENI);
        Map entry = new HashMap<String,Object>();
        entry.put(RESPONSE_ACCESS_DENI_NAME, request.getContextPath() + getAccessDeniedUrl());
        entry.put(Constants.ACCESS_DENI_MESSAGE,accessDeniedException.getMessage());
        resp.addDataEntry(entry);
        String jsonResp = JSON.toJSONString(resp);
        try {
            out = response.getWriter();
            out.append(jsonResp);
            LOGGER.debug("返回是\n");
            LOGGER.debug(jsonResp);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(),e);
        } finally {
            if (out != null) {
                out.close();
            }
        }

        //测试返回方式二：返回411的错误码，给前端由angular控制跳转
//        response.addHeader(RESPONSE_ACCESS_DENI_NAME, request.getContextPath() + getAccessDeniedUrl());
//        response.setStatus(RESPONSE_ACCESS_DENI);//--------------------------改json串
        //测试返回方式三：直接跳转
//        response.sendRedirect(request.getContextPath() + getAccessDeniedUrl());
    }

    public String getAccessDeniedUrl() {
        return accessDeniedUrl;
    }

    public void setAccessDeniedUrl(String accessDeniedUrl) {
        this.accessDeniedUrl = accessDeniedUrl;
    }

    private String accessDeniedUrl = null;

}
