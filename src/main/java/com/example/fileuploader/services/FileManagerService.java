package com.example.fileuploader.services;

import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class FileManagerService {

    @Value("${fileuploader_dir}")
    private String fileUploadPath;
    public boolean storeFile(MultipartFile file){
        String filename = file.getOriginalFilename();

        File theFile = new File(fileUploadPath+filename);
        File theDir = new File(fileUploadPath);
        System.out.println(theFile.toPath().toString());
        System.out.println(theDir.toPath().toString());
//            Files.createDirectory(theDir.toPath());
//            Files.createFile(theFile.toPath());
//            file.transferTo(theDir);
        return true;
    }
}
