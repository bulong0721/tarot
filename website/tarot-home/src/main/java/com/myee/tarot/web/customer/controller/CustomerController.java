package com.myee.tarot.web.customer.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Martin on 2016/4/21.
 */
@Controller
public class CustomerController {

    @Value("${cleverm.push.http}")
    private String downloadBase;

    @RequestMapping(value = {"shop/home.html", "shop/", "shop"}, method = RequestMethod.GET)
    public ModelAndView displayDashboard(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mv = new ModelAndView("shop/home");
        mv.addObject("downloadBase",downloadBase);
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
