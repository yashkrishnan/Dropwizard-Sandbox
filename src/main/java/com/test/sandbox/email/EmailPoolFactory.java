package com.test.sandbox.email;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory pool for email service
 */
public class EmailPoolFactory extends BasePooledObjectFactory<Email> {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Override
    public synchronized Email create() throws Exception {
        logger.info("Creating Email object");
        return new Email();
    }

    @Override
    public synchronized PooledObject<Email> wrap(Email email) {
        logger.info("Wrap pooled Email object");
        return new DefaultPooledObject<Email>(email);
    }
}