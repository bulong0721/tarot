package com.myee.tarot.admin.web.resolver;

import com.myee.tarot.core.web.controller.ControllerUtility;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class AdminMappingExceptionResolver extends SimpleMappingExceptionResolver {

    private static final Log LOG = LogFactory.getLog(AdminMappingExceptionResolver.class);

    protected boolean showDebugMessage = false;

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                         Exception ex) {
        if (ControllerUtility.isAjaxRequest(request)) {
            // Set up some basic response attributes
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ModelAndView mav = new ModelAndView("utility/blcException");

            // Friendly message
            mav.addObject("exceptionMessage", ex.getMessage());

            mav.addObject("showDebugMessage", showDebugMessage);
            if (showDebugMessage) {
                StringBuilder sb2 = new StringBuilder();
                appendStackTrace(ex, sb2);
                mav.addObject("debugMessage", sb2.toString());
                LOG.error("Unhandled error processing ajax request", ex);
            }

            // Add the message to the model so we can render it 
            return mav;
        } else {
            // If the exception is "Entity not found" redirect to main listgrid view
            if (ex.getClass().equals(EntityNotFoundException.class)) {
                String servletPath = request.getServletPath();

                // Remove erroneous entity Id from servletPath
                servletPath = servletPath.substring(0, servletPath.lastIndexOf('/'));
                return new ModelAndView("redirect:" + servletPath);
            }
            return super.resolveException(request, response, handler, ex);
        }
    }
    
    /**
     * By default, appends the exception and its message followed by the file location that triggered this exception.
     * Recursively builds this out for each cause of the given exception.
     * 
     * @param throwable
     * @param sb
     */
    protected void appendStackTrace(Throwable throwable, StringBuilder sb) {
        if (throwable == null) {
            return;
        }
        
        StackTraceElement[] st = throwable.getStackTrace();
        if (st != null && st.length > 0) {
            sb.append("\r\n\r\n");
            sb.append(throwable.toString());
            sb.append("\r\n");
            sb.append(st[0].toString());
        }
        
        appendStackTrace(throwable.getCause(), sb);
    }

    public boolean isShowDebugMessage() {
        return showDebugMessage;
    }
    
    public void setShowDebugMessage(boolean showDebugMessage) {
        this.showDebugMessage = showDebugMessage;
    }

}
