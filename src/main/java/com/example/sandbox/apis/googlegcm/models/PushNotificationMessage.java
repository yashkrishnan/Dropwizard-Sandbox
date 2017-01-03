package com.example.sandbox.apis.googlegcm.models;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Model Class for message of push notification.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PushNotificationMessage {
    private String notificationId;
    private String notificationType;
    private String notificationTitle;
    private String notificationMessage;

    private String issuerUserId;
    private String targetUserId;
    private String issuingEntityId;
    private String targetEntityId;

    private String notificationStatus;
    private long newNotifications;
    private long eventTime;

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public String getIssuerUserId() {
        return issuerUserId;
    }

    public void setIssuerUserId(String issuerUserId) {
        this.issuerUserId = issuerUserId;
    }

    public String getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(String targetUserId) {
        this.targetUserId = targetUserId;
    }

    public String getIssuingEntityId() {
        return issuingEntityId;
    }

    public void setIssuingEntityId(String issuingEntityId) {
        this.issuingEntityId = issuingEntityId;
    }

    public String getTargetEntityId() {
        return targetEntityId;
    }

    public void setTargetEntityId(String targetEntityId) {
        this.targetEntityId = targetEntityId;
    }

    public String getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(String notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public long getNewNotifications() {
        return newNotifications;
    }

    public void setNewNotifications(long newNotifications) {
        this.newNotifications = newNotifications;
    }

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }
}
