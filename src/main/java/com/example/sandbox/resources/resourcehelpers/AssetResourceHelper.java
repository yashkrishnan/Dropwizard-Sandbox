package com.example.sandbox.resources.resourcehelpers;

import com.example.sandbox.constants.GenericConstants;
import com.example.sandbox.constants.PathConstants;
import com.example.sandbox.constants.TextConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * Helper class for AssetResource class
 */
public class AssetResourceHelper {

    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    public Response getIndex() {
        URI indexPathUri;
        Response response;
        try {
            indexPathUri = new URI(PathConstants.INDEX_PATH);
            response = Response.temporaryRedirect(indexPathUri).build();
        } catch (Exception e) {
            logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
            logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
            response = Response.serverError().entity(e).build();
        }
        return response;
    }

    public Response getAsset(String webDir, String resourceDir, String target, UriInfo uriInfo, HttpServletResponse httpServletResponse) {

        File webFile = new File(webDir, uriInfo.getPath());
        File resFile = new File(resourceDir, uriInfo.getPath());

        if (webFile.exists() && webFile.isFile()) {
            try {
                OutputStream out = httpServletResponse.getOutputStream();
                InputStream in = new FileInputStream(webFile);
                byte buf[] = new byte[1024];
                int read = 0;
                while ((read = in.read(buf)) >= 0) {
                    out.write(buf, 0, read);
                }
                out.flush();
                if (target.toLowerCase().endsWith(GenericConstants.JS.toLowerCase())) {
                    httpServletResponse.setContentType(GenericConstants.APPLICATION_JAVASCRIPT);
                }
                return Response.ok().build();
            } catch (Exception e) {
                logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
                logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
                return Response.serverError().build();
            }
        } else if (resFile.exists() && resFile.isFile()) {
            try {
                OutputStream out = httpServletResponse.getOutputStream();
                InputStream in = new FileInputStream(resFile);
                byte buf[] = new byte[1024];
                int read = 0;
                while ((read = in.read(buf)) >= 0) {
                    out.write(buf, 0, read);
                }
                out.flush();
                String contentType = getAssetContentType(target);
                httpServletResponse.setContentType(contentType);
                return Response.ok().build();
            } catch (Exception e) {
                logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
                logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
                return Response.serverError().build();
            }
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    public String getAssetContentType(String target) {
        String contentType = MediaType.WILDCARD;
        if (target.toLowerCase().endsWith(GenericConstants.JPG.toLowerCase())) {
            contentType = GenericConstants.IMAGE_JPEG;
        } else if (target.toLowerCase().endsWith(GenericConstants.JPEG.toLowerCase())) {
            contentType = GenericConstants.IMAGE_JPEG;
        } else if (target.toLowerCase().endsWith(GenericConstants.JSON.toLowerCase())) {
            contentType = MediaType.APPLICATION_JSON;
        } else if (target.toLowerCase().endsWith(GenericConstants.PNG.toLowerCase())) {
            contentType = GenericConstants.IMAGE_PNG;
        } else if (target.toLowerCase().endsWith(GenericConstants.GIF.toLowerCase())) {
            contentType = GenericConstants.IMAGE_GIF;
        } else if (target.toLowerCase().endsWith(GenericConstants.BMP.toLowerCase())) {
            contentType = GenericConstants.IMAGE_BMP;
        } else if (target.toLowerCase().endsWith(GenericConstants.MP4.toLowerCase())) {
            contentType = GenericConstants.VIDEO_MP4;
        } else if (target.toLowerCase().endsWith(GenericConstants.WEBP.toLowerCase())) {
            contentType = GenericConstants.IMAGE_WEBP;
        } else if (target.toLowerCase().endsWith(GenericConstants.MP4.toLowerCase())) {
            contentType = GenericConstants.VIDEO_MP4;
        } else if (target.toLowerCase().endsWith(GenericConstants.THREEGP.toLowerCase())) {
            contentType = GenericConstants.VIDEO_3GPP;
        } else if (target.toLowerCase().endsWith(GenericConstants.MOV.toLowerCase())) {
            contentType = GenericConstants.VIDEO_QUICKTIME;
        } else if (target.toLowerCase().endsWith(GenericConstants.MKV.toLowerCase())) {
            contentType = GenericConstants.VIDEO_X_MATROSKA;
        } else if (target.toLowerCase().endsWith(GenericConstants.WEBM.toLowerCase())) {
            contentType = GenericConstants.VIDEO_WEBM;
        } else if (target.toLowerCase().endsWith(GenericConstants.PDF.toLowerCase())) {
            contentType = GenericConstants.APPLICATION_PDF;
        }
        return contentType;
    }
}
