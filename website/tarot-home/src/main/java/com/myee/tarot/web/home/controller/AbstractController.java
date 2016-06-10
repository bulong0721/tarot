package com.myee.tarot.web.home.controller;

import com.myee.tarot.web.home.model.paging.PaginationData;
import com.myee.tarot.web.util.SessionUtil;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Martin on 2016/4/21.
 */
public abstract class AbstractController {

    @SuppressWarnings("unchecked")
    protected <T> T getSessionAttribute(final String key, HttpServletRequest request) {
        return (T) SessionUtil.getSessionAttribute(key, request);

    }

    protected void setSessionAttribute(final String key, final Object value, HttpServletRequest request) {
        SessionUtil.setSessionAttribute(key, value, request);
    }

    protected void removeAttribute(final String key, HttpServletRequest request) {
        SessionUtil.removeSessionAttribute(key, request);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Model model, Exception ex, HttpServletRequest request) {
//        MerchantStore store = getSessionAttribute(Constants.MERCHANT_STORE, request);
        StringBuilder template = null;
        //ModelAndView model = null;
//        if (ex instanceof AccessDeniedException) {
//            if (store != null) {
//                template = new StringBuilder().append(ControllerConstants.Tiles.Error.accessDenied).append(".").append(store.getStoreTemplate());
//            } else {
//                template = new StringBuilder().append(ControllerConstants.Tiles.Error.accessDenied);
//            }
//            //model = new ModelAndView("error/access_denied");
//        } else {
//
//            model.addAttribute("stackError", ExceptionUtils.getStackTrace(ex));
//            model.addAttribute("errMsg", ex.getMessage());
//            if (store != null) {
//                template = new StringBuilder().append(ControllerConstants.Tiles.Error.error).append(".").append(store.getStoreTemplate());
//            } else {
//                template = new StringBuilder().append(ControllerConstants.Tiles.Error.error);
//            }
//            //model = new ModelAndView("error/generic_error");
//            //model.addObject("stackError", ExceptionUtils.getStackTrace(ex));
//            //model.addObject("errMsg", ex.getMessage());
//        }
        return template.toString();
    }

    protected PaginationData createPaginaionData(final int pageNumber, final int pageSize) {
        final PaginationData paginaionData = new PaginationData(pageSize, pageNumber);

        return paginaionData;
    }

    protected PaginationData calculatePaginaionData(final PaginationData paginationData, final int pageSize, final int resultCount) {

        int currentPage = paginationData.getCurrentPage();


        int count = Math.min((currentPage * pageSize), resultCount);
        paginationData.setCountByPage(count);

        paginationData.setTotalCount(resultCount);
        return paginationData;
    }
}
