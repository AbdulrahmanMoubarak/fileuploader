package com.example.fileuploader.fileupload.configurations;

import javax.servlet.MultipartConfigElement;

public class MultipartElementConfig extends MultipartConfigElement {
    private long maxFileSize = -1;
    public MultipartElementConfig(String location) {
        super(location);
    }

    public MultipartElementConfig(String location, long maxFileSize, long maxRequestSize, int fileSizeThreshold) {
        super(location, maxFileSize, maxRequestSize, fileSizeThreshold);
    }

    public MultipartElementConfig(javax.servlet.annotation.MultipartConfig annotation) {
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
        this.maxFileSize = size*1048576;
    }
}
