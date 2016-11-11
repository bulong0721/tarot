package com.myee.tarot.web.admin.controller;

import com.alibaba.fastjson.JSON;
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

    @RequestMapping(value = {"admin/home.html", "admin/", "admin","admin/home"}, method = RequestMethod.GET)
    public ModelAndView displayDashboard(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mv = new ModelAndView("admin/home");
        Map<String,Object> entry = new HashMap<String ,Object>();
        entry.put("pushUrl",pushUrl);
        entry.put("qiniuCdn",qiniuCdn);
		entry.put("maxUploadSize",maxUploadSize);
        mv.addObject("downloadBase", JSON.toJSON(entry));
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
