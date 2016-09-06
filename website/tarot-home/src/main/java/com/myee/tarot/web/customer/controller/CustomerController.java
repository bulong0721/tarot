package com.myee.tarot.web.customer.controller;

import com.alibaba.fastjson.JSON;
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

    @RequestMapping(value = {"shop/home.html", "shop/", "shop"}, method = RequestMethod.GET)
    public ModelAndView displayDashboard(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mv = new ModelAndView("shop/home");
        Map<String,Object> entry = new HashMap<String ,Object>();
        entry.put("pushUrl",pushUrl);
        entry.put("qiniuCdn",qiniuCdn);
        mv.addObject("downloadBase", JSON.toJSON(entry));
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
