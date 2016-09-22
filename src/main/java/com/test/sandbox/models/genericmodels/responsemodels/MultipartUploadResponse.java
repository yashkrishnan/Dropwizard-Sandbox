package com.test.sandbox.models.genericmodels.responsemodels;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Model POJO class for multipart upload response
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MultipartUploadResponse extends BaseResponse {
    private String uploadedFileLocation;

    public String getUploadedFileLocation() {
        return uploadedFileLocation;
    }

    public void setUploadedFileLocation(String uploadedFileLocation) {
        this.uploadedFileLocation = uploadedFileLocation;
    }
}
