package com.myee.tarot.web.customer.controller;

import com.alibaba.fastjson.JSON;
import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.core.Constants;
import com.myee.tarot.customer.domain.Customer;
import com.myee.tarot.merchant.domain.MerchantStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
public class CustomerController {

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
    @Value("${response.sessionTimeOutUrlShop}")
    private String RESPONSE_SESSION_TIMEOUT_URL_SHOP;

    @RequestMapping(value = {"shop/home.html", "shop/", "shop","shop/home"}, method = RequestMethod.GET)
    public ModelAndView displayDashboard(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mv = new ModelAndView("shop/home");
        Map<String,Object> entry = new HashMap<String ,Object>();
        entry.put(Constants.RESPONSE_PUSH_URL,pushUrl);
        entry.put(Constants.RESPONSE_QINIU_CDN,qiniuCdn);
        entry.put(Constants.RESPONSE_MAX_UPLOAD_SIZE,maxUploadSize);
        entry.put(Constants.RESPONSE_SESSION_TIMEOUT_URL,RESPONSE_SESSION_TIMEOUT_URL_SHOP);
        entry.put(Constants.RESPONSE_USERNAME,((Customer)request.getSession().getAttribute(Constants.CUSTOMER_USER)).getUsername());
        entry.put(Constants.RESPONSE_USER_ALL_PERMISSIONS,request.getSession().getAttribute(Constants.RESPONSE_USER_ALL_PERMISSIONS));
        entry.put(Constants.RESPONSE_THIS_STORE_NAME, ((MerchantStore) request.getSession().getAttribute(Constants.CUSTOMER_STORE)).getName());
        entry.put(RESPONSE_ACCESS_DENI_NAME,RESPONSE_ACCESS_DENI);
        mv.addObject(Constants.RESPONSE_DOWNLOAD_BASE, JSON.toJSON(entry));
        return mv;
    }

    @RequestMapping(value = {"shop/denied.html"}, method = RequestMethod.GET)
    public String displayDenied(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "shop/denied";
    }

    @RequestMapping(value = {"shop/unauthorized.html"}, method = RequestMethod.GET)
    public String displayUnauthorized(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "shop/unauthorized";
    }
}
