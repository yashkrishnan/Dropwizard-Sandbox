package com.test.sandbox.securities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Custom exception handler for handling the runtime errors.
 */
@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Override
    public Response toResponse(RuntimeException runtimeException) {
        Response response = Response.status(runtimeException.hashCode()).build();
        if (runtimeException instanceof WebApplicationException) {
            WebApplicationException webApplicationException = (WebApplicationException) runtimeException;
            int statusCode = webApplicationException.getResponse().getStatus();
            response = Response.status(statusCode).entity(webApplicationException.getResponse().getEntity()).build();
        }
        return response;
    }
}
