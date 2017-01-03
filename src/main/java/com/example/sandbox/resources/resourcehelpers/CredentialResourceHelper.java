package com.example.sandbox.resources.resourcehelpers;


import com.example.sandbox.constants.ApplicationConstants;
import com.example.sandbox.constants.MarkupConstants;
import com.example.sandbox.constants.SecurityConstants;
import com.example.sandbox.constants.StatusConstants;
import com.example.sandbox.daos.CredentialDAO;
import com.example.sandbox.daos.UserDAOImpl;
import com.example.sandbox.models.User;
import com.example.sandbox.models.genericmodels.SessionVariables;
import com.example.sandbox.models.genericmodels.responsemodels.BaseResponse;
import com.example.sandbox.securities.SecurityUtil;
import com.example.sandbox.utilities.BeanUtil;
import com.example.sandbox.utilities.EmailUtil;
import com.example.sandbox.utilities.TimeUtil;
import org.json.simple.JSONObject;

import javax.ws.rs.core.*;
import java.util.Date;
import java.util.List;

/**
 * Helper class for CredentialResource class
 */
public class CredentialResourceHelper {

    private CredentialDAO credentialDAO;

    public CredentialResourceHelper(CredentialDAO credentialDAO) {
        this.credentialDAO = credentialDAO;
    }

    public Response updatePassword(User user, SessionVariables sessionVariables) {
        String authToken = sessionVariables.getAuthToken();
        boolean validAuthToken = sessionVariables.isValidAuthToken();
        BaseResponse updatePasswordResponse = new BaseResponse();
        Response response;
        if (validAuthToken) {
            updatePasswordResponse = credentialDAO.updatePassword(user, authToken);
            updatePasswordResponse.setUserType(sessionVariables.getUserType());
            Date expiryDate = TimeUtil.getExpiryDate(1);
            response = Response.ok(updatePasswordResponse).status(Response.Status.OK).expires(expiryDate).header(ApplicationConstants.AUTH_TOKEN, authToken).build();
        } else {
            updatePasswordResponse.setUserType(sessionVariables.getUserType());
            updatePasswordResponse.setStatus(StatusConstants.FAILURE);
            updatePasswordResponse.setMessage(StatusConstants.UPDATE_PASSWORD_UNAUTHORIZED);
            response = Response.ok(updatePasswordResponse).status(Response.Status.FORBIDDEN).build();
        }
        return response;
    }

    public Response validateAuthToken(UriInfo uriInfo, SessionVariables sessionVariables) {
        boolean validAuthToken = sessionVariables.isValidAuthToken();
        User validateAuthTokenResponse = new User();
        Response response;
        if (validAuthToken) {
            String authToken = sessionVariables.getAuthToken();
            MultivaluedMap<String, String> queryParametersMultivaluedMap = uriInfo.getQueryParameters();
            String tokenType = queryParametersMultivaluedMap.getFirst(ApplicationConstants.TOKEN_TYPE);
            String tokenValue = queryParametersMultivaluedMap.getFirst(ApplicationConstants.TOKEN_VALUE);
            if (tokenType != null && tokenValue != null) {
                JSONObject tokenObject = BeanUtil.getToken(tokenType, tokenValue, authToken);
                UserDAOImpl.addNewToken(sessionVariables.getUserId(), tokenObject);
            }

            validateAuthTokenResponse = new UserDAOImpl().fetchProfile(authToken, ApplicationConstants.AUTH_TOKEN);
            validateAuthTokenResponse.setStatus(StatusConstants.SUCCESS);
            validateAuthTokenResponse.setMessage(StatusConstants.AUTH_TOKEN_VALID);
            String userType = sessionVariables.getUserType();
            validateAuthTokenResponse.setUserType(userType);
            Date expiryDate = TimeUtil.getExpiryDate(1);
            String userAgentType = sessionVariables.getUserAgentType();
            if (userAgentType.startsWith(SecurityConstants.APP)) {
                response = Response.ok(validateAuthTokenResponse).status(Response.Status.OK).expires(expiryDate).header(ApplicationConstants.AUTH_TOKEN, authToken).build();
            } else {
                List<NewCookie> validNewCookies = SecurityUtil.generateNewCookies(ApplicationConstants.AUTH_TOKEN, authToken);
                NewCookie newCookieRoot = validNewCookies.get(0);
                NewCookie newCookieSession = validNewCookies.get(1);
                NewCookie newCookieUser = validNewCookies.get(2);
                NewCookie newAuthCookie = SecurityUtil.generateNewCookie(ApplicationConstants.AUTH_TOKEN, authToken);
                response = Response.ok(validateAuthTokenResponse).status(Response.Status.OK).cookie(newCookieRoot, newCookieSession, newCookieUser).expires(expiryDate).header(ApplicationConstants.AUTH_TOKEN, authToken).build();
            }
        } else {
            validateAuthTokenResponse.setStatus(StatusConstants.FAILURE);
            validateAuthTokenResponse.setMessage(StatusConstants.AUTH_TOKEN_INVALID);
            response = Response.ok(validateAuthTokenResponse).status(Response.Status.FORBIDDEN).build();
        }
        return response;
    }

    public Response signInUser(User user, SessionVariables sessionVariables) {
        Response response;
        user = credentialDAO.signInUser(user, sessionVariables.getSessionId());
        String authToken = user.getAuthToken();
        boolean userValidity = user.getValid();
        if (userValidity) {
            user.protectUser();
            user.setStatus(StatusConstants.SUCCESS);
            user.setMessage(StatusConstants.SIGN_IN_SUCCESS);
            List<NewCookie> validNewCookies = SecurityUtil.generateNewCookies(ApplicationConstants.AUTH_TOKEN, authToken);
            NewCookie newCookieRoot = validNewCookies.get(0);
            NewCookie newCookieSession = validNewCookies.get(1);
            NewCookie newCookieUser = validNewCookies.get(2);
            NewCookie newAuthCookie = SecurityUtil.generateNewCookie(ApplicationConstants.AUTH_TOKEN, authToken);
            Date expiryDate = TimeUtil.getExpiryDate(1);
            if (sessionVariables.getUserAgentType().startsWith(SecurityConstants.APP)) {
                response = Response.ok(user).status(Response.Status.OK).expires(expiryDate)
                        .header(ApplicationConstants.AUTH_TOKEN, authToken).build();
            } else {
                response = Response.ok(user).status(Response.Status.OK)
                        .cookie(newCookieRoot, newCookieSession, newCookieUser)
                        .expires(expiryDate).build();
            }
        } else {
            user.protectUser();
            user.setStatus(StatusConstants.FAILURE);
            user.setMessage(StatusConstants.SIGN_IN_FAILURE);
            response = Response.ok(user).status(Response.Status.OK).build();
        }
        return response;
    }

    public Response forgotPassword(User user, SessionVariables sessionVariables) {
        String sessionId = sessionVariables.getSessionId();
        String email = user.getEmail();
        boolean validUserEmail = credentialDAO.validateKeyValue(ApplicationConstants.EMAIL, email);
        BaseResponse forgotPasswordResponse = new BaseResponse();
        if (validUserEmail) {
            String passwordResetURL = SecurityUtil.generatePasswordResetURL(email, sessionId);
            forgotPasswordResponse = EmailUtil.forgotPasswordEmail(email, passwordResetURL);
            forgotPasswordResponse.setStatus(StatusConstants.SUCCESS);
            forgotPasswordResponse.setMessage(StatusConstants.PASSWORD_REQUEST_SUCCESS);
        } else {
            forgotPasswordResponse.setStatus(StatusConstants.SUCCESS);
            forgotPasswordResponse.setMessage(StatusConstants.PASSWORD_REQUEST_SUCCESS);
        }
        return Response.ok(forgotPasswordResponse).status(Response.Status.OK).build();
    }

    public Response getResetPasswordPage(UriInfo uriInfo) {
        String token = uriInfo.getQueryParameters().getFirst(SecurityConstants.TOKEN);
        String page = MarkupConstants.RESET_PASSWORD_PART_1 + token + MarkupConstants.RESET_PASSWORD_PART_2;
        return Response.ok(page, MediaType.TEXT_HTML_TYPE).status(Response.Status.OK).build();
    }

    public Response resetPassword(UriInfo uriInfo, String email, String password) {
        String passwordResetToken = uriInfo.getQueryParameters().getFirst(SecurityConstants.TOKEN);
        Response response;
        boolean validPasswordResetToken = credentialDAO.validatePasswordResetToken(email, passwordResetToken);
        if (validPasswordResetToken) {
            credentialDAO.resetPasswordAndTokens(email, password);
            response = Response.ok(MarkupConstants.RESET_PASSWORD_SUCCESS, MediaType.TEXT_HTML_TYPE)
                    .status(Response.Status.OK).build();
        } else {
            response = Response.ok().status(Response.Status.FORBIDDEN).build();
        }
        return response;
    }

    public Response logoutUser(SessionVariables sessionVariables) {
        String authToken = sessionVariables.getAuthToken();
        boolean validAuthToken = sessionVariables.isValidAuthToken();
        Response response;
        BaseResponse baseResponse = new BaseResponse();
        List<NewCookie> invalidNewCookies = SecurityUtil.invalidateNewCookies(ApplicationConstants.AUTH_TOKEN);
        NewCookie newCookieRoot = invalidNewCookies.get(0);
        NewCookie newCookieSession = invalidNewCookies.get(1);
        NewCookie newCookieUser = invalidNewCookies.get(2);
        Date expiryDate = TimeUtil.getExpiryDate(1);
        if (validAuthToken) {
            baseResponse = credentialDAO.logoutUser(authToken);
            response = Response.ok(baseResponse).status(Response.Status.OK)
                    .cookie(newCookieRoot, newCookieSession, newCookieUser)
                    .expires(expiryDate).header(ApplicationConstants.AUTH_TOKEN, null).build();
        } else {
            baseResponse.setStatus(StatusConstants.FAILURE);
            baseResponse.setMessage(StatusConstants.LOG_OUT_FAILURE);
            response = Response.ok(baseResponse).status(Response.Status.OK)
                    .cookie(newCookieRoot, newCookieSession, newCookieUser)
                    .expires(expiryDate).header(ApplicationConstants.AUTH_TOKEN, null).build();
        }
        return response;
    }
}