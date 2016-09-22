package com.test.sandbox.resources;

import com.test.sandbox.constants.ApplicationConstants;
import com.test.sandbox.constants.PathConstants;
import com.test.sandbox.constants.TextConstants;
import com.test.sandbox.models.User;
import com.test.sandbox.models.genericmodels.SessionVariables;
import com.test.sandbox.resources.resourcehelpers.CredentialResourceHelper;
import com.test.sandbox.utilities.SessionUtil;
import com.codahale.metrics.annotation.Timed;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 * Resource class for Credential based REST API end points
 */
@Path(PathConstants.PATH_ROOT)
public class CredentialResource {
    private final Logger logger = LoggerFactory.getLogger(CredentialResource.class);
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private SessionUtil sessionUtil;
    private CredentialResourceHelper credentialResourceHelper;

    @Context
    private HttpServletRequest httpServletRequest;
    @Context
    private HttpHeaders httpHeaders;
    @Context
    private UriInfo uriInfo;
    @Context
    private HttpSession httpSession;

    public CredentialResource(CredentialResourceHelper credentialResourceHelper, SessionUtil sessionUtil) {
        this.credentialResourceHelper = credentialResourceHelper;
        this.sessionUtil = sessionUtil;
    }

    @Timed
    @GET
    @PermitAll
    @Path(PathConstants.PATH_VALIDATE_AUTH_TOKEN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateAuthToken() {
        SessionVariables sessionVariables = sessionUtil.getSessionVariables(httpServletRequest);
        logger.debug(TextConstants.LOG_RESOURCE_WITHOUT_REQUEST, sessionVariables.getPath(), sessionVariables.getMethod(), sessionVariables.getSessionId(), sessionVariables.getRequestIP(), sessionVariables.getUserAgentType().toUpperCase(), sessionVariables.getHttpAccept(), sessionVariables.getContentType());
        Response response = credentialResourceHelper.validateAuthToken(uriInfo, sessionVariables);
        String authToken = sessionUtil.getAuthTokenFromResponse(response);
        logger.debug(TextConstants.LOG_RESPONSE_AUTH_TOKEN, gson.toJson(response.getEntity()), authToken);
        httpSession = httpServletRequest.getSession(true);
        httpSession.setMaxInactiveInterval(60000);
        httpSession.setAttribute(ApplicationConstants.AUTH_TOKEN, authToken);
        return response;
    }

    @Timed
    @POST
    @PermitAll
    @Path(PathConstants.PATH_SIGNIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signInUser(@Valid User user) {
        SessionVariables sessionVariables = sessionUtil.getSessionVariables(httpServletRequest);
        logger.debug(TextConstants.LOG_RESOURCE_WITH_REQUEST, sessionVariables.getPath(), sessionVariables.getMethod(), gson.toJson(user), sessionVariables.getSessionId(), sessionVariables.getRequestIP(), sessionVariables.getUserAgentType().toUpperCase(), sessionVariables.getHttpAccept(), sessionVariables.getContentType());
        Response response = credentialResourceHelper.signInUser(user, sessionVariables);
        String authToken = sessionUtil.getAuthTokenFromResponse(response);
        logger.debug(TextConstants.LOG_RESPONSE_AUTH_TOKEN, gson.toJson(response.getEntity()), authToken);
        httpSession = httpServletRequest.getSession(true);
        httpSession.setAttribute(ApplicationConstants.AUTH_TOKEN, authToken);
        httpSession.setMaxInactiveInterval(60000);
        return response;
    }

    @Timed
    @POST
    @PermitAll
    @Path(PathConstants.PATH_FORGOT_PASSWORD)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response forgotPassword(@Valid User user) {
        SessionVariables sessionVariables = sessionUtil.getSessionVariables(httpServletRequest);
        logger.debug(TextConstants.LOG_RESOURCE_WITH_REQUEST, sessionVariables.getPath(), sessionVariables.getMethod(), gson.toJson(user), sessionVariables.getSessionId(), sessionVariables.getRequestIP(), sessionVariables.getUserAgentType().toUpperCase(), sessionVariables.getHttpAccept(), sessionVariables.getContentType());
        Response response = credentialResourceHelper.forgotPassword(user, sessionVariables);
        logger.debug(TextConstants.LOG_RESPONSE, gson.toJson(response.getEntity()));
        return response;
    }

    @Timed
    @GET
    @PermitAll
    @Path(PathConstants.PATH_RESET_PASSWORD)
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.TEXT_HTML)
    public Response getResetPasswordPage(@Context UriInfo uriInfo) {
        SessionVariables sessionVariables = sessionUtil.getSessionVariables(httpServletRequest);
        logger.debug(TextConstants.LOG_RESOURCE_WITHOUT_REQUEST, sessionVariables.getPath(), sessionVariables.getMethod(), sessionVariables.getSessionId(), sessionVariables.getRequestIP(), sessionVariables.getUserAgentType().toUpperCase(), sessionVariables.getHttpAccept(), sessionVariables.getContentType());
        Response response = credentialResourceHelper.getResetPasswordPage(uriInfo);
        logger.debug(TextConstants.LOG_RESPONSE, response.getStatus());
        return response;
    }

    @Timed
    @POST
    @PermitAll
    @Path(PathConstants.PATH_RESET_PASSWORD)
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.TEXT_HTML)
    public Response resetPassword(@FormParam(ApplicationConstants.EMAIL) String email, @FormParam(ApplicationConstants.PASSWORD) String password) {
        SessionVariables sessionVariables = sessionUtil.getSessionVariables(httpServletRequest);
        logger.debug(TextConstants.LOG_RESOURCE_WITHOUT_REQUEST, sessionVariables.getPath(), sessionVariables.getMethod(), sessionVariables.getSessionId(), sessionVariables.getRequestIP(), sessionVariables.getUserAgentType().toUpperCase(), sessionVariables.getHttpAccept(), sessionVariables.getContentType());
        Response response = credentialResourceHelper.resetPassword(uriInfo, email, password);
        logger.debug(TextConstants.LOG_RESPONSE, response.getStatus());
        return response;
    }

    @Timed
    @POST
    @PermitAll
    @Path(PathConstants.PATH_USER_UPDATE_PASSWORD)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePassword(@Valid User user) {
        SessionVariables sessionVariables = sessionUtil.getSessionVariables(httpServletRequest);
        logger.debug(TextConstants.LOG_RESOURCE_WITH_REQUEST, sessionVariables.getPath(), sessionVariables.getMethod(), gson.toJson(user), sessionVariables.getSessionId(), sessionVariables.getRequestIP(), sessionVariables.getUserAgentType().toUpperCase(), sessionVariables.getHttpAccept(), sessionVariables.getContentType());
        Response response = credentialResourceHelper.updatePassword(user, sessionVariables);
        String authToken = sessionUtil.getAuthTokenFromResponse(response);
        logger.debug(TextConstants.LOG_RESPONSE_AUTH_TOKEN, gson.toJson(response.getEntity()), authToken);
        return response;
    }

    @Timed
    @GET
    @PermitAll
    @Path(PathConstants.PATH_LOG_OUT)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @io.dropwizard.jersey.caching.CacheControl(noCache = true)
    public Response logoutUser() {
        SessionVariables sessionVariables = sessionUtil.getSessionVariables(httpServletRequest);
        logger.debug(TextConstants.LOG_RESOURCE_WITHOUT_REQUEST, sessionVariables.getPath(), sessionVariables.getMethod(), sessionVariables.getSessionId(), sessionVariables.getRequestIP(), sessionVariables.getUserAgentType().toUpperCase(), sessionVariables.getHttpAccept(), sessionVariables.getContentType());
        Response response = credentialResourceHelper.logoutUser(sessionVariables);
        String authToken = sessionUtil.getAuthTokenFromResponse(response);
        httpSession = httpServletRequest.getSession(true);
        httpSession.invalidate();
        logger.debug(TextConstants.LOG_RESPONSE_AUTH_TOKEN, gson.toJson(response.getEntity()), authToken);
        return response;
    }
}