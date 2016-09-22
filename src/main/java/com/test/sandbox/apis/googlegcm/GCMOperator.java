package com.test.sandbox.apis.googlegcm;

import com.test.sandbox.apis.googlegcm.models.GCMPushNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Operator to handle GCM communications
 */
public class GCMOperator {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    public synchronized void pushGCMNotifications(List<GCMPushNotification> gcmPushNotifications) {
        GCMPoolManager gcmPoolManager = new GCMPoolManager();
        GoogleGCMUtil googleGCMUtil = gcmPoolManager.borrowObjectFromPool();
        logger.info("Sending GCM");
        googleGCMUtil.pushGCMNotifications(gcmPushNotifications);
        gcmPoolManager.returnObjectToPool(googleGCMUtil);
    }
}
