package com.test.sandbox.apis.googlegcm.models;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Model POJO class for APNS Notification
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Notification {
    private String sound;
    private String priority;
    private String body;
    private String icon;
    private Integer badge;
    private Boolean content_available;
    private String title;

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getBadge() {
        return badge;
    }

    public void setBadge(Integer badge) {
        this.badge = badge;
    }

    public Boolean getContent_available() {
        return content_available;
    }

    public void setContent_available(Boolean content_available) {
        this.content_available = content_available;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
