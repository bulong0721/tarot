package com.myee.tarot.web.admin.controller;

import com.alibaba.fastjson.JSON;
import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.core.Constants;
import com.myee.tarot.merchant.domain.MerchantStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Martin on 2016/4/21.
 */
@Controller
public class AdminController {

    @Value("${cleverm.push.http}")
    private String pushUrl;
    @Value("${qiniu_cdn}")
    private String qiniuCdn;
	@Value("${maxUploadSize}")
	private String maxUploadSize;
    @Value("${response.accessDeni}")
    private int RESPONSE_ACCESS_DENI;
    @Value("${response.accessDeniName}")
    private String RESPONSE_ACCESS_DENI_NAME;
    @Value("${response.sessionTimeOutUrlAdmin}")
    private String RESPONSE_SESSION_TIMEOUT_URL_ADMIN;

    @RequestMapping(value = {"admin/home.html", "admin/", "admin","admin/home"}, method = RequestMethod.GET)
    public ModelAndView displayDashboard(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mv = new ModelAndView("admin/home");
        Map<String,Object> entry = new HashMap<String ,Object>();
        entry.put(Constants.RESPONSE_PUSH_URL,pushUrl);
        entry.put(Constants.RESPONSE_QINIU_CDN,qiniuCdn);
		entry.put(Constants.RESPONSE_MAX_UPLOAD_SIZE,maxUploadSize);
        entry.put(Constants.RESPONSE_SESSION_TIMEOUT_URL,RESPONSE_SESSION_TIMEOUT_URL_ADMIN);
        entry.put(Constants.RESPONSE_USERNAME,((AdminUser)request.getSession().getAttribute(Constants.ADMIN_USER)).getLogin());
        entry.put(Constants.RESPONSE_THIS_STORE_NAME, ((MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE)).getName());
        entry.put(RESPONSE_ACCESS_DENI_NAME,RESPONSE_ACCESS_DENI);
        mv.addObject(Constants.RESPONSE_DOWNLOAD_BASE, JSON.toJSON(entry));
        return mv;
    }

    @RequestMapping(value = {"admin/denied.html"}, method = RequestMethod.GET)
    public String displayDenied(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "admin/denied";
    }

    @RequestMapping(value = {"admin/unauthorized.html"}, method = RequestMethod.GET)
    public String displayUnauthorized(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "admin/unauthorized";
    }
}
