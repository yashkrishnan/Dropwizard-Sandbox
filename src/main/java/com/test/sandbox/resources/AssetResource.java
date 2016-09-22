package com.test.sandbox.resources;

import com.codahale.metrics.annotation.Timed;
import com.test.sandbox.constants.PathConstants;
import com.test.sandbox.constants.TextConstants;
import com.test.sandbox.models.genericmodels.SessionVariables;
import com.test.sandbox.resources.resourcehelpers.AssetResourceHelper;
import com.test.sandbox.utilities.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

/**
 * Resource class to serve HTML, CSS, JS, IMG, and any other files that are in spawn's web directory.
 */
@Path(PathConstants.PATH_ROOT)
public class AssetResource {

    private final Logger logger = LoggerFactory.getLogger(AssetResource.class);
    private String webDir;
    private String resourceDir;
    private AssetResourceHelper assetResourceHelper;
    private SessionUtil sessionUtil;

    @Context
    private HttpServletRequest httpServletRequest;
    @Context
    private HttpServletResponse httpServletResponse;
    @Context
    private HttpHeaders httpHeaders;
    @Context
    private UriInfo uriInfo;

    public AssetResource(String webDir, String resourceDir, AssetResourceHelper assetResourceHelper, SessionUtil sessionUtil) {
        this.webDir = webDir;
        this.resourceDir = resourceDir;
        this.assetResourceHelper = assetResourceHelper;
        this.sessionUtil = sessionUtil;
    }

    /*@GET
    public Response getIndex() {
        SessionVariables sessionVariables = new SessionUtil().getSessionVariables(httpServletRequest);
        logger.debug(TextConstants.LOG_RESOURCE_WITHOUT_REQUEST, sessionVariables.getPath(), sessionVariables.getMethod(), sessionVariables.getSessionId(), sessionVariables.getRequestIP(), sessionVariables.getUserAgentType().toUpperCase(), sessionVariables.getHttpAccept(), sessionVariables.getContentType());
        return assetResourceHelper.getIndex();
    }*/

    @GET
    @Path(PathConstants.PATH_TARGET)
    @Produces(MediaType.WILDCARD)
    public Response getAsset(@PathParam(PathConstants.TARGET) String target) {
        return assetResourceHelper.getAsset(webDir, resourceDir, target, uriInfo, httpServletResponse);
    }

    /**
     * Preflight Request Handler.
     *
     * @return the response
     */
    @Timed
    @OPTIONS
    @Path(PathConstants.PATH_TARGET)
    @Consumes(MediaType.WILDCARD)
    @Produces(MediaType.WILDCARD)
    public Response preflightHandler() {
        SessionVariables sessionVariables = sessionUtil.getSessionVariables(httpServletRequest);
        logger.debug(TextConstants.LOG_RESOURCE_WITHOUT_REQUEST, sessionVariables.getPath(), sessionVariables.getMethod(), sessionVariables.getSessionId(), sessionVariables.getRequestIP(), sessionVariables.getUserAgentType().toUpperCase(), sessionVariables.getHttpAccept(), sessionVariables.getContentType());
        Response response = Response.ok().status(Response.Status.OK).build();
        logger.debug(TextConstants.LOG_RESPONSE, response.getStatus());
        return response;
    }
}