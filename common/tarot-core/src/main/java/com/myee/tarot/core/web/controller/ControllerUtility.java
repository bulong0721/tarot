package com.myee.tarot.core.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Martin on 2016/4/14.
 */
public class ControllerUtility {
    protected static final Log LOG = LogFactory.getLog(ControllerUtility.class);

    public static final String BLC_REDIRECT_ATTRIBUTE = "blc_redirect";
    public static final String BLC_AJAX_PARAMETER     = "blcAjax";

    /**
     * A helper method that returns whether or not the given request was invoked via an AJAX call
     * <p/>
     * Returns true if the request contains the XMLHttpRequest header or a blcAjax=true parameter.
     *
     * @param request
     * @return - whether or not it was an AJAX request
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        String ajaxParameter = request.getParameter(BLC_AJAX_PARAMETER);
        String requestedWithHeader = request.getHeader("X-Requested-With");
        boolean result = (ajaxParameter != null && "true".equals(ajaxParameter))
                || "XMLHttpRequest".equals(requestedWithHeader);

        if (LOG.isTraceEnabled()) {
            StringBuilder sb = new StringBuilder()
                    .append("Request URL: [").append(request.getServletPath()).append("]")
                    .append(" - ")
                    .append("ajaxParam: [").append(String.valueOf(ajaxParameter)).append("]")
                    .append(" - ")
                    .append("X-Requested-With: [").append(requestedWithHeader).append("]")
                    .append(" - ")
                    .append("Returning: [").append(result).append("]");
            LOG.trace(sb.toString());
        }

        return result;
    }
}
