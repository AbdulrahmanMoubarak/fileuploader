package com.example.fileuploader.controllers;

import com.example.fileuploader.configurations.MultipartElementConfig;
import com.example.fileuploader.services.FileManagerService;
import com.example.fileuploader.utils.FileValidator;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {

    @Autowired
    FileManagerService fileService;

    @Autowired
    MultipartElementConfig multipartConfig;

    @GetMapping(path = "/")
    public ResponseEntity<?> welcomeMessage(){
        return ResponseEntity.ok("{welcome: hello}");
    }

    @PutMapping(path = "/setMaxSize")
    //@CrossOrigin(origins = {"http://localhost:4200", "https://angular-file-uploader.herokuapp.com"})
    public ResponseEntity<?> setMaxFileSize(@RequestParam String size){
        System.out.println("new max file size  = " + size);
        this.multipartConfig.setMaxFileSize(Long.parseLong(size));
        return ResponseEntity.ok().headers(getResponseHeaders()).body("{}");
    }

    //TODO: Exception handling
    @PostMapping(path = "/upload", produces = {MediaType.TEXT_PLAIN_VALUE})
    @CrossOrigin(origins = "*")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        System.out.println("file name: " + file.getOriginalFilename());
        System.out.println("file size: " + file.getSize());
        System.out.println("max file size: " + this.multipartConfig.getMaxFileSize());
        if(new FileValidator().validateFileName(file.getName())) {
            boolean status = fileService.storeFile(file);
            if (status) {
                return ResponseEntity.ok().body("{}");
            } else {
                return ResponseEntity.internalServerError().body("{}");
            }
        } else{
            return ResponseEntity.badRequest().body("{}");
        }
    }

    private HttpHeaders getResponseHeaders(){
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Allow-Origin", "*");
        responseHeaders.set("Access-Control-Allow-Headers", "Origin, X-Requested, Content-Type, Accept Authorization");
        responseHeaders.set("Access-Control-Allow-Methods", "POST, PUT, PATCH, GET, DELETE");
        responseHeaders.set("Access-Control-Allow-Credentials", "true");
        return responseHeaders;
    }
}
