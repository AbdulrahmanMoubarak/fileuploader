package com.example.fileuploader.ticketing.models;

public class UploadRequestMetadataModel {

    private int userId;
    private float fileSize;
    private String fileName;

    public UploadRequestMetadataModel(int userId, float fileSize, String fileName) {
        this.userId = userId;
        this.fileSize = fileSize;
        this.fileName = fileName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public float getFileSize() {
        return fileSize;
    }

    public void setFileSize(float fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
