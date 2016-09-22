package com.test.sandbox.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * Operator to handle IO operations
 */
public class FileOperator {
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    public synchronized void writeFile(InputStream uploadedInputStream, String uploadedFileLocation) {
        FilePoolManager filePoolManager = new FilePoolManager();
        FileUtil fileUtil = filePoolManager.borrowObjectFromPool();
        logger.info("Writing file");
        fileUtil.writeFile(uploadedInputStream, uploadedFileLocation);
        filePoolManager.returnObjectToPool(fileUtil);
    }
}
