package com.myee.tarot.web.admin.controller.user;

import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.admin.service.AdminRoleService;
import com.myee.tarot.admin.service.AdminUserService;
import com.myee.tarot.admin.service.RoleService;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.core.web.JQGridResponse;
import com.myee.tarot.reference.domain.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.sql.SQLException;
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

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/admin/users/save", method = RequestMethod.POST)
    public String addUser(@Valid @ModelAttribute AdminUser user, Model model, HttpServletRequest request) throws Exception {

        AdminUser dbUser = userService.getByUserName(user.getName());
        if (user.getId() != null) {

        }
        userService.save(dbUser);
        return "/admin/login";
    }

    @RequestMapping(value = "/admin/users/paging", method = RequestMethod.GET)
    public
    @ResponseBody
    JQGridResponse pageUsers(Model model, HttpServletRequest request) {
        JQGridResponse resp = new JQGridResponse();
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
        return resp;
    }

    @RequestMapping(value = "/admin/roles/paging", method = RequestMethod.GET)
    public
    @ResponseBody
    JQGridResponse pageRoles(Model model, HttpServletRequest request) {
        JQGridResponse resp = new JQGridResponse();
        List<Role> roleList = roleService.list();
        for (Role role : roleList) {
            Map entry = new HashMap();
            entry.put("id", role.getId());
            entry.put("roleName", role.getRoleName());
            entry.put("description", role.getDescription());
            resp.addDataEntry(entry);
        }
        return resp;
    }

    @RequestMapping(value = "/admin/roles/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse mergeRole(@Valid @RequestBody Role role, HttpServletRequest request) throws Exception {
        roleService.update(role);
        return AjaxResponse.success();
    }

    @ExceptionHandler({SQLException.class, Exception.class})
    protected void handleException(Exception ex, HttpServletResponse resp) {
        LOGGER.error(ex.getMessage(), ex);
    }
}
