package com.example.fileuploader.services;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileManagerService {

    @Value("${fileuploader_dir}")
    private String fileUploadPath;
    public boolean storeFile(MultipartFile file){
        String filename = file.getOriginalFilename();
        try {
            file.transferTo(new File(fileUploadPath+filename));
            return true;
        } catch (SizeLimitExceededException e){
            System.out.println(e.getMessage());
            return false;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
