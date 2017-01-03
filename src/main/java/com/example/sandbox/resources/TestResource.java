package com.example.sandbox.resources;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * Created by dxuser on 4/1/16.
 */
@Path("/")
public class TestResource {

    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Context
    private HttpServletRequest httpServletRequest;
    @Context
    private HttpServletResponse httpServletResponse;
    @Context
    private HttpHeaders httpHeaders;
    @Context
    private UriInfo uriInfo;
    @Context
    private HttpSession httpSession;

    @Timed
    @POST
    @PermitAll
    @Path("/set-session")
    public Response setSession() {
        httpSession = httpServletRequest.getSession(true);
        httpSession.setAttribute("attrib", "value");
        logger.info("\nSession ID : " + httpSession.getId());
        httpSession.setMaxInactiveInterval(100000);
        return Response.ok(httpSession.getId()).build();
    }

    @Timed
    @POST
    @PermitAll
    @Path("/get-session")
    public Response getSession() {
        httpSession = httpServletRequest.getSession(true);
        logger.info("\nAttribute : " + httpSession.getAttribute("attrib"));
        logger.info("\nSession ID : " + httpSession.getId());
        httpSession.setMaxInactiveInterval(100000);
        return Response.ok(httpSession.getId()).build();
    }

}
