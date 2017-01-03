package com.example.sandbox.utilities;

import com.example.sandbox.constants.ApplicationConstants;
import com.example.sandbox.constants.SecurityConstants;
import com.example.sandbox.daos.CredentialDAO;
import com.example.sandbox.models.genericmodels.SessionVariables;
import org.json.simple.JSONObject;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.util.Enumeration;
import java.util.Map;

/**
 * Utility helper class for handling session based operations
 */
public class SessionUtil {
    public static String getAuthToken(HttpServletRequest httpServletRequest, String userAgentType) {
        String authToken = null;
        if (userAgentType.equals(SecurityConstants.WEB)) {
            Cookie[] cookies = httpServletRequest.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(ApplicationConstants.AUTH_TOKEN)) {
                        authToken = cookie.getValue();
                    }
                }
            }
        } else if (userAgentType.startsWith(SecurityConstants.APP)) {
            Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                if (headerNames.nextElement().equals(ApplicationConstants.AUTH_TOKEN)) {
                    authToken = httpServletRequest.getHeader(ApplicationConstants.AUTH_TOKEN);
                }
            }
        }
        if (authToken == null) {
            HttpSession httpSession = httpServletRequest.getSession(true);
            authToken = (String) httpSession.getAttribute(ApplicationConstants.AUTH_TOKEN);
        }

        return authToken;
    }

    public static String getAuthToken(ContainerRequestContext containerRequestContext) {
        String userAgentType = UserAgentUtil.getUserAgentType(containerRequestContext);
        String authToken = null;
        switch (userAgentType) {
            case SecurityConstants.WEB:
                Map<String, javax.ws.rs.core.Cookie> cookies = containerRequestContext.getCookies();
                if (cookies.containsKey(ApplicationConstants.AUTH_TOKEN)) {
                    authToken = cookies.get(ApplicationConstants.AUTH_TOKEN).getValue();
                }
                break;
            case SecurityConstants.APP_ANDROID:
            case SecurityConstants.APP_IOS:
                MultivaluedMap<String, String> headerNames = containerRequestContext.getHeaders();
                if (headerNames.containsKey(ApplicationConstants.AUTH_TOKEN)) {
                    authToken = headerNames.getFirst(ApplicationConstants.AUTH_TOKEN);
                }
                break;
        }
        return authToken;
    }

    public SessionVariables getSessionVariables(HttpServletRequest httpServletRequest) {
        SessionVariables sessionVariables = new SessionVariables();
        sessionVariables.setSessionId(httpServletRequest.getSession().getId());
        sessionVariables.setRequestIP(httpServletRequest.getRemoteAddr());
        String userAgentType = UserAgentUtil.getUserAgentType(httpServletRequest);
        sessionVariables.setHttpAccept(httpServletRequest.getHeader(SecurityConstants.ACCEPT));
        sessionVariables.setContentType(httpServletRequest.getHeader(SecurityConstants.CONTENT_TYPE));
        sessionVariables.setPath(httpServletRequest.getRequestURI());
        sessionVariables.setMethod(httpServletRequest.getMethod());
        sessionVariables.setUserAgentType(userAgentType);
        String authToken = getAuthToken(httpServletRequest, userAgentType);
        sessionVariables.setAuthToken(authToken);
        JSONObject userValidation = CredentialDAO.validateUser(authToken);
        if (userValidation.get(ApplicationConstants.VALID_AUTH_TOKEN) != null) {
            sessionVariables.setValidAuthToken((boolean) userValidation.get(ApplicationConstants.VALID_AUTH_TOKEN));
        }
        sessionVariables.setUserType((String) userValidation.get(ApplicationConstants.USER_TYPE));
        sessionVariables.setUserId((String) userValidation.get(ApplicationConstants.USER_ID));
        return sessionVariables;
    }

    public SessionVariables getSessionVariables(ServletRequest servletRequest) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        SessionVariables sessionVariables = new SessionVariables();
        sessionVariables.setSessionId(httpServletRequest.getSession().getId());
        sessionVariables.setRequestIP(httpServletRequest.getRemoteAddr());
        String userAgentType = UserAgentUtil.getUserAgentType(httpServletRequest);
        sessionVariables.setHttpAccept(httpServletRequest.getHeader(SecurityConstants.ACCEPT));
        sessionVariables.setContentType(httpServletRequest.getHeader(SecurityConstants.CONTENT_TYPE));
        sessionVariables.setPath(httpServletRequest.getRequestURI());
        sessionVariables.setMethod(httpServletRequest.getMethod());
        sessionVariables.setAuthToken(getAuthToken(httpServletRequest, userAgentType));
        sessionVariables.setUserAgentType(userAgentType);
        return sessionVariables;
    }

    public String getAuthTokenFromResponse(Response response) {
        MultivaluedMap<String, Object> responseHeaders = response.getHeaders();
        Map<String, NewCookie> responseCookies = response.getCookies();
        String authToken = null;
        if (responseHeaders.containsKey(ApplicationConstants.AUTH_TOKEN)) {
            authToken = response.getHeaderString(ApplicationConstants.AUTH_TOKEN);
        } else if (responseCookies.containsKey(ApplicationConstants.AUTH_TOKEN)) {
            authToken = responseCookies.get(ApplicationConstants.AUTH_TOKEN).getValue();
        }
        return authToken;
    }
}