package com.myee.tarot.web.admin.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.myee.tarot.admin.domain.AdminPermission;
import com.myee.tarot.admin.domain.AdminRole;
import com.myee.tarot.admin.domain.AdminUser;
import com.myee.tarot.admin.service.AdminPermissionService;
import com.myee.tarot.admin.service.AdminRoleService;
import com.myee.tarot.admin.service.AdminUserService;
import com.myee.tarot.core.Constants;
import com.myee.tarot.core.util.ListSortUtil;
import com.myee.tarot.core.util.PageResult;
import com.myee.tarot.core.util.WhereRequest;
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
import java.util.*;

/**
 * Created by Martin on 2016/4/21.
 */
@Controller
public class AdminUserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminUserController.class);
    private static final String DEFAULT_PASSWORD = "1234561";
    private static final String DEFAULT_CUSTOMER_PASSWORD = "123456";

    @Autowired
    private AdminUserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AdminRoleService adminRoleService;

    @Autowired
    private AdminPermissionService adminPermissionService;

    @Autowired
    private MerchantStoreService merchantStoreService;

    @RequestMapping(value = "admin/users/paging", method = RequestMethod.GET)
    public
    @ResponseBody
    AjaxPageableResponse pageUsers(Model model, HttpServletRequest request, WhereRequest whereRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        PageResult<AdminUser> pageList = userService.pageList(whereRequest);
        List<AdminUser> userList = pageList.getList();
        for (AdminUser user : userList) {
            resp.addDataEntry(objectToEntry(user));
        }
        AdminUser user = (AdminUser) request.getSession().getAttribute(Constants.ADMIN_USER);
        Long id = user.getId();
        Map map = Maps.newHashMap();
        map.put("loggedUserId", id);
        resp.setRecordsTotal(pageList.getRecordsTotal());
        resp.setDataMap(map);
        return resp;
    }

    @RequestMapping(value = "admin/users/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse addUser(@RequestBody AdminUser user, HttpServletRequest request) throws Exception {
        AjaxResponse resp;
        MerchantStore merchantStore1;
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
                return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE, "请先切换门店");
            }
            merchantStore1 = (MerchantStore) o;
            user.setMerchantStore(merchantStore1);
            user.setPassword(DEFAULT_PASSWORD);
        }

        //校验登录名不能重复
        AdminUser adminUser = userService.getByLogin(user.getLogin());
        if (adminUser != null && !adminUser.getId().equals(user.getId())) {
            return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE,"错误:重复的登录名，请修改后重新提交");
        }

        try {
            user = userService.update(user);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE,"错误");
        }
        resp = AjaxResponse.success();
        resp.addEntry(Constants.RESPONSE_UPDATE_RESULT, objectToEntry(user));
        return resp;
    }

    @RequestMapping(value = "admin/users/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteUser(@Valid @RequestBody AdminUser adminUser, HttpServletRequest request) {
        AjaxResponse resp = new AjaxResponse();
        MerchantStore thisSwitchMerchantStore = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
        AdminUser loggedUser = (AdminUser) request.getSession().getAttribute(Constants.ADMIN_USER);
        if (thisSwitchMerchantStore == null) {
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("请先切换门店");
            return resp;
        }
        AdminUser adminUserFound = userService.findById(adminUser.getId());
        if (adminUserFound == null) {
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("错误:该用户不存在，无法被删除");
            return resp;
        }
        if (adminUserFound.getId().equals(loggedUser.getId())) {
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("要删除的用户是当前登录的账号，不能删除");
            return resp;
        }
        try {
            userService.delete(adminUserFound);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("有其他模块关联使用该账号，不能删除！比如资源日志。");
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
        entry.put("storeName", user.getMerchantStore() == null ? null : user.getMerchantStore().getName());
        entry.put("storeList",user.getAllMerchantStores());
        Set<AdminPermission> permissionSet = user.getAllPermissions();
        Set<AdminRole> roleSet = user.getAllRoles();
        if (roleSet != null && roleSet.size() > 0) {
            for (AdminRole adminRole : roleSet) {
                adminRole.setAllUsers(null);
                adminRole.setAllPermissions(null);
            }
            entry.put("roleList", roleSet);
        }

        Set<AdminPermission> tempPermission = Sets.newHashSet();
        return getCheckedPermission(entry, permissionSet, tempPermission);
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(AdminRole role) {
        Map entry = new HashMap();
        entry.put("id", role.getId());
        entry.put("name", role.getName());
        entry.put("description", role.getDescription());
        Set<AdminPermission> permissionSet = role.getAllPermissions();
        Set<AdminPermission> tempPermission = Sets.newHashSet();
        return getCheckedPermission(entry, permissionSet, tempPermission);
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(Role role) {
        Map entry = new HashMap();
        entry.put("id", role.getId());
        entry.put("roleName", role.getRoleName());
        entry.put("description", role.getDescription());
        Set<AdminPermission> permissionSet = role.getAllPermissions();
        Set<AdminPermission> tempPermission = Sets.newHashSet();
        return getCheckedPermission(entry, permissionSet, tempPermission);
    }

    private Map getCheckedPermission (Map entry, Set<AdminPermission> permissionSet, Set<AdminPermission> tempPermission) {
        if (permissionSet != null && permissionSet.size() > 0) {
            for (AdminPermission adminPermission : permissionSet) {
                putAllParentInSet(adminPermission, tempPermission);
                adminPermission.setAllUsers(null);
                adminPermission.setAllRoles(null);
                adminPermission.setAllChildPermissions(null);
            }
            permissionSet.addAll(tempPermission);
            entry.put("checkedPermissionList", permissionSet);
        } else {
            entry.put("checkedPermissionList", null);
        }
        return entry;
    }

    @RequestMapping(value = "admin/customers/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse addCustomer(@RequestBody Customer user, HttpServletRequest request) throws Exception {
        AjaxResponse resp;
        Customer userOld = customerService.getByUsername(user.getUsername());
        if (userOld != null && !userOld.getId().equals(user.getId())) {
            return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE, "已有该用户名");
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
                return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE, "请先切换门店");
            }
            MerchantStore merchantStore1 = (MerchantStore) o;
            user.setMerchantStore(merchantStore1);
            user.setPasswordChangeRequired(false);
            user.setRegistered(true);
            user.setPassword(DEFAULT_CUSTOMER_PASSWORD);
        }
        user = customerService.update(user);
        resp = AjaxResponse.success();
        resp.addEntry(Constants.RESPONSE_UPDATE_RESULT, objectToEntry(user));
        return resp;
    }

    @RequestMapping(value = "admin/customers/paging", method = RequestMethod.GET)
    public
    @ResponseBody
    AjaxPageableResponse pageCustomer(Model model, HttpServletRequest request, WhereRequest whereRequest) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        Object o = request.getSession().getAttribute(Constants.ADMIN_STORE);
        if (o == null) {
            resp.setErrorString("请先切换门店");
            return resp;
        }
        MerchantStore merchantStore1 = (MerchantStore) o;

        PageResult<Customer> pageList = customerService.pageByStore(merchantStore1.getId(), whereRequest);
        List<Customer> customerList = pageList.getList();
        for (Customer customer : customerList) {
            resp.addDataEntry(objectToEntry(customer));
        }
        resp.setRecordsTotal(pageList.getRecordsTotal());

        return resp;
    }

    @RequestMapping(value = "admin/customers/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteCustomer(@Valid @RequestBody Customer user, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        MerchantStore thisSwitchMerchantStore = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
        if (thisSwitchMerchantStore == null) {
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("请先切换门店");
            return resp;
        }
        Customer customerFound = customerService.findById(user.getId());
        if (customerFound == null) {
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("错误:该用户不存在，无法被删除");
            return resp;
        }
        try {
            customerService.delete(customerFound);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("有其他模块关联使用该账号，不能删除！比如资源日志。");
        }
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
        entry.put("storeName", customer.getMerchantStore() == null ? null : customer.getMerchantStore().getName());
        entry.put("storeList",customer.getAllMerchantStores());
        Set<Role> roleSet = customer.getAllRoles();
        if (roleSet != null && roleSet.size() > 0) {
            for (Role role : roleSet) {
                role.setAllCustomers(null);
                role.setAllPermissions(null);
            }
            entry.put("roleList",roleSet);
        }
        Set<AdminPermission> permissionSet = customer.getAllPermissions();
        Set<AdminPermission> tempPermission = Sets.newHashSet();
        return getCheckedPermission(entry, permissionSet, tempPermission);
    }

    @RequestMapping(value = "admin/roles/paging", method = RequestMethod.GET)
    public
    @ResponseBody
    AjaxPageableResponse pageRoles(Model model, HttpServletRequest request) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        List<Role> roleList = roleService.list();
        ListSortUtil<Role> sortList = new ListSortUtil<Role>();
        sortList.sort(roleList, "roleName", "asc");
        for (Role role : roleList) {
            Map entry = new HashMap();
            resp.addDataEntry(objectToEntry(role));
        }
        return resp;
    }

    @RequestMapping(value = "admin/roles/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse mergeRole(@Valid @RequestBody Role role, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        //校验角色名不能重复
        Role role1 = roleService.getByName(role.getRoleName());
        if (role1 != null && !role1.getId().equals(role.getId())) {
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("错误:重复的角色名，请修改后重新提交");
            return resp;
        }
        role = roleService.update(role);
        resp = AjaxResponse.success();
        resp.addEntry(Constants.RESPONSE_UPDATE_RESULT, role);
        return resp;
    }

    @RequestMapping(value = "admin/roles/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteRole(@Valid @RequestBody Role role, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();

        MerchantStore thisSwitchMerchantStore = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
        if (thisSwitchMerchantStore == null) {
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("请先切换门店");
            return resp;
        }
        Role roleFound = roleService.findById(role.getId());
        if (roleFound == null) {
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("错误:该角色不存在，无法被删除");
            return resp;
        }
        try {
            roleService.delete(roleFound);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("有其他模块关联使用该角色，不能删除！");
        }

        return resp;
    }

    @RequestMapping(value = "admin/roles/bindPermissions", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse bindRolePermissions(@RequestParam(value = "bindString")String bingString, @RequestParam(value = "roleId")Long roleId, HttpServletRequest request) {
        AjaxResponse resp;
        try {
            Role role = roleService.findById(roleId);
            List<Long> bindList = JSON.parseArray(bingString, Long.class);
            List<AdminPermission> permissionList = adminPermissionService.listByIds(bindList);
            Set<AdminPermission> permissionSet = null;
            if (permissionList != null) {
                permissionSet = Sets.newHashSet(permissionList);
            }
            role.setAllPermissions(permissionSet);
            role = roleService.update(role);
            resp = AjaxResponse.success();
            resp.addEntry(Constants.RESPONSE_UPDATE_RESULT, objectToEntry(role));
            return resp;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return AjaxResponse.failed(-1, "绑定失败");
        }
    }

    ////////////////////////////////////////////////////////////////////

    @RequestMapping(value = "admin/adminRoles/paging", method = RequestMethod.GET)
    public
    @ResponseBody
    AjaxPageableResponse pageAdminRoles(Model model, HttpServletRequest request) {
        AjaxPageableResponse resp = new AjaxPageableResponse();
        List<AdminRole> roleList = adminRoleService.list();
        ListSortUtil<AdminRole> sortList = new ListSortUtil<AdminRole>();
        sortList.sort(roleList, "name", "asc");
        for (AdminRole adminRole : roleList) {
            Map entry = new HashMap();
            resp.addDataEntry(objectToEntry(adminRole));
        }
        return resp;
    }

    @RequestMapping(value = "admin/adminRoles/save", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse mergeAdminRole(@Valid @RequestBody AdminRole role, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();
        //校验角色名不能重复
        AdminRole role1 = adminRoleService.getByName(role.getName());
        if (role1 != null && !role1.getId().equals(role.getId())) {
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("错误:重复的角色名，请修改后重新提交");
            return resp;
        }
        role = adminRoleService.update(role);
        resp = AjaxResponse.success();
        resp.addEntry(Constants.RESPONSE_UPDATE_RESULT, role);
        return resp;
    }

    @RequestMapping(value = "admin/adminRoles/delete", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse deleteAdminRole(@Valid @RequestBody AdminRole role, HttpServletRequest request) throws Exception {
        AjaxResponse resp = new AjaxResponse();

        MerchantStore thisSwitchMerchantStore = (MerchantStore) request.getSession().getAttribute(Constants.ADMIN_STORE);
        if (thisSwitchMerchantStore == null) {
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("请先切换门店");
            return resp;
        }
        AdminRole roleFound = adminRoleService.findById(role.getId());
        if (roleFound == null) {
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("错误:该角色不存在，无法被删除");
            return resp;
        }
        try {
            adminRoleService.delete(roleFound);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
            resp = AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE);
            resp.setErrorString("有其他模块关联使用该角色，不能删除！");
        }
        return resp;
    }

    @RequestMapping(value = "admin/adminRoles/bindPermissions", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse bindAdminRolePermissions(@RequestParam(value = "bindString")String bingString, @RequestParam(value = "roleId")Long roleId, HttpServletRequest request) {
        AjaxResponse resp;
        try {
            AdminRole adminRole = adminRoleService.findById(roleId);
            List<Long> bindList = JSON.parseArray(bingString, Long.class);
            List<AdminPermission> permissionList = adminPermissionService.listByIds(bindList);
            Set<AdminPermission> permissionSet = null;
            if (permissionList != null) {
                permissionSet = Sets.newHashSet(permissionList);
            }
            adminRole.setAllPermissions(permissionSet);
            adminRole = adminRoleService.update(adminRole);
            resp = AjaxResponse.success();
            resp.addEntry(Constants.RESPONSE_UPDATE_RESULT, objectToEntry(adminRole));
            return resp;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return AjaxResponse.failed(-1, "绑定失败");
        }
    }

    @ExceptionHandler({SQLException.class, Exception.class})
    protected void handleException(Exception ex, HttpServletResponse resp) {
        LOGGER.error(ex.getMessage(), ex);
    }

    @RequestMapping(value = "admin/listPermission/list", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse listAllPermissions(@RequestParam String isFriendly, HttpServletRequest request) {
        AjaxResponse resp = new AjaxResponse();
        Boolean isFriendlyBoolean;
        if (isFriendly.equals("true")) {
            isFriendlyBoolean = true;
        } else {
            isFriendlyBoolean = false;
        }
        List<AdminPermission> permissionList = adminPermissionService.listAllPermissions(isFriendlyBoolean);
        for (AdminPermission permission : permissionList) {
            resp.addDataEntry(objectToEntry(permission,isFriendlyBoolean));
        }
        return resp;
    }

    //把类转换成entry返回给前端，解耦和
    private Map objectToEntry(AdminPermission adminPermission, Boolean isFriendly) {
        Map entry = new HashMap();
        if (isFriendly) {
            entry.put("id", adminPermission.getId());
            entry.put("name", adminPermission.getDescription());
            entry.put("isFriendly", adminPermission.isFriendly());
            entry.put("eName", adminPermission.getName());
            entry.put("permissionType", adminPermission.getType());
            if (adminPermission.getAllChildPermissions() == null || adminPermission.getAllChildPermissions().size() == 0) {
                entry.put("type", Constants.PERMISSION_TREE_LEAF);
            } else {
                entry.put("type", Constants.PERMISSION_TREE_PARENT);
            }
            List<AdminPermission> adminPermissionListChild = adminPermission.getAllChildPermissions();
            List<Map> permissionChildListResult = null;
            if(adminPermissionListChild != null && adminPermissionListChild.size() >0) {
                permissionChildListResult = new ArrayList<Map>();
                for( AdminPermission permission : adminPermissionListChild ) {
                    permissionChildListResult.add(objectToEntry(permission, isFriendly));
                }
            }
            entry.put("children",permissionChildListResult);
        } else {
            if (adminPermission.isFriendly() == isFriendly) {
                entry.put("id", adminPermission.getId());
                entry.put("name", adminPermission.getDescription());
                entry.put("isFriendly", adminPermission.isFriendly());
                entry.put("eName", adminPermission.getName());
                entry.put("permissionType", adminPermission.getType());
                if (adminPermission.getAllChildPermissions() == null || adminPermission.getAllChildPermissions().size() == 0) {
                    entry.put("type", Constants.PERMISSION_TREE_LEAF);
                } else {
                    entry.put("type", Constants.PERMISSION_TREE_PARENT);
                }
                List<AdminPermission> adminPermissionListChild = adminPermission.getAllChildPermissions();
                List<Map> permissionChildListResult = null;
                if(adminPermissionListChild != null && adminPermissionListChild.size() >0) {
                    permissionChildListResult = new ArrayList<Map>();
                    for( AdminPermission permission : adminPermissionListChild ) {
                        if (permission.isFriendly() != isFriendly) {
                            continue;
                        }
                        permissionChildListResult.add(objectToEntry(permission, isFriendly));
                    }
                }
                entry.put("children",permissionChildListResult);
            }
        }
        return entry;
    }

    @RequestMapping(value = "admin/customers/bindMerchantStore", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse bindCustomerAndMerchantStore(@RequestParam(value = "bindString")String bingString, @RequestParam(value = "userId")Long userId, HttpServletRequest request) {
        AjaxResponse resp;
        try {
            Customer customer = customerService.findById(userId);
            if (customer == null) {
                return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE,"参数不正确");
            }
            List<Long> bindList = JSON.parseArray(bingString, Long.class);
            Set<MerchantStore> merchantStoreSet = null;
            if(bindList != null && bindList.size() > 0) {
                List<MerchantStore> merchantStores = merchantStoreService.listByIds(bindList);
                if (merchantStores != null) {
                    merchantStoreSet = Sets.newHashSet(merchantStores);
                }
            }

            customer.setAllMerchantStores(merchantStoreSet);
            customer = customerService.update(customer);
            resp = AjaxResponse.success();
            resp.addEntry(Constants.RESPONSE_UPDATE_RESULT, objectToEntry(customer));
            return resp;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return AjaxResponse.failed(-1, "绑定失败");
        }
    }

    @RequestMapping(value = "admin/users/bindRole", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse bindUserRole(@RequestParam(value = "bindString")String bingString, @RequestParam(value = "userId")Long userId, HttpServletRequest request) {
        AjaxResponse resp;
        try {
            AdminUser adminUser = userService.findById(userId);
            if (adminUser == null) {
                return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE,"参数不正确");
            }
            List<Long> bindList = JSON.parseArray(bingString, Long.class);
            Set<AdminRole> adminRoleSet = null;
            if(bindList != null && bindList.size() > 0) {
                List<AdminRole> adminRoleList = adminRoleService.listByIds(bindList);
                if (adminRoleList != null) {
                    adminRoleSet = Sets.newHashSet(adminRoleList);
                }
            }
            adminUser.setAllRoles(adminRoleSet);
            adminUser = userService.update(adminUser);
            resp = AjaxResponse.success();
            resp.addEntry(Constants.RESPONSE_UPDATE_RESULT, objectToEntry(adminUser));
            return resp;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return AjaxResponse.failed(-1, "绑定失败");
        }
    }

    @RequestMapping(value = "admin/customers/bindRole", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse bindCustomerRole(@RequestParam(value = "bindString")String bingString, @RequestParam(value = "userId")Long userId, HttpServletRequest request) {
        AjaxResponse resp;
        try {
            Customer customer = customerService.findById(userId);
            if (customer == null) {
                return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE,"参数不正确");
            }
            List<Long> bindList = JSON.parseArray(bingString, Long.class);
            Set<Role> adminRoleSet = null;
            if(bindList != null && bindList.size() > 0) {
                List<Role> customerRoleList = roleService.listByIds(bindList);
                if (customerRoleList != null) {
                    adminRoleSet = Sets.newHashSet(customerRoleList);
                }
            }
            customer.setAllRoles(adminRoleSet);
            customer = customerService.update(customer);
            resp = AjaxResponse.success();
            resp.addEntry(Constants.RESPONSE_UPDATE_RESULT, objectToEntry(customer));
            return resp;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return AjaxResponse.failed(-1, "绑定失败");
        }
    }

    @RequestMapping(value = "admin/users/bindMerchantStore", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse bindAdminAndMerchantStore(@RequestParam(value = "bindString")String bingString, @RequestParam(value = "userId")Long userId, HttpServletRequest request) {
        AjaxResponse resp;
        try {
            AdminUser adminUser = userService.findById(userId);
            if (adminUser == null) {
                return AjaxResponse.failed(AjaxResponse.RESPONSE_STATUS_FAIURE,"参数不正确");
            }
            List<Long> bindList = JSON.parseArray(bingString, Long.class);
            Set<MerchantStore> merchantStoreSet = null;
            if(bindList != null && bindList.size() > 0) {
                List<MerchantStore> merchantStores = merchantStoreService.listByIds(bindList);
                if (merchantStores != null) {
                    merchantStoreSet = Sets.newHashSet(merchantStores);
                }
            }

            adminUser.setAllMerchantStores(merchantStoreSet);
            adminUser = userService.update(adminUser);
            resp = AjaxResponse.success();
            resp.addEntry(Constants.RESPONSE_UPDATE_RESULT, objectToEntry(adminUser));
            return resp;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return AjaxResponse.failed(-1, "绑定失败");
        }
    }

    @RequestMapping(value = "admin/users/bindPermissions", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse bindAdminUserPermissions(@RequestParam(value = "bindString")String bingString, @RequestParam(value = "userId")Long userId, HttpServletRequest request) {
        AjaxResponse resp;
        try {
            AdminUser adminUser = userService.findById(userId);
            List<Long> bindList = JSON.parseArray(bingString, Long.class);
            List<AdminPermission> permissionList = adminPermissionService.listByIds(bindList);
            Set<AdminPermission> permissionSet = null;
            if (permissionList != null) {
                permissionSet = Sets.newHashSet(permissionList);
            }
            adminUser.setAllPermissions(permissionSet);
            adminUser = userService.update(adminUser);
            resp = AjaxResponse.success();
            resp.addEntry(Constants.RESPONSE_UPDATE_RESULT, objectToEntry(adminUser));
            return resp;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return AjaxResponse.failed(-1, "绑定失败");
        }
    }

    @RequestMapping(value = "admin/customer/bindPermissions", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse bindCustomerPermissions(@RequestParam(value = "bindString")String bingString, @RequestParam(value = "customerId")Long customerId, HttpServletRequest request) {
        AjaxResponse resp;
        try {
            Customer customer = customerService.findById(customerId);
            List<Long> bindList = JSON.parseArray(bingString, Long.class);
            List<AdminPermission> permissionList = adminPermissionService.listByIds(bindList);
            Set<AdminPermission> permissionSet = null;
            if (permissionList != null) {
                permissionSet = Sets.newHashSet(permissionList);
            }
            customer.setAllPermissions(permissionSet);
            customer = customerService.update(customer);
            resp = AjaxResponse.success();
            resp.addEntry(Constants.RESPONSE_UPDATE_RESULT, objectToEntry(customer));
            return resp;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return AjaxResponse.failed(-1, "绑定失败");
        }
    }

    /**
     * 将某个权限的所有递归父权限放入set
     * @param adminPermission
     * @param set
     */
    private void putAllParentInSet(AdminPermission adminPermission, Set<AdminPermission> set) {
        List<AdminPermission> listParentPermission = adminPermission.getAllParentPermissions();
        if (listParentPermission != null) {
            for (AdminPermission adminPermission1 : listParentPermission) {
                adminPermission1.setAllChildPermissions(null);
                set.add(adminPermission1);
                putAllParentInSet(adminPermission1, set);
            }
        }
    }

    @RequestMapping(value = "admin/role/listAdminRole", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse getAllAdminRole() {
        try {
            AjaxResponse resp = new AjaxResponse();
            List<AdminRole> result = adminRoleService.list();
            for (AdminRole adminRole : result) {
                resp.addDataEntry(objectToEntry(adminRole));
            }
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);

    }

    @RequestMapping(value = "admin/role/listCustomerRole", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResponse getAllCustomerRole() {
        try {
            AjaxResponse resp = new AjaxResponse();
            List<Role> result = roleService.list();
            for (Role role : result) {
                resp.addDataEntry(objectToEntry(role));
            }
            return resp;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResponse.failed(-1);

    }
}

