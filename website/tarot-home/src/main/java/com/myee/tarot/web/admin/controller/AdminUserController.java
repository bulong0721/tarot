package com.myee.tarot.web.admin.controller;

import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.admin.service.AdminUserService;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.PageRequest;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.ajax.AjaxPageableResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.customer.domain.Customer;
import com.myee.tarot.customer.service.CustomerService;
import com.myee.tarot.merchant.domain.MerchantStore;
import com.myee.tarot.merchant.service.MerchantStoreService;
import com.myee.tarot.profile.domain.Role;
import com.myee.tarot.profile.service.RoleService;
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
public class AdminUserController {
    private static final Logger LOGGER           = LoggerFactory.getLogger(AdminUserController.class);
    private static final String DEFAULT_PASSWORD = "1234561";
    private static final String DEFAULT_CUSTOMER_PASSWORD = "123456";

    @Autowired
    private AdminUserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "admin/users/save", method = RequestMethod.POST)
     @ResponseBody
     public AjaxResponse addUser(@RequestBody AdminUser user, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        MerchantStore merchantStore1 = null;
        if (null != user.getId()) {
            AdminUser dbUser = userService.findById(user.getId());
            dbUser.setName(user.getName());
            dbUser.setLogin(user.getLogin());
            dbUser.setPhoneNumber(user.getPhoneNumber());
            dbUser.setEmail(user.getEmail());
            dbUser.setActiveStatusFlag(user.getActiveStatusFlag());
            user = dbUser;
        } else {
            //新建账号将绑定切换的门店
            Object o = request.getSession().getAttribute(Constants.ADMIN_STORE);
            if (o == null) {
                return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE,"请先切换门店");
            }
            merchantStore1 = (MerchantStore)o;
            user.setMerchantStore(merchantStore1);
            user.setPassword(DEFAULT_PASSWORD);
        }
        user = userService.update(user);
        resp = AjaxResponse.success();
        resp.addEntry("updateResult", objectToEntry(user));
        return resp;
    }

    @RequestMapping(value = "admin/users/paging", method = RequestMethod.GET)
    public
    @ResponseBody
    AjaxPageableResponse pageUsers(Model model, HttpServletRequest request) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        List<AdminUser> userList = userService.list();
        for (AdminUser user : userList) {
            resp.addDataEntry(objectToEntry(user));
        }
        return resp;
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(AdminUser user) {
        Map entry = new HashMap();
        entry.put("id", user.getId());
        entry.put("login", user.getLogin());
        entry.put("name", user.getName());
        entry.put("email", user.getEmail());
        entry.put("phoneNumber", user.getPhoneNumber());
        entry.put("activeStatusFlag", user.getActiveStatusFlag());
        entry.put("lastLogin", user.getLastLogin());
        entry.put("loginIP", user.getLoginIP());
        entry.put("storeName", user.getMerchantStore().getName());
        return entry;
    }

    @RequestMapping(value = "admin/customers/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse addCustomer(@RequestBody Customer user, HttpServletRequest request) throws Exception {
        AjaxResponse resp;
        Customer userOld = customerService.getByUsername(user.getUsername());
        if( userOld != null && userOld.getId() != user.getId() ){
            return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE,"已有该用户名");
        }
        if (null != user.getId()) {
            Customer dbUser = customerService.findById(user.getId());
            dbUser.setEmailAddress(user.getEmailAddress());
            dbUser.setFirstName(user.getFirstName());
            dbUser.setLastName(user.getLastName());
            dbUser.setUsername(user.getUsername());
            dbUser.setReceiveEmail(user.isReceiveEmail());
            dbUser.setDeactivated(user.isDeactivated());
            user = dbUser;
        } else {
            Object o = request.getSession().getAttribute(Constants.ADMIN_STORE);
            //新建账号将绑定切换的门店
            if (o == null) {
                return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE,"请先切换门店");
            }
            MerchantStore merchantStore1 = (MerchantStore)o;
            user.setMerchantStore(merchantStore1);
            user.setPasswordChangeRequired(false);
            user.setRegistered(true);
            user.setPassword(DEFAULT_CUSTOMER_PASSWORD);
        }
        user = customerService.update(user);
        resp = AjaxResponse.success();
        resp.addEntry("updateResult", user);
        return resp;
    }

    @RequestMapping(value = "admin/customers/paging", method = RequestMethod.GET)
    public
    @ResponseBody
    AjaxPageableResponse pageCustomer(Model model, HttpServletRequest request, PageRequest pageRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        Object o = request.getSession().getAttribute(Constants.ADMIN_STORE);
        if (o == null) {
            resp.setErrorString("请先切换门店");
            return resp;
        }
        MerchantStore merchantStore1 = (MerchantStore) o;

        PageResult<Customer> pageList = customerService.pageByStore(merchantStore1.getId(), pageRequest);
        List<Customer> customerList = pageList.getList();
        for (Customer customer : customerList) {
            resp.addDataEntry(objectToEntry(customer));
        }
        resp.setRecordsTotal(pageList.getRecordsTotal());

        return resp;
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(Customer customer) {
        Map entry = new HashMap();
        entry.put("id", customer.getId());
        entry.put("emailAddress", customer.getEmailAddress());
        entry.put("firstName", customer.getFirstName());
        entry.put("lastName", customer.getLastName());
        entry.put("username", customer.getUsername());
        entry.put("receiveEmail", customer.isReceiveEmail());
        entry.put("deactivated", customer.isDeactivated());
        return entry;
    }

    @RequestMapping(value = "admin/roles/paging", method = RequestMethod.GET)
    public
    @ResponseBody
    AjaxPageableResponse pageRoles(Model model, HttpServletRequest request) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
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
