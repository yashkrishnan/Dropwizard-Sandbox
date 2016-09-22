package com.test.sandbox.filters;

import com.test.sandbox.constants.TextConstants;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CORSFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(CORSFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        response.setHeader(CrossOriginFilter.ACCESS_CONTROL_ALLOW_METHODS_HEADER, "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        response.setHeader(CrossOriginFilter.ACCESS_CONTROL_EXPOSE_HEADERS_HEADER, CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER);
        response.setHeader(CrossOriginFilter.ACCESS_CONTROL_MAX_AGE_HEADER, "3600");
        response.setHeader(CrossOriginFilter.ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER, "true");
        response.setHeader(CrossOriginFilter.ACCESS_CONTROL_ALLOW_HEADERS_HEADER, "Content-Type, AuthToken, Content-Disposition, Accept, " +
                "Pragma, Origin, Cache-Control, Access-Control-Allow-Headers, Authorization, Accept-Encoding, " +
                "Accept-Charset, Accept-Language, Cookie, Set-Cookie, User-Agent, Expires, Content-Length, " +
                "Last-Modified, X-Requested-With, X-Prototype-Version");
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (IOException | ServletException e) {
            logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
            logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
        }
    }

    @Override
    public void destroy() {
    }
}
