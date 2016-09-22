package com.test.sandbox.apis.googlegcm;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory pool for GCM service
 */
public class GCMPoolFactory extends BasePooledObjectFactory<GoogleGCMUtil> {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Override
    public synchronized GoogleGCMUtil create() throws Exception {
        logger.info("Creating GCM object");
        return GoogleGCMUtil.getInstance();
    }

    @Override
    public synchronized PooledObject<GoogleGCMUtil> wrap(GoogleGCMUtil googleGCMUtil) {
        logger.info("Wrap pooled GCM object");
        return new DefaultPooledObject<GoogleGCMUtil>(googleGCMUtil);
    }
}