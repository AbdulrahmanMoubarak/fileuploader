package com.example.fileuploader.ticketing.models;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UploadRequestMetadataModel {
    
    
    private int userId;
    
    
    private float fileSize;
    
    
    private String fileName;
}
