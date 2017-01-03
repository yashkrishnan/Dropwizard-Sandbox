package com.example.sandbox.apis.googlegeocoding.models;

import java.io.Serializable;

/**
 * Created by Crystal on 25/05/2015.
 */
public class Result implements Serializable {
    private GeocodeContent[] results;
    private String status;
    private boolean exclude_from_slo;
    private String error_message;

    public boolean isExclude_from_slo() {
        return exclude_from_slo;
    }

    public void setExclude_from_slo(boolean exclude_from_slo) {
        this.exclude_from_slo = exclude_from_slo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public GeocodeContent[] getResults() {
        return results;
    }

    public void setResults(GeocodeContent[] results) {
        this.results = results;
    }

    public String getError_message() {
        return error_message;
    }

    public void setError_message(String error_message) {
        this.error_message = error_message;
    }
}