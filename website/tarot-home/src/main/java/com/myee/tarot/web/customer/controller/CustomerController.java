package com.myee.tarot.web.customer.controller;

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
public class CustomerController {

    @RequestMapping(value = {"shop/home.html", "shop/", "shop"}, method = RequestMethod.GET)
    public String displayDashboard(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "shop/home";
    }
}
