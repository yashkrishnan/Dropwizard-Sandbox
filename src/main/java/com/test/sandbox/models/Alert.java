package com.test.sandbox.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.test.sandbox.models.genericmodels.responsemodels.BaseResponse;

import java.io.Serializable;
import java.util.Map;

/**
 * Model POJO class for alert
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Alert extends BaseResponse implements Serializable {
    private String alertId;
    private Map<String, String> alertRecords;
    private double value;

    private transient Long startTime;
    private transient Long endTime;

    public String getAlertId() {
        return alertId;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public Map<String, String> getAlertRecords() {
        return alertRecords;
    }

    public void setAlertRecords(Map<String, String> alertRecords) {
        this.alertRecords = alertRecords;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}
