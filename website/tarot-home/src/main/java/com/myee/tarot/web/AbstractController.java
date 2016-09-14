package com.myee.tarot.web;

import com.myee.tarot.core.Constants;
import com.myee.tarot.core.exception.ServiceException;
import com.myee.tarot.core.util.ajax.AjaxResponse;
import com.myee.tarot.core.web.util.SessionUtil;
import com.myee.tarot.merchant.domain.MerchantStore;
import jodd.exception.ExceptionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Martin on 2016/4/14.
 */
public abstract class AbstractController {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public AjaxResponse handleException(Model model, Exception ex, HttpServletRequest request) {
        MerchantStore store = SessionUtil.getSessionAttribute(Constants.ADMIN_STORE, request);
        StringBuilder template = null;
        //ModelAndView model = null;
        if (ex instanceof AccessDeniedException) {

        } else if (ex instanceof ServiceException) {

        } else {
            model.addAttribute("stackError", ExceptionUtil.getStackTrace(ex, null, null));
            model.addAttribute("errMsg", ex.getMessage());
        }
        return AjaxResponse.failed(-1);

    }
}
