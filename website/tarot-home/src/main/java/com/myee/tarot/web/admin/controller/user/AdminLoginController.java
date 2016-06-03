package com.myee.tarot.web.admin.controller.user;

import com.myee.tarot.admin.service.AdminUserService;
import com.myee.tarot.core.service.GenericResponse;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.core.web.controller.AdminAbstractController;
import com.myee.tarot.core.web.form.ResetPasswordForm;
import com.myee.tarot.core.web.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Martin on 2016/4/21.
 */
@Controller
public class AdminLoginController extends AdminAbstractController {

    @Autowired
    protected AdminUserService adminUserService;

    @RequestMapping(value = "/admin/login.html", method = RequestMethod.GET)
    public String displayLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "/admin/login";
    }

    @RequestMapping(value = "/admin/forgotUsername", method = RequestMethod.POST)
    @ResponseBody
    public String processForgotUserName(HttpServletRequest request,
                                        @RequestParam("emailAddress") String email) {
        return null;
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    @ResponseBody
    public AjaxResponse processChangePassword(HttpServletRequest request, HttpServletResponse response, Model model,
                                              @ModelAttribute("resetPasswordForm") ResetPasswordForm resetPasswordForm) {
        GenericResponse errorResponse = adminUserService.changePassword(resetPasswordForm.getUsername(),
                resetPasswordForm.getOldPassword(),
                resetPasswordForm.getPassword(),
                resetPasswordForm.getConfirmPassword());

        if (errorResponse.getHasErrors()) {
            String errorCode = errorResponse.getErrorCodesList().get(0);
            return new AjaxResponse()
                    .addEntry("status", "error")
                    .addEntry("errorText", MessageUtil.getMessage("password." + errorCode));
        } else {
            return new AjaxResponse()
                    .addEntry("data.status", "ok")
                    .addEntry("successMessage", MessageUtil.getMessage("PasswordChange_success"));
        }
    }
}
