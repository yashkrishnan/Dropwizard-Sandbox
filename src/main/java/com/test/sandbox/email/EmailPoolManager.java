package com.test.sandbox.email;

import com.test.sandbox.constants.TextConstants;
import io.dropwizard.lifecycle.Managed;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed class for handling Email pool
 */
public class EmailPoolManager implements Managed {
    public static GenericObjectPool<Email> emailGenericObjectPool;
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    public EmailPoolManager() {

    }

    @Override
    public synchronized void start() throws Exception {
        logger.info("Creating Email Pool");
        EmailPoolFactory emailPoolFactory = new EmailPoolFactory();
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        emailGenericObjectPool = new GenericObjectPool<>(emailPoolFactory, genericObjectPoolConfig);
        emailGenericObjectPool.setMaxTotal(1);
    }

    @Override
    public synchronized void stop() throws Exception {
        logger.info("Closing Email Pool");
        emailGenericObjectPool.close();
    }

    public synchronized Email borrowObjectFromPool() {
        Email email = null;
        try {
            logger.info("Borrowing email object from pool");
            email = emailGenericObjectPool.borrowObject();
        } catch (Exception e) {
            logger.error("Error Borrowing email object from pool");
            logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
            logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
        }
        return email;
    }

    public synchronized void returnObjectToPool(Email email) {
        try {
            logger.info("Returning email object to pool");
            emailGenericObjectPool.returnObject(email);
        } catch (Exception e) {
            logger.error("Error returning email object to pool");
            logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
            logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
        }
    }

}
