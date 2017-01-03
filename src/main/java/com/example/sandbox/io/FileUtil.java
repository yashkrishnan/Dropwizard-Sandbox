package com.example.sandbox.io;

import com.example.sandbox.configurations.SandboxConfiguration;
import com.example.sandbox.constants.StatusConstants;
import com.example.sandbox.constants.TextConstants;
import com.example.sandbox.models.genericmodels.responsemodels.MultipartUploadResponse;
import org.apache.commons.io.FilenameUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.UUID;


/**
 * Util singleton class for performing file operations
 */
public class FileUtil {
    //create an object of FileUtil
    private static volatile FileUtil fileUtil;
    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    //make the constructor private so that this class cannot be instantiated
    private FileUtil() {

    }

    //Get the only object available
    public synchronized static FileUtil getInstance() {
        synchronized (FileUtil.class) {
            // Double check
            if (fileUtil == null) {
                fileUtil = new FileUtil();
            }
        }
        return fileUtil;
    }


    // save uploaded file to new location
    public synchronized void writeFile(InputStream uploadedInputStream, String uploadedFileLocation) {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(uploadedFileLocation));
            int read = 0;
            byte[] bytes = new byte[8192];

            outputStream = new FileOutputStream(new File(uploadedFileLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            logger.info("\nFile is created : " + uploadedFileLocation);
        } catch (IOException e) {
            logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
            logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
        } finally {
            try {
                uploadedInputStream.close();
                assert outputStream != null;
                outputStream.flush();
                outputStream.close();
                logger.info("\nOutput stream is closed");
            } catch (IOException e) {
                logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
                logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
            }
        }
    }

    public synchronized MultipartUploadResponse multipartUploadHandler(final InputStream inputStream, FormDataContentDisposition formDataContentDisposition, String filePath) {
        MultipartUploadResponse multipartUploadResponse = new MultipartUploadResponse();
        String fileName = formDataContentDisposition.getFileName();
        fileName = fileName.toLowerCase();
        String fileExtension = TextConstants.STRING_PERIOD + FilenameUtils.getExtension(fileName);
        File file = new File(SandboxConfiguration.getResourceDirectory() + filePath);
        try {
            boolean folderExists = file.exists();
            if (!folderExists) {
                boolean folderCreated = file.mkdir();
                if (folderCreated) {
                    logger.info("\nDirectory is created : " + filePath);
                } else {
                    multipartUploadResponse.setStatus(StatusConstants.FAILURE);
                    multipartUploadResponse.setMessage(StatusConstants.UPLOAD_FILE_FAILURE);
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
                        logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
                    }
                    return multipartUploadResponse;
                }
            }
        } catch (SecurityException e) {
            logger.info("\nFailed to create directory : " + filePath);
            logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
            logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
        }
        UUID uuid = UUID.randomUUID();
        final String uploadedFileLocation = file.getAbsolutePath() + File.separator + uuid.toString() + fileExtension;
        new FileOperator().writeFile(inputStream, uploadedFileLocation);
        multipartUploadResponse.setStatus(StatusConstants.SUCCESS);
        multipartUploadResponse.setMessage(StatusConstants.UPLOAD_FILE_SUCCESS);
        multipartUploadResponse.setUploadedFileLocation(filePath + uuid.toString() + fileExtension);
        try {
            inputStream.close();
        } catch (IOException e) {
            logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
            logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
        }
        return multipartUploadResponse;
    }

    public synchronized boolean createFolder(String filePath) {
        boolean status = false;
        File file = new File(SandboxConfiguration.getResourceDirectory() + filePath);
        try {
            boolean folderExists = file.exists();
            if (!folderExists) {
                boolean folderCreated = file.mkdir();
                if (folderCreated) {
                    logger.info("\nDirectory is created : " + filePath);
                    status = true;
                } else {
                    status = false;
                }
            }
        } catch (SecurityException e) {
            logger.info("\nFailed to create directory : " + filePath);
            logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
            logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
        }
        return status;
    }

    public synchronized void deleteFile(String path) {
        try {
            File file = new File(SandboxConfiguration.getResourceDirectory() + path);
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        } catch (SecurityException e) {
            logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
            logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
        }
    }

    public synchronized void writeFileReport(String fileName, String data) {
        try {
            PrintWriter printWriter = new PrintWriter(new File(fileName));
            printWriter.println(data);
            printWriter.close();
        } catch (FileNotFoundException e) {
            logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
            logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
        }
    }

}
