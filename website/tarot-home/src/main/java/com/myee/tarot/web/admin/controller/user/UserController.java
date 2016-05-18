package com.myee.tarot.web.admin.controller.user;

import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.admin.service.AdminUserService;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.core.web.JQGridRequest;
import com.myee.tarot.core.web.JQGridResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Martin on 2016/4/21.
 */
@Controller
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private AdminUserService userService;

    @RequestMapping(value = "/admin/users/save.html", method = RequestMethod.POST)
    public String addUser(@Valid @ModelAttribute AdminUser user, Model model, HttpServletRequest request) throws Exception {

        AdminUser dbUser = userService.getByUserName(user.getName());
        if (user.getId() != null) {

        }
        userService.save(dbUser);
        return "/admin/login";
    }

    @RequestMapping(value = "/admin/users/paging.html", method = RequestMethod.GET)
    public
    @ResponseBody
    JQGridResponse pageUsers(Model model, HttpServletRequest request) {
        JQGridResponse resp = new JQGridResponse();
        String currentUser = request.getRemoteUser();
        try {
            List<AdminUser> userList = userService.list();
            for (AdminUser user : userList) {
                Map entry = new HashMap();
                entry.put("login", user.getLogin());
                entry.put("name", user.getName());
                entry.put("email", user.getEmail());
                entry.put("phone", user.getPhoneNumber());
                entry.put("active", user.getActiveStatusFlag());
                resp.addDataEntry(entry);
            }
        } catch (Exception e) {
            LOGGER.error("Error while paging products", e);
        }
        return resp;
    }

}
