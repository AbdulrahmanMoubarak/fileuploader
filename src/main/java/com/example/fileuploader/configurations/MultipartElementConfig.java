package com.example.fileuploader.configurations;

import javax.servlet.MultipartConfigElement;

public class MultipartConfig extends MultipartConfigElement {
    private long maxFileSize = -1;
    public MultipartConfig(String location) {
        super(location);
    }

    public MultipartConfig(String location, long maxFileSize, long maxRequestSize, int fileSizeThreshold) {
        super(location, maxFileSize, maxRequestSize, fileSizeThreshold);
    }

    public MultipartConfig(javax.servlet.annotation.MultipartConfig annotation) {
        super(annotation);
    }

    @Override
    public long getMaxFileSize() {
        if (this.maxFileSize == -1) {
            return super.getMaxFileSize();
        } else {
            return this.maxFileSize;
        }
    }

    public void setMaxFileSize(long size){
        this.maxFileSize = size;
    }
}
