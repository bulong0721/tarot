package com.myee.tarot.web.admin.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Martin on 2016/4/21.
 */
@Controller
public class AdminController {

    @Value("${cleverm.push.http}")
    private String downloadBase;

    @RequestMapping(value = {"admin/home.html", "admin/", "admin"}, method = RequestMethod.GET)
    public ModelAndView displayDashboard(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mv = new ModelAndView("admin/home");
        mv.addObject("downloadBase",downloadBase);
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
