package com.test.sandbox.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.test.sandbox.constants.ApplicationConstants;
import com.test.sandbox.constants.PathConstants;
import com.test.sandbox.constants.TextConstants;
import com.test.sandbox.models.User;
import com.test.sandbox.models.Users;
import com.test.sandbox.models.genericmodels.SessionVariables;
import com.test.sandbox.resources.resourcehelpers.UserResourceHelper;
import com.test.sandbox.utilities.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 * Resource class for User based REST API end points
 */
@Path(PathConstants.PATH_ROOT)
public class UserResource {
    private final Logger logger = LoggerFactory.getLogger(UserResource.class);
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private UserResourceHelper userResourceHelper;
    private SessionUtil sessionUtil = new SessionUtil();

    @Context
    private HttpServletRequest httpServletRequest;
    @Context
    private HttpHeaders httpHeaders;
    @Context
    private UriInfo uriInfo;
    @Context
    private HttpSession httpSession;

    /**
     * Instantiates a new User resource.
     *
     * @param userResourceHelper the user resource helper
     * @param sessionUtil
     */
    public UserResource(UserResourceHelper userResourceHelper, SessionUtil sessionUtil) {
        this.userResourceHelper = userResourceHelper;
        this.sessionUtil = sessionUtil;
    }

    /**
     * Validate user name.
     *
     * @param user the user
     * @return the response
     */
    @Timed
    @POST
    @PermitAll
    @Path(PathConstants.PATH_VALIDATE_USERNAME)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateUserName(@Valid User user) {
        SessionVariables sessionVariables = sessionUtil.getSessionVariables(httpServletRequest);
        logger.debug(TextConstants.LOG_RESOURCE_WITH_REQUEST, sessionVariables.getPath(), sessionVariables.getMethod(), gson.toJson(user), sessionVariables.getSessionId(), sessionVariables.getRequestIP(), sessionVariables.getUserAgentType().toUpperCase(), sessionVariables.getHttpAccept(), sessionVariables.getContentType());
        Response response = userResourceHelper.validateUsername(user);
        String authToken = sessionUtil.getAuthTokenFromResponse(response);
        logger.debug(TextConstants.LOG_RESPONSE_AUTH_TOKEN, gson.toJson(response.getEntity()), authToken);
        return response;
    }


    /**
     * Sign up user.
     *
     * @param user the user
     * @return the response
     */
    @Timed
    @PUT
    @PermitAll
    @Path(PathConstants.PATH_SIGNUP)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signUpUser(@Valid User user) {
        SessionVariables sessionVariables = sessionUtil.getSessionVariables(httpServletRequest);
        logger.debug(TextConstants.LOG_RESOURCE_WITH_REQUEST, sessionVariables.getPath(), sessionVariables.getMethod(), gson.toJson(user), sessionVariables.getSessionId(), sessionVariables.getRequestIP(), sessionVariables.getUserAgentType().toUpperCase(), sessionVariables.getHttpAccept(), sessionVariables.getContentType());
        Response response = userResourceHelper.signUpUser(user, sessionVariables);
        String authToken = sessionUtil.getAuthTokenFromResponse(response);
        logger.debug(TextConstants.LOG_RESPONSE_AUTH_TOKEN, gson.toJson(response.getEntity()), authToken);
        httpSession = httpServletRequest.getSession(true);
        httpSession.setAttribute(ApplicationConstants.AUTH_TOKEN, authToken);
        httpSession.setMaxInactiveInterval(60000);
        return response;
    }

    /**
     * Fetch profile.
     *
     * @return the response
     */
    @Timed
    @GET
    @Path(PathConstants.PATH_USER_FETCH_PROFILE)
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchProfile() {
        SessionVariables sessionVariables = sessionUtil.getSessionVariables(httpServletRequest);
        logger.debug(TextConstants.LOG_RESOURCE_WITHOUT_REQUEST, sessionVariables.getPath(), sessionVariables.getMethod(), sessionVariables.getSessionId(), sessionVariables.getRequestIP(), sessionVariables.getUserAgentType().toUpperCase(), sessionVariables.getHttpAccept(), sessionVariables.getContentType());
        // Transfer to resource helper class for perform manipulations
        Response response = userResourceHelper.fetchProfile(sessionVariables);
        String authToken = sessionUtil.getAuthTokenFromResponse(response);
        logger.debug(TextConstants.LOG_RESPONSE_AUTH_TOKEN, gson.toJson(response.getEntity()), authToken);
        return response;
    }

    /**
     * Fetch profile.
     *
     * @param user the user
     * @return the response
     */
    @Timed
    @POST
    @Path(PathConstants.PATH_USER_FETCH_PROFILE)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchProfile(User user) {
        SessionVariables sessionVariables = sessionUtil.getSessionVariables(httpServletRequest);
        logger.debug(TextConstants.LOG_RESOURCE_WITHOUT_REQUEST, sessionVariables.getPath(), sessionVariables.getMethod(), sessionVariables.getSessionId(), sessionVariables.getRequestIP(), sessionVariables.getUserAgentType().toUpperCase(), sessionVariables.getHttpAccept(), sessionVariables.getContentType());
        // Transfer to resource helper class for perform manipulations
        Response response = userResourceHelper.fetchProfile(user, sessionVariables);
        String authToken = sessionUtil.getAuthTokenFromResponse(response);
        logger.debug(TextConstants.LOG_RESPONSE_AUTH_TOKEN, gson.toJson(response.getEntity()), authToken);
        return response;
    }

    /**
     * Update profile.
     *
     * @param user the user
     * @return the response
     */
    @Timed
    @POST
    @Path(PathConstants.PATH_USER_UPDATE_PROFILE)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProfile(@Valid User user) {
        SessionVariables sessionVariables = sessionUtil.getSessionVariables(httpServletRequest);
        logger.debug(TextConstants.LOG_RESOURCE_WITH_REQUEST, sessionVariables.getPath(), sessionVariables.getMethod(), gson.toJson(user), sessionVariables.getSessionId(), sessionVariables.getRequestIP(), sessionVariables.getUserAgentType().toUpperCase(), sessionVariables.getHttpAccept(), sessionVariables.getContentType());
        Response response = userResourceHelper.updateProfile(user, sessionVariables);
        String authToken = sessionUtil.getAuthTokenFromResponse(response);
        logger.debug(TextConstants.LOG_RESPONSE_AUTH_TOKEN, gson.toJson(response.getEntity()), authToken);
        return response;
    }

    /**
     * Create user.
     *
     * @param user the user
     * @return the response
     */
    @Timed
    @POST
    @Path(PathConstants.PATH_USER_CREATE_USER)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(@Valid User user) {
        SessionVariables sessionVariables = sessionUtil.getSessionVariables(httpServletRequest);
        logger.debug(TextConstants.LOG_RESOURCE_WITH_REQUEST, sessionVariables.getPath(), sessionVariables.getMethod(), gson.toJson(user), sessionVariables.getSessionId(), sessionVariables.getRequestIP(), sessionVariables.getUserAgentType().toUpperCase(), sessionVariables.getHttpAccept(), sessionVariables.getContentType());
        Response response = userResourceHelper.createUser(user, sessionVariables);
        String authToken = sessionUtil.getAuthTokenFromResponse(response);
        logger.debug(TextConstants.LOG_RESPONSE_AUTH_TOKEN, gson.toJson(response.getEntity()), authToken);
        return response;
    }

    /**
     * Read user.
     *
     * @param user the user
     * @return the response
     */
    @Timed
    @POST
    @Path(PathConstants.PATH_USER_READ_USER)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response readUser(@Valid User user) {
        SessionVariables sessionVariables = sessionUtil.getSessionVariables(httpServletRequest);
        logger.debug(TextConstants.LOG_RESOURCE_WITH_REQUEST, sessionVariables.getPath(), sessionVariables.getMethod(), gson.toJson(user), sessionVariables.getSessionId(), sessionVariables.getRequestIP(), sessionVariables.getUserAgentType().toUpperCase(), sessionVariables.getHttpAccept(), sessionVariables.getContentType());
        Response response = userResourceHelper.readUser(user, sessionVariables);
        String authToken = sessionUtil.getAuthTokenFromResponse(response);
        logger.debug(TextConstants.LOG_RESPONSE_AUTH_TOKEN, gson.toJson(response.getEntity()), authToken);
        return response;
    }

    /**
     * Update user.
     *
     * @param user the user
     * @return the response
     */
    @Timed
    @POST
    @Path(PathConstants.PATH_USER_UPDATE_USER)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@Valid User user) {
        SessionVariables sessionVariables = sessionUtil.getSessionVariables(httpServletRequest);
        logger.debug(TextConstants.LOG_RESOURCE_WITH_REQUEST, sessionVariables.getPath(), sessionVariables.getMethod(), gson.toJson(user), sessionVariables.getSessionId(), sessionVariables.getRequestIP(), sessionVariables.getUserAgentType().toUpperCase(), sessionVariables.getHttpAccept(), sessionVariables.getContentType());
        Response response = userResourceHelper.updateUser(user, sessionVariables);
        String authToken = sessionUtil.getAuthTokenFromResponse(response);
        logger.debug(TextConstants.LOG_RESPONSE_AUTH_TOKEN, gson.toJson(response.getEntity()), authToken);
        return response;
    }

    /**
     * Delete user.
     *
     * @param user the user
     * @return the response
     */
    @Timed
    @DELETE
    @Path(PathConstants.PATH_USER_DELETE_USER)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@Valid User user) {
        SessionVariables sessionVariables = sessionUtil.getSessionVariables(httpServletRequest);
        logger.debug(TextConstants.LOG_RESOURCE_WITH_REQUEST, sessionVariables.getPath(), sessionVariables.getMethod(), gson.toJson(user), sessionVariables.getSessionId(), sessionVariables.getRequestIP(), sessionVariables.getUserAgentType().toUpperCase(), sessionVariables.getHttpAccept(), sessionVariables.getContentType());
        Response response = userResourceHelper.deleteUser(user, sessionVariables);
        String authToken = sessionUtil.getAuthTokenFromResponse(response);
        logger.debug(TextConstants.LOG_RESPONSE_AUTH_TOKEN, gson.toJson(response.getEntity()), authToken);
        return response;
    }

    /**
     * Delete users.
     *
     * @param users the users
     * @return the response
     */
    @Timed
    @DELETE
    @Path(PathConstants.PATH_USER_DELETE_USERS)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUsers(@Valid Users users) {
        SessionVariables sessionVariables = sessionUtil.getSessionVariables(httpServletRequest);
        logger.debug(TextConstants.LOG_RESOURCE_WITH_REQUEST, sessionVariables.getPath(), sessionVariables.getMethod(), gson.toJson(users), sessionVariables.getSessionId(), sessionVariables.getRequestIP(), sessionVariables.getUserAgentType().toUpperCase(), sessionVariables.getHttpAccept(), sessionVariables.getContentType());
        Response response = userResourceHelper.deleteUsers(users, sessionVariables);
        String authToken = sessionUtil.getAuthTokenFromResponse(response);
        logger.debug(TextConstants.LOG_RESPONSE_AUTH_TOKEN, gson.toJson(response.getEntity()), authToken);
        return response;
    }

    /**
     * List users.
     *
     * @return the response
     */
    @Timed
    @GET
    @Path(PathConstants.PATH_USER_LIST_USERS)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response listUsers() {
        SessionVariables sessionVariables = sessionUtil.getSessionVariables(httpServletRequest);
        logger.debug(TextConstants.LOG_RESOURCE_WITHOUT_REQUEST, sessionVariables.getPath(), sessionVariables.getMethod(), sessionVariables.getSessionId(), sessionVariables.getRequestIP(), sessionVariables.getUserAgentType().toUpperCase(), sessionVariables.getHttpAccept(), sessionVariables.getContentType());
        Response response = userResourceHelper.listUsers(uriInfo, sessionVariables);
        String authToken = sessionUtil.getAuthTokenFromResponse(response);
        logger.debug(TextConstants.LOG_RESPONSE_AUTH_TOKEN, gson.toJson(response.getEntity()), authToken);
        return response;
    }

    /**
     * List users.
     *
     * @param users the users
     * @return the response
     */
    @Timed
    @POST
    @Path(PathConstants.PATH_USER_LIST_USERS)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response listUsers(Users users) {
        SessionVariables sessionVariables = sessionUtil.getSessionVariables(httpServletRequest);
        logger.debug(TextConstants.LOG_RESOURCE_WITH_REQUEST, sessionVariables.getPath(), sessionVariables.getMethod(), gson.toJson(users), sessionVariables.getSessionId(), sessionVariables.getRequestIP(), sessionVariables.getUserAgentType().toUpperCase(), sessionVariables.getHttpAccept(), sessionVariables.getContentType());
        Response response = userResourceHelper.listUsers(users, sessionVariables);
        String authToken = sessionUtil.getAuthTokenFromResponse(response);
        logger.debug(TextConstants.LOG_RESPONSE_AUTH_TOKEN, gson.toJson(response.getEntity()), authToken);
        return response;
    }

    /**
     * List latest users.
     *
     * @return the response
     */
    @Timed
    @GET
    @Path(PathConstants.PATH_USER_LIST_LATEST_USERS)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response listLatestUsers() {
        SessionVariables sessionVariables = sessionUtil.getSessionVariables(httpServletRequest);
        logger.debug(TextConstants.LOG_RESOURCE_WITHOUT_REQUEST, sessionVariables.getPath(), sessionVariables.getMethod(), sessionVariables.getSessionId(), sessionVariables.getRequestIP(), sessionVariables.getUserAgentType().toUpperCase(), sessionVariables.getHttpAccept(), sessionVariables.getContentType());
        Response response = userResourceHelper.listLatestUsers(uriInfo, sessionVariables);
        String authToken = sessionUtil.getAuthTokenFromResponse(response);
        logger.debug(TextConstants.LOG_RESPONSE_AUTH_TOKEN, gson.toJson(response.getEntity()), authToken);
        return response;
    }
}