package com.example.fileuploader.fileupload.controllers;

import com.example.fileuploader.filechecksum.services.FileValidationService;
import com.example.fileuploader.fileupload.configurations.MultipartElementConfig;
import com.example.fileuploader.fileupload.services.FileManagerService;
import com.example.fileuploader.fileupload.utils.FileValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;

@RestController
public class FileController {

    @Autowired
    FileManagerService fileService;

    @Autowired
    MultipartElementConfig multipartConfig;

    @Autowired
    ServletContext context;

    Logger logger = LoggerFactory.getLogger(FileController.class);

    @GetMapping(path = "/")
    @CrossOrigin()
    public ResponseEntity<?> welcomeMessage() {
        String absolutePath = context.getRealPath("files");
//        System.out.println("abs path: " + absolutePath);
        return ResponseEntity.ok("{welcome: hello}");
    }

    @PutMapping(path = "/setMaxSize")
    @CrossOrigin()
    public ResponseEntity<?> setMaxFileSize(@RequestParam String size) {
        logger.info("new max file size  = " + size);
        this.multipartConfig.setMaxFileSize(Long.parseLong(size));
        return ResponseEntity.ok().body("{}");
    }

    //TODO: Exception handling
    @PostMapping(path = "/upload", produces = {MediaType.TEXT_PLAIN_VALUE})
    @CrossOrigin(origins = "*")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("ticketId") int ticketId,
            @RequestParam("checksum") String checksum
    ) {
        logger.info("file name: " + file.getOriginalFilename());
        logger.info("file size: " + file.getSize());
        logger.info("ticket id: " + ticketId);
        if (new FileValidator().validateFileName(file.getName())) {
            boolean status = fileService.storeFile(file, ticketId, checksum);
            if (status) {
                return ResponseEntity.ok().body("{}");
            } else {
                return ResponseEntity.internalServerError().body("{message:error ticket expired please refresh}");
            }
        } else {
            return ResponseEntity.badRequest().body("{}");
        }
    }
}
