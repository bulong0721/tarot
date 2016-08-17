package com.myee.tarot.web.customer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Martin on 2016/4/21.
 */
@Controller
public class CustomerLoginController {

    @RequestMapping(value = "shop/login.html", method = RequestMethod.GET)
    public String displayLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "shop/login";
    }

    @RequestMapping(value = "shop/forgotUsername", method = RequestMethod.POST)
    @ResponseBody
    public String processForgotUserName(HttpServletRequest request,
                                        @RequestParam("emailAddress") String email) {
        return null;
    }
}
