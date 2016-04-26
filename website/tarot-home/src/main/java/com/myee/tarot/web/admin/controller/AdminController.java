package com.myee.tarot.web.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Martin on 2016/4/21.
 */
@Controller
public class AdminController {

    @RequestMapping(value = {"/admin/home.html", "/admin/", "/admin"}, method = RequestMethod.GET)
    public String displayDashboard(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "/admin/home";
    }
}
