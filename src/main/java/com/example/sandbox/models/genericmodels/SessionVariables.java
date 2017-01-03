package com.example.sandbox.models.genericmodels;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Model POJO class for session variables of each request to server application through REST api end points in resource classes
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SessionVariables {
    private String sessionId;
    private String requestIP;
    private String userAgentType;
    private String httpAccept;
    private String contentType;
    private String path;
    private String method;

    private String authToken;
    private boolean validAuthToken;
    private String userType;
    private String userId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getRequestIP() {
        return requestIP;
    }

    public void setRequestIP(String requestIP) {
        this.requestIP = requestIP;
    }

    public String getUserAgentType() {
        return userAgentType;
    }

    public void setUserAgentType(String userAgentType) {
        this.userAgentType = userAgentType;
    }

    public String getHttpAccept() {
        return httpAccept;
    }

    public void setHttpAccept(String httpAccept) {
        this.httpAccept = httpAccept;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public boolean isValidAuthToken() {
        return validAuthToken;
    }

    public void setValidAuthToken(boolean validAuthToken) {
        this.validAuthToken = validAuthToken;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
