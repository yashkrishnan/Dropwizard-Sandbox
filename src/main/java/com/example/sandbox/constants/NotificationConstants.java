package com.example.sandbox.constants;

/**
 * Collected constants of notification actions. All members of this class are immutable.
 */
public final class NotificationConstants {

    public static final String APNS_ALERT_DEFAULT = "default";

    public static final String TAG_APNS = "APNS";
    public static final String TAG_GCM = "GCM";
    public static final String TAG_SOCKET = "SOCKET";

    public static final String NOTIFICATIONS_TYPE_NEW_ALERT = "New Alert";

    public static final String NOTIFICATIONS_TITLE_NEW_ALERT = "New alerts for you";

    public static final String NOTIFICATIONS_MESSAGE_NEW_ALERT = " reportedly contains %d alert(s)";

    public static final String NOTIFICATIONS_STATUS_NEW = "new";
    public static final String NOTIFICATIONS_STATUS_OLD = "old";
    public static final String NOTIFICATIONS_STATUS_PROCESSED = "processed";
}