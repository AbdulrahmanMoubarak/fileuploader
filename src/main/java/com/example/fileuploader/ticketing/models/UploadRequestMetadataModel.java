package com.example.fileuploader.ticketing.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class UploadRequestMetadataModel {
    @Getter
    @Setter
    private int userId;
    @Getter
    @Setter
    private float fileSize;
    @Getter
    @Setter
    private String fileName;
}
