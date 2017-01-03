package com.example.sandbox.filters;

import com.example.sandbox.constants.ApplicationConstants;
import com.example.sandbox.constants.PathConstants;
import com.example.sandbox.daos.CredentialDAO;
import com.example.sandbox.utilities.SessionUtil;
import com.example.sandbox.utilities.UserAgentUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 * Filter class to restrict access to resources without authentication
 */
public class SessionFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String userAgentType = UserAgentUtil.getUserAgentType(httpServletRequest);
        String authToken = SessionUtil.getAuthToken(httpServletRequest, userAgentType);
        HttpSession httpSession = httpServletRequest.getSession(true);
        httpSession.setAttribute(ApplicationConstants.AUTH_TOKEN, authToken);
        String httpAccept = httpServletRequest.getHeader(HttpHeaders.ACCEPT);
        boolean validAuthToken = CredentialDAO.validateAuthToken(authToken);
        if (!validAuthToken && !httpAccept.equalsIgnoreCase(MediaType.APPLICATION_JSON)) {
            httpServletResponse.sendRedirect(PathConstants.PATH_ROOT);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    @Override
    public void destroy() {

    }
}
