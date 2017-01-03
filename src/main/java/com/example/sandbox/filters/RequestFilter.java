package com.example.sandbox.filters;

import com.example.sandbox.constants.PathConstants;
import com.example.sandbox.utilities.SessionUtil;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Filter class to get requests with valid authentications associated in the cookie/header
 */
public class RequestFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String path = containerRequestContext.getUriInfo().getPath();
        String method = containerRequestContext.getMethod();
        if (path.startsWith(PathConstants.PATH_FILTER_USER_ROOT) && !method.equalsIgnoreCase(HttpMethod.OPTIONS)) {
            String authToken = SessionUtil.getAuthToken(containerRequestContext);
            if (authToken == null) {
                Response response = Response.ok().status(Response.Status.FORBIDDEN).build();
                containerRequestContext.abortWith(response);
            }
        }
    }
}