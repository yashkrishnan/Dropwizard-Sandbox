package com.test.sandbox.resources;

import com.codahale.metrics.annotation.Timed;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.test.sandbox.constants.ApplicationConstants;
import com.test.sandbox.constants.GenericConstants;
import com.test.sandbox.constants.PathConstants;
import com.test.sandbox.constants.TextConstants;
import com.test.sandbox.daos.UserDAOImpl;
import com.test.sandbox.io.FileUtil;
import com.test.sandbox.models.genericmodels.SessionVariables;
import com.test.sandbox.models.genericmodels.responsemodels.MultipartUploadResponse;
import com.test.sandbox.utilities.SessionUtil;
import com.test.sandbox.utilities.TimeUtil;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * Resource class for multi path uploads
 */
@Path(PathConstants.PATH_ROOT)
public class MultipartUploadResource {
    private final Logger logger = LoggerFactory.getLogger(MultipartUploadResource.class);
    @Context
    HttpSession httpSession;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private FileUtil fileUtil = FileUtil.getInstance();
    @Context
    private HttpServletRequest httpServletRequest;
    @Context
    private HttpHeaders httpHeaders;

    public MultipartUploadResource() {

    }

    @PermitAll
    @Timed
    @POST
    @Path(PathConstants.PATH_UPLOAD_PROFILE_IMAGE)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProfileImage(@FormDataParam(GenericConstants.FILE) InputStream inputStream, @FormDataParam(GenericConstants.FILE) FormDataContentDisposition formDataContentDisposition) {
        SessionVariables sessionVariables = new SessionUtil().getSessionVariables(httpServletRequest);
        logger.debug(TextConstants.LOG_RESOURCE_WITHOUT_REQUEST, sessionVariables.getPath(), sessionVariables.getMethod(), sessionVariables.getSessionId(), sessionVariables.getRequestIP(), sessionVariables.getUserAgentType().toUpperCase(), sessionVariables.getHttpAccept(), sessionVariables.getContentType());
        String userAgentType = sessionVariables.getUserAgentType();
        String authToken = SessionUtil.getAuthToken(httpServletRequest, userAgentType);
        boolean validAuthToken = sessionVariables.isValidAuthToken();
        String filePath = PathConstants.PATH_IMAGES_PROFILE_IMAGES;
        MultipartUploadResponse multipartUploadResponse = fileUtil.multipartUploadHandler(inputStream, formDataContentDisposition, filePath);
        Response response;
        if (validAuthToken) {
            String imageURL = multipartUploadResponse.getUploadedFileLocation();
            UserDAOImpl.updateFileURL(authToken, imageURL);
            Date expiryDate = TimeUtil.getExpiryDate(1);
            logger.debug(TextConstants.LOG_RESPONSE_AUTH_TOKEN, gson.toJson(multipartUploadResponse), authToken);
            response = Response.ok(multipartUploadResponse).status(Response.Status.OK).expires(expiryDate).header(ApplicationConstants.AUTH_TOKEN, authToken).build();
        } else {
            logger.debug(TextConstants.LOG_RESPONSE, gson.toJson(multipartUploadResponse));
            response = Response.ok(multipartUploadResponse).status(Response.Status.OK).build();
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
            logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
        }
        return response;
    }
}