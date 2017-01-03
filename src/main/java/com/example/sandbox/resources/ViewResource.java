package com.example.sandbox.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.sandbox.constants.PathConstants;
import com.example.sandbox.models.genericmodels.SessionVariables;
import com.example.sandbox.resources.resourcehelpers.ViewResourceHelper;
import com.example.sandbox.utilities.SessionUtil;
import io.dropwizard.jersey.caching.CacheControl;
import io.dropwizard.views.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

@Path(PathConstants.PATH_ROOT)
public class ViewResource {

    private final Logger logger = LoggerFactory.getLogger(UserResource.class);
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private ViewResourceHelper viewResourceHelper;
    private SessionUtil sessionUtil = new SessionUtil();


    @Context
    private HttpServletRequest httpServletRequest;
    @Context
    private HttpHeaders httpHeaders;
    @Context
    private UriInfo uriInfo;
    @Context
    private HttpSession httpSession;

    public ViewResource(ViewResourceHelper viewResourceHelper, SessionUtil sessionUtil) {
        this.viewResourceHelper = viewResourceHelper;
        this.sessionUtil = sessionUtil;
    }

    @Timed
    @GET
    @PermitAll
    @CacheControl(noCache = true)
    public View getIndex() {
        SessionVariables sessionVariables = sessionUtil.getSessionVariables(httpServletRequest);
        View view = viewResourceHelper.getIndex(sessionVariables);
        return view;
    }
}
