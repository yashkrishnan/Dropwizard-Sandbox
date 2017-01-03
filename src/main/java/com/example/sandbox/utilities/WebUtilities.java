package com.example.sandbox.utilities;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
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
        try {
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
}
