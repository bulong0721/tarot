/*
 * #%L
 * BroadleafCommerce Common Libraries
 * %%
 * Copyright (C) 2009 - 2013 Broadleaf Commerce
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.myee.tarot.core.web;

import com.myee.tarot.core.classloader.ThreadLocalManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


public class RequestContext {

    protected static final Log LOG = LogFactory.getLog(RequestContext.class);

    private static final ThreadLocal<RequestContext> BROADLEAF_REQUEST_CONTEXT = ThreadLocalManager.createThreadLocal(RequestContext.class);

    public static RequestContext getBroadleafRequestContext() {
        return BROADLEAF_REQUEST_CONTEXT.get();
    }

    public static void setBroadleafRequestContext(RequestContext requestContext) {
        BROADLEAF_REQUEST_CONTEXT.set(requestContext);
    }

    public static boolean hasLocale() {
        if (getBroadleafRequestContext() != null) {
            if (getBroadleafRequestContext().getLocale() != null) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasCurrency() {
        if (getBroadleafRequestContext() != null) {
        }
        return false;
    }

    protected HttpServletRequest  request;
    protected HttpServletResponse response;
    protected WebRequest          webRequest;
    protected Locale              locale;
    protected TimeZone            timeZone;
    protected Currency            javaCurrency;
    protected Map<String, Object> additionalProperties = new HashMap<String, Object>();
    protected MessageSource messageSource;
    protected RequestDTO    requestDTO;
    protected boolean isAdmin = false;
    protected Long adminUserId;

    protected boolean internalIgnoreFilters = false;

    /**
     * Gets the current request on the context
     *
     * @return
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * Sets the current request on the context. Note that this also invokes {@link #setWebRequest(WebRequest)} by wrapping
     * <b>request</b> in a {@link ServletWebRequest}.
     *
     * @param request
     */
    public void setRequest(HttpServletRequest request) {
        this.request = request;
        this.webRequest = new ServletWebRequest(request);
    }

    /**
     * Returns the response for the context
     *
     * @return
     */
    public HttpServletResponse getResponse() {
        return response;
    }

    /**
     * Sets the response on the context
     *
     * @param response
     */
    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    /**
     * Sets the generic request on the context. This is available to be used in non-Servlet environments (like Portlets).
     * Note that if <b>webRequest</b> is an instance of {@link ServletWebRequest} then
     * {@link #setRequest(HttpServletRequest)} will be invoked as well with the native underlying {@link HttpServletRequest}
     * passed as a parameter.
     * <br />
     * <br />
     * Also, if <b>webRequest</b> is an instance of {@link ServletWebRequest} then an attempt is made to set the response
     * (note that this could be null if the ServletWebRequest was not instantiated with both the {@link HttpServletRequest}
     * and {@link HttpServletResponse}
     *
     * @param webRequest
     */
    public void setWebRequest(WebRequest webRequest) {
        this.webRequest = webRequest;
        if (webRequest instanceof ServletWebRequest) {
            this.request = ((ServletWebRequest) webRequest).getRequest();
            setResponse(((ServletWebRequest) webRequest).getResponse());
        }
    }

    /**
     * Returns the generic request for use outside of servlets (like in Portlets). This will be automatically set
     * by invoking {@link #setRequest(HttpServletRequest)}
     *
     * @return the generic request
     * @see {@link #setWebRequest(WebRequest)}
     */
    public WebRequest getWebRequest() {
        return webRequest;
    }

    public Locale getLocale() {
        return locale;
    }

    /**
     * Returns the java.util.Currency constructed from the org.broadleafcommerce.common.currency.domain.BroadleafCurrency.
     * If there is no BroadleafCurrency specified this will return the currency based on the JVM locale
     *
     * @return
     */
    public Currency getJavaCurrency() {
        if (javaCurrency == null) {
            try {
                javaCurrency = Currency.getInstance(getLocale());
            } catch (IllegalArgumentException e) {
                LOG.warn("There was an error processing the configured locale into the java currency. This is likely because the default" +
                        " locale is set to something like 'en' (which is NOT apart of ISO 3166 and does not have a currency" +
                        " associated with it) instead of 'en_US' (which IS apart of ISO 3166 and has a currency associated" +
                        " with it). Because of this, the currency is now set to the default locale of the JVM");
                LOG.warn("To fully resolve this, update the default entry in the BLC_LOCALE table to take into account the" +
                        " country code as well as the language. Alternatively, you could also update the BLC_CURRENCY table" +
                        " to contain a default currency.");
                javaCurrency = Currency.getInstance(java.util.Locale.getDefault());
            }
        }
        return javaCurrency;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getRequestURIWithoutContext() {
        String requestURIWithoutContext = null;

        if (request != null && request.getRequestURI() != null) {
            if (request.getContextPath() != null) {
                requestURIWithoutContext = request.getRequestURI().substring(request.getContextPath().length());
            } else {
                requestURIWithoutContext = request.getRequestURI();
            }

            // Remove JSESSION-ID or other modifiers
            int pos = requestURIWithoutContext.indexOf(";");
            if (pos >= 0) {
                requestURIWithoutContext = requestURIWithoutContext.substring(0, pos);
            }
        }

        return requestURIWithoutContext;

    }

    public boolean isSecure() {
        boolean secure = false;
        if (request != null) {
            secure = ("HTTPS".equalsIgnoreCase(request.getScheme()) || request.isSecure());
        }
        return secure;
    }

    @SuppressWarnings("unchecked")
    public static Map<String, String[]> getRequestParameterMap() {
        return getBroadleafRequestContext().getRequest().getParameterMap();
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public RequestDTO getRequestDTO() {
        return requestDTO;
    }

    public void setRequestDTO(RequestDTO requestDTO) {
        this.requestDTO = requestDTO;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public boolean isInternalIgnoreFilters() {
        return internalIgnoreFilters;
    }

    public void setInternalIgnoreFilters(boolean internalIgnoreFilters) {
        this.internalIgnoreFilters = internalIgnoreFilters;
    }

    public Long getAdminUserId() {
        return adminUserId;
    }

    public void setAdminUserId(Long adminUserId) {
        this.adminUserId = adminUserId;
    }

    /**
     * Intended for internal use only
     */
    public Boolean getInternalIgnoreFilters() {
        return internalIgnoreFilters;
    }

    /**
     * In some cases, it is useful to utilize a clone of the context that does not include the actual container request
     * and response information. Such a case would be when executing an asynchronous operation on a new thread from
     * an existing request thread. That new thread may still require context information, in which case this lightweight
     * context is useful.
     *
     * @return The instance without the container request and response
     */
    public RequestContext createLightWeightClone() {
        RequestContext context = new RequestContext();
        context.setAdmin(isAdmin);
        context.setAdminUserId(adminUserId);
        context.setInternalIgnoreFilters(internalIgnoreFilters);
        context.setLocale(locale);
        context.setMessageSource(messageSource);
        context.setTimeZone(timeZone);
        //purposefully excluding additionalProperties - this contains state that can mess with SandBoxFilterEnabler (for one)

        return context;
    }
}
