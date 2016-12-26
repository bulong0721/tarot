package com.myee.tarot.admin.web.handler;

import com.alibaba.fastjson.JSON;
import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.admin.service.AdminUserService;
import com.myee.tarot.common.domain.LoginLog;
import com.myee.tarot.common.service.LoginLogService;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by Martin on 2016/4/18.
 */
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationSuccessHandler.class);

    @Autowired
    private AdminUserService userService;

	@Autowired
	private LoginLogService loginLogService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // last access timestamp
        String userName = authentication.getName();
        try {
            AdminUser user = userService.getByLogin(userName);

            user.setLoginIP(request.getRemoteAddr());
            user.setLastLogin(new Date());

            userService.update(user);
			//将登录日志记录到数据库

			LoginLog loginLog = new LoginLog();
			String ip = request.getHeader("x-forwarded-for");
			if(ip == null || ip.length() == 0 ||"unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if(ip == null || ip.length() == 0 ||"unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if(ip == null || ip.length() == 0 ||"unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
			loginLog.setLoginIP(ip);
			loginLog.setLoginTime(new Date());
			loginLog.setType(1);
			loginLog.setUserId(user.getId());
			String loginAddress = Address.getSingleInstance().getAddress(ip);
			loginLog.setLoginAddress(loginAddress);
			loginLogService.create(loginLog);
			LOGGER.info("ip={} LoginAddres={}",ip,loginAddress);
			String securityCode = request.getParameter(Constants.REQUEST_SECURITY_CODE);
            String sessionCode = (String) request.getSession().getAttribute(Constants.SESSION_SECURITY_CODE);
            if(StringUtil.isNullOrEmpty(sessionCode) || !sessionCode.equalsIgnoreCase(securityCode)){
                authentication.setAuthenticated(false);
            }
            response.sendRedirect(request.getContextPath() + "/admin/home.html");
        } catch (Exception e) {
            LOGGER.error("User authenticationSuccess", e);
        }
    }

}
