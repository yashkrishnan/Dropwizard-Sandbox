package com.example.sandbox.io;

import com.example.sandbox.constants.TextConstants;
import io.dropwizard.lifecycle.Managed;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Managed class for handling File pool
 */
public class FilePoolManager implements Managed {
    public static GenericObjectPool<FileUtil> fileUtilGenericObjectPool;
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    public FilePoolManager() {

    }

    @Override
    public synchronized void start() throws Exception {
        logger.info("Creating IO Pool");
        FilePoolFactory filePoolFactory = new FilePoolFactory();
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        fileUtilGenericObjectPool = new GenericObjectPool<FileUtil>(filePoolFactory, genericObjectPoolConfig);
        fileUtilGenericObjectPool.setMaxTotal(1);
    }

    @Override
    public synchronized void stop() throws Exception {
        logger.info("Closing IO Pool");
        fileUtilGenericObjectPool.close();
    }

    public synchronized FileUtil borrowObjectFromPool() {
        FileUtil fileUtil = null;
        try {
            logger.info("Borrowing IO object from pool");
            fileUtil = fileUtilGenericObjectPool.borrowObject();
        } catch (Exception e) {
            logger.error("Error Borrowing IO object from pool");
            logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
            logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
        }
        return fileUtil;
    }

    public synchronized void returnObjectToPool(FileUtil fileUtil) {
        try {
            logger.info("Returning IO object to pool");
            fileUtilGenericObjectPool.returnObject(fileUtil);
        } catch (Exception e) {
            logger.error("Error returning IO object to pool");
            logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
            logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
        }
    }

}