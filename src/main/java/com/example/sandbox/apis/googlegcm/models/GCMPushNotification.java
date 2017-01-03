package com.example.sandbox.apis.googlegcm.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Model Class for GCM push notification.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GCMPushNotification {
    private String to;
    private List<String> registration_ids;
    private PushNotificationMessage data;
    private Notification notification;

    private Boolean content_available;
    private Boolean delay_while_idle;
    private String collapse_key;
    private Integer time_to_live;
    private String priority;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<String> getRegistration_ids() {
        return registration_ids;
    }

    public void setRegistration_ids(List<String> registration_ids) {
        this.registration_ids = registration_ids;
    }

    public PushNotificationMessage getData() {
        return data;
    }

    public void setData(PushNotificationMessage data) {
        this.data = data;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public Boolean getContent_available() {
        return content_available;
    }

    public void setContent_available(Boolean content_available) {
        this.content_available = content_available;
    }

    public Boolean getDelay_while_idle() {
        return delay_while_idle;
    }

    public void setDelay_while_idle(Boolean delay_while_idle) {
        this.delay_while_idle = delay_while_idle;
    }

    public String getCollapse_key() {
        return collapse_key;
    }

    public void setCollapse_key(String collapse_key) {
        this.collapse_key = collapse_key;
    }

    public Integer getTime_to_live() {
        return time_to_live;
    }

    public void setTime_to_live(Integer time_to_live) {
        this.time_to_live = time_to_live;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
