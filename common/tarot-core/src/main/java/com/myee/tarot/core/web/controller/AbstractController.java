package com.myee.tarot.core.web.controller;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Martin on 2016/4/14.
 */
public abstract class AbstractController {

    protected boolean isAjaxRequest(HttpServletRequest request) {
        return ControllerUtility.isAjaxRequest(request);
    }

    protected String getContextPath(HttpServletRequest request) {
        String ctxPath = request.getContextPath();
        if (StringUtils.isBlank(ctxPath)) {
            return "/";
        } else {
            if (ctxPath.charAt(0) != '/') {
                ctxPath = '/' + ctxPath;
            }
            if (ctxPath.charAt(ctxPath.length() - 1) != '/') {
                ctxPath = ctxPath + '/';
            }
            return ctxPath;
        }
    }

    protected String jsonResponse(HttpServletResponse response, Map<?, ?> responseMap) throws IOException {
        response.setHeader("Content-Type", "application/json");
        JSON.writeJSONStringTo(responseMap, response.getWriter());
        return null;
    }
}
