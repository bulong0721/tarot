package com.myee.tarot.web.admin.controller;

import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.admin.service.AdminUserService;
import com.myee.tarot.admin.service.RoleService;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.profile.domain.Role;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Martin on 2016/4/21.
 */
@Controller
public class AdminUserController {
    private static final Logger LOGGER           = LoggerFactory.getLogger(AdminUserController.class);
    private static final String DEFAULT_PASSWORD = "123456";

    @Autowired
    private AdminUserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private Ignite ignite;

    @RequestMapping(value = "admin/users/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse addUser(@RequestBody AdminUser user, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        if (null != user.getId()) {
            AdminUser dbUser = userService.findById(user.getId());
            dbUser.setName(user.getName());
            dbUser.setLogin(user.getLogin());
            dbUser.setPhoneNumber(user.getPhoneNumber());
            dbUser.setEmail(user.getEmail());
            dbUser.setActiveStatusFlag(user.getActiveStatusFlag());
            user = dbUser;
        } else {
            user.setPassword(DEFAULT_PASSWORD);
        }
        user = userService.update(user);
        resp = AjaxResponse.success();
        resp.addEntry("updateResult", user);
        return resp;
    }

    @RequestMapping(value = "admin/users/paging", method = RequestMethod.GET)
    public
    @ResponseBody
    AjaxPageableResponse pageUsers(Model model, HttpServletRequest request) {
        IgniteCache<Integer, String> cache = ignite.getOrCreateCache("test");
        cache.put(1, "Martin.xu" + new Date().getTime());
        AjaxPageableResponse resp = new AjaxPageableResponse();
        List<AdminUser> userList = userService.list();
        for (AdminUser user : userList) {
            Map entry = new HashMap();
            entry.put("id", user.getId());
            entry.put("login", user.getLogin());
            entry.put("name", user.getName());
            entry.put("email", user.getEmail());
            entry.put("phoneNumber", user.getPhoneNumber());
            entry.put("activeStatusFlag", user.getActiveStatusFlag());
            entry.put("lastLogin", user.getLastLogin());
            entry.put("loginIP", user.getLoginIP());
            resp.addDataEntry(entry);
        }
        return resp;
    }

    @RequestMapping(value = "admin/roles/paging", method = RequestMethod.GET)
    public
    @ResponseBody
    AjaxPageableResponse pageRoles(Model model, HttpServletRequest request) {
        IgniteCache<Integer, String> cache = ignite.getOrCreateCache("test");
        AjaxPageableResponse resp = new AjaxPageableResponse();
        List<Role> roleList = roleService.listAll();
        for (Role role : roleList) {
            Map entry = new HashMap();
            entry.put("id", role.getId());
            entry.put("roleName", role.getRoleName());
            entry.put("description", cache.get(1));
            resp.addDataEntry(entry);
        }
        return resp;
    }

    @RequestMapping(value = "admin/roles/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse mergeRole(@Valid @RequestBody Role role, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        role = roleService.update(role);
        resp = AjaxResponse.success();
        resp.addEntry("updateResult", role);
        return resp;
    }

    @ExceptionHandler({SQLException.class, Exception.class})
    protected void handleException(Exception ex, HttpServletResponse resp) {
        LOGGER.error(ex.getMessage(), ex);
    }
}
