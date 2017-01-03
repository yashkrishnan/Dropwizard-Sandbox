package com.example.sandbox.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 * Filter class to restrict access valid resources
 */
public class ResourceFilter implements Filter {

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        // Get request url from httpServletRequest
        String url = httpServletRequest.getRequestURL().toString();
        if (url.endsWith("/set-session")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } else if (url.endsWith("/get-session")) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } else {
            httpServletResponse.setStatus(404);
            httpServletResponse.setCharacterEncoding("utf-8");
            httpServletResponse.setContentType(MediaType.TEXT_HTML);
            httpServletResponse.getWriter().print("PAGE NOT FOUND");
        }
    }

    public void destroy() {

    }
}
