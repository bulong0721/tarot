package com.myee.tarot.web.admin.controller.user;

import com.myee.tarot.admin.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by Martin on 2016/4/21.
 */
@Controller
public class UserController {

    @Autowired
    private AdminUserService userService;

    @RequestMapping(value = "/admin/users/save.html", method = RequestMethod.POST)
    public String addUser(@Valid @ModelAttribute com.myee.tarot.admin.domain.AdminUser user, Model model, HttpServletRequest request) throws Exception {

        com.myee.tarot.admin.domain.AdminUser dbUser = userService.getByUserName(user.getName());
        if (user.getId() != null) {

        }
        userService.save(dbUser);
        return "/admin/login";
    }

}
