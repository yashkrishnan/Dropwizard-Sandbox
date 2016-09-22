package com.test.sandbox.apis.googlegcm;

import com.test.sandbox.constants.TextConstants;
import io.dropwizard.lifecycle.Managed;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed class for handling GCM pool
 */
public class GCMPoolManager implements Managed {
    public static GenericObjectPool<GoogleGCMUtil> googleGCMAPIGenericObjectPool;
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    public GCMPoolManager() {

    }

    @Override
    public synchronized void start() throws Exception {
        logger.info("Creating GCM Pool");
        GCMPoolFactory gcmPoolFactory = new GCMPoolFactory();
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        googleGCMAPIGenericObjectPool = new GenericObjectPool<>(gcmPoolFactory, genericObjectPoolConfig);
        googleGCMAPIGenericObjectPool.setMaxTotal(1);
    }

    @Override
    public synchronized void stop() throws Exception {
        logger.info("Closing GCM Pool");
        googleGCMAPIGenericObjectPool.close();
    }

    public synchronized GoogleGCMUtil borrowObjectFromPool() {
        GoogleGCMUtil googleGCMUtil = null;
        try {
            logger.info("Borrowing GCM object from pool");
            googleGCMUtil = googleGCMAPIGenericObjectPool.borrowObject();
        } catch (Exception e) {
            logger.error("Error Borrowing GCM object from pool");
            logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
            logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
        }
        return googleGCMUtil;
    }

    public synchronized void returnObjectToPool(GoogleGCMUtil googleGCMUtil) {
        try {
            logger.info("Returning GCM object to pool");
            googleGCMAPIGenericObjectPool.returnObject(googleGCMUtil);
        } catch (Exception e) {
            logger.error("Error returning GCM object to pool");
            logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
            logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
        }
    }
}