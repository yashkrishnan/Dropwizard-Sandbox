package com.example.sandbox.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.example.sandbox.models.genericmodels.responsemodels.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Model POJO class for actions on multiple alerts
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Alerts extends BaseResponse implements Serializable {
    private List<String> alertIds;
    private List<Alert> alerts;
    private Long alertCount;

    public List<String> getAlertIds() {
        return alertIds;
    }

    public void setAlertIds(List<String> alertIds) {
        this.alertIds = alertIds;
    }

    public List<Alert> getAlerts() {
        return alerts;
    }

    public void setAlerts(List<Alert> alerts) {
        this.alerts = alerts;
    }

    public Long getAlertCount() {
        return alertCount;
    }

    public void setAlertCount(Long alertCount) {
        this.alertCount = alertCount;
    }
}
