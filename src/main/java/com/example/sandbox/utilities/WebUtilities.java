package com.example.sandbox.utilities;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;

/**
 * Utility class for web based transactions
 */
public class WebUtilities {
    private static Logger logger = LoggerFactory.getLogger(WebUtilities.class);

    private String requestURL;
    private String requestPayload;
    private HashMap<String, String> headers;
    private HashMap<String, Object> queryParams;
    private HashMap<String, Object> fieldParams;
    private long connectionTimeout = 60000;
    private long socketTimeout = 60000;

    public WebUtilities(WebUtilitiesBuilder webUtilitiesBuilder) {
        this.requestURL = webUtilitiesBuilder.requestURL;
        this.requestPayload = webUtilitiesBuilder.requestPayload;
        this.headers = webUtilitiesBuilder.headers;
        this.queryParams = webUtilitiesBuilder.queryParams;
        this.fieldParams = webUtilitiesBuilder.fieldParams;
        if (webUtilitiesBuilder.connectionTimeout != 0) {
            this.connectionTimeout = webUtilitiesBuilder.connectionTimeout;
        }
        if (webUtilitiesBuilder.socketTimeout != 0) {
            this.socketTimeout = webUtilitiesBuilder.socketTimeout;
        }
    }

    public WebUtilities() {
    }

    public static Logger getLogger() {
        return logger;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public String getRequestPayload() {
        return requestPayload;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public HashMap<String, Object> getQueryParams() {
        return queryParams;
    }

    public HashMap<String, Object> getFieldParams() {
        return fieldParams;
    }

    public long getConnectionTimeout() {
        return connectionTimeout;
    }

    public long getSocketTimeout() {
        return socketTimeout;
    }

    /**
     * Do POST with response.
     *
     * @return the response
     */
    public String doPostResponse() {
        HttpResponse<String> httpResponse = null;
        Unirest.setTimeouts(this.connectionTimeout, this.socketTimeout);
        HttpRequestWithBody httpRequestWithBody = Unirest.post(this.requestURL);
        if (this.headers != null) {
            httpRequestWithBody.headers(this.headers);
        }
        if (this.queryParams != null) {
            httpRequestWithBody.queryString(this.queryParams);
        }
        if (this.fieldParams != null) {
            httpRequestWithBody.fields(this.fieldParams);
        }
        if (this.requestPayload != null) {
            httpRequestWithBody.body(this.requestPayload);
        }
        try {
            httpResponse = httpRequestWithBody.asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        } finally {
            try {
                Unirest.shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        assert httpResponse != null;
        return httpResponse.getBody();
    }

    /**
     * Do PUT with response.
     *
     * @return the response
     */
    public String doPutResponse() {
        HttpResponse<String> httpResponse = null;
        Unirest.setTimeouts(this.connectionTimeout, this.socketTimeout);
        HttpRequestWithBody httpRequestWithBody = Unirest.put(this.requestURL);
        if (this.headers != null) {
            httpRequestWithBody.headers(this.headers);
        }
        if (this.queryParams != null) {
            httpRequestWithBody.queryString(this.queryParams);
        }
        if (this.fieldParams != null) {
            httpRequestWithBody.fields(this.fieldParams);
        }
        if (this.requestPayload != null) {
            httpRequestWithBody.body(this.requestPayload);
        }
        try {
            httpResponse = httpRequestWithBody.asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        } finally {
            try {
                Unirest.shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        assert httpResponse != null;
        return httpResponse.getBody();
    }

    /**
     * Do DELETE with response.
     *
     * @return the response
     */
    public String doDeleteResponse() {
        HttpResponse<String> httpResponse = null;
        Unirest.setTimeouts(this.connectionTimeout, this.socketTimeout);
        HttpRequestWithBody httpRequestWithBody = Unirest.delete(this.requestURL);
        if (this.headers != null) {
            httpRequestWithBody.headers(this.headers);
        }
        if (this.queryParams != null) {
            httpRequestWithBody.queryString(this.queryParams);
        }
        if (this.fieldParams != null) {
            httpRequestWithBody.fields(this.fieldParams);
        }
        if (this.requestPayload != null) {
            httpRequestWithBody.body(this.requestPayload);
        }
        try {
            httpResponse = httpRequestWithBody.asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        } finally {
            try {
                Unirest.shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        assert httpResponse != null;
        return httpResponse.getBody();
    }

    /**
     * Do GET with response.
     *
     * @return the response
     */
    public String doGetResponse() {
        HttpResponse<String> httpResponse = null;
        try {
            Unirest.setTimeouts(this.connectionTimeout, this.socketTimeout);
            GetRequest getRequest = Unirest.get(this.requestURL);
            getRequest.headers(this.headers);
            getRequest.queryString(this.queryParams);
            httpResponse = getRequest.asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        } finally {
            try {
                Unirest.shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        assert httpResponse != null;
        return httpResponse.getBody();
    }


    /**
     * Gets post response.
     *
     * @param requestURL     the request url
     * @param requestPayload the request payload
     * @param headers        the headers
     * @param queryParams    the query params
     * @param fieldParams    the field params
     * @return the post response
     */
    public String getPostResponse(String requestURL, String requestPayload, HashMap<String, String> headers, HashMap<String, Object> queryParams, HashMap<String, Object> fieldParams) {
        HttpResponse<String> httpResponse = null;
        Unirest.setTimeouts(60000, 60000);
        HttpRequestWithBody httpRequestWithBody = Unirest.post(requestURL);
        if (headers != null) {
            httpRequestWithBody.headers(headers);
        }
        if (queryParams != null) {
            httpRequestWithBody.queryString(queryParams);
        }
        if (fieldParams != null) {
            httpRequestWithBody.fields(fieldParams);
        }
        if (requestPayload != null) {
            httpRequestWithBody.body(requestPayload);
        }
        try {
            httpResponse = httpRequestWithBody.asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        } finally {
            try {
                Unirest.shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        assert httpResponse != null;
        return httpResponse.getBody();
    }

    /**
     * Gets get response.
     *
     * @param requestURL  the request url
     * @param headers     the headers
     * @param queryParams the query params
     * @return the get response
     */
    public String getGetResponse(String requestURL, HashMap<String, String> headers, HashMap<String, Object> queryParams) {
        HttpResponse<String> httpResponse = null;
        try {
            Unirest.setTimeouts(60000, 60000);
            GetRequest getRequest = Unirest.get(requestURL);
            getRequest.headers(headers);
            getRequest.queryString(queryParams);
            httpResponse = getRequest.asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        } finally {
            try {
                Unirest.shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        assert httpResponse != null;
        return httpResponse.getBody();
    }


    public static class WebUtilitiesBuilder {
        private String requestURL;
        private String requestPayload;
        private HashMap<String, String> headers;
        private HashMap<String, Object> queryParams;
        private HashMap<String, Object> fieldParams;
        private long connectionTimeout = 60000;
        private long socketTimeout = 60000;


        private GetRequest getRequest;
        private HttpRequestWithBody httpRequestWithBody;
        private HttpRequest httpRequest;

        public WebUtilitiesBuilder requestURL(String requestURL) {
            this.requestURL = requestURL;
            return this;
        }

        public WebUtilitiesBuilder requestPayload(String requestPayload) {
            this.requestPayload = requestPayload;
            return this;
        }

        public WebUtilitiesBuilder headers(HashMap<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public WebUtilitiesBuilder queryParams(HashMap<String, Object> queryParams) {
            this.queryParams = queryParams;
            return this;
        }

        public WebUtilitiesBuilder fieldParams(HashMap<String, Object> fieldParams) {
            this.fieldParams = fieldParams;
            return this;
        }

        public WebUtilitiesBuilder connectionTimeout(long connectionTimeout) {
            this.connectionTimeout = connectionTimeout;
            return this;
        }

        public WebUtilitiesBuilder socketTimeout(long socketTimeout) {
            this.socketTimeout = socketTimeout;
            return this;
        }

        public WebUtilities build() {
            WebUtilities WebUtilities = new WebUtilities(this);
            return WebUtilities;
        }

    }
}