package com.myee.tarot.core.web.controller;

import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Martin on 2016/4/14.
 */
public class RedirectController {

    public String redirect(HttpServletRequest request, HttpServletResponse response, Model model) {
        String path = request.getContextPath();
        return "redirect:" + path;
    }
}
