//package com.myee.tarot.admin.web.handler;
//
//import com.myee.tarot.core.util.StringUtil;
//import org.apache.commons.lang.BooleanUtils;
//import org.apache.commons.lang.StringUtils;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * Created by Martin on 2016/4/18.
// */
//public class AdminAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
//    private String defaultFailureUrl;
//
//    public AdminAuthenticationFailureHandler() {
//        super();
//    }
//
//    public AdminAuthenticationFailureHandler(String defaultFailureUrl) {
//        super(defaultFailureUrl);
//        this.defaultFailureUrl = defaultFailureUrl;
//    }
//
//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//        String failureUrlParam = StringUtil.cleanseUrlString(request.getParameter("failureUrl"));
//        String successUrlParam = StringUtil.cleanseUrlString(request.getParameter("successUrl"));
//        String failureUrl = failureUrlParam == null ? null : failureUrlParam.trim();
//        Boolean sessionTimeout = (Boolean) request.getAttribute("sessionTimeout");
//
//        if (StringUtils.isEmpty(failureUrl) && BooleanUtils.isNotTrue(sessionTimeout)) {
//            failureUrl = defaultFailureUrl;
//        }
//
//        if (BooleanUtils.isTrue(sessionTimeout)) {
//            failureUrl = "?sessionTimeout=true";
//        }
//
//        if (StringUtils.isEmpty(successUrlParam)) {
//            //Grab url the user, was redirected from
//            successUrlParam = request.getHeader("referer");
//        }
//
//        if (failureUrl != null) {
//            if (!StringUtils.isEmpty(successUrlParam)) {
//                //Preserve the original successUrl from the referer.  If there is one, it must be the last url segment
//                int successUrlPos = successUrlParam.indexOf("successUrl");
//                if (successUrlPos >= 0) {
//                    successUrlParam = successUrlParam.substring(successUrlPos);
//                } else {
//                    successUrlParam = "successUrl=" + successUrlParam;
//                }
//
//                if (!failureUrl.contains("?")) {
//                    failureUrl += "?" + successUrlParam;
//                } else {
//                    failureUrl += "&" + successUrlParam;
//                }
//            } else {
//
//            }
//
//            saveException(request, exception);
//            getRedirectStrategy().sendRedirect(request, response, failureUrl);
//        } else {
//            super.onAuthenticationFailure(request, response, exception);
//        }
//    }
//}
