package com.example.sandbox.io;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory pool for IO service
 */
public class FilePoolFactory extends BasePooledObjectFactory<FileUtil> {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    @Override
    public synchronized FileUtil create() throws Exception {
        logger.info("Creating IO object");
        return FileUtil.getInstance();
    }

    @Override
    public synchronized PooledObject<FileUtil> wrap(FileUtil fileUtil) {
        logger.info("Wrap pooled IO object");
        return new DefaultPooledObject<FileUtil>(fileUtil);
    }
}