package com.example.fileuploader.fileupload.controllers;

import com.example.fileuploader.fileupload.configurations.MultipartElementConfig;
import com.example.fileuploader.fileupload.services.FileManagerService;
import com.example.fileuploader.fileupload.utils.FileValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileController {

    @Autowired
    FileManagerService fileManagerService;

    @Autowired
    MultipartElementConfig multipartConfig;

    Logger logger = LoggerFactory.getLogger(FileController.class);

    @GetMapping(path = "/")
    @CrossOrigin()
    public ResponseEntity<?> welcomeMessage() {
        return ResponseEntity.ok("{welcome: hello}");
    }

    @PutMapping(path = "/setMaxSize")
    @CrossOrigin()
    public ResponseEntity<?> setMaxFileSize(@RequestParam String newMaxFileSize) {
        logger.info("new max file size  = " + newMaxFileSize);
        this.multipartConfig.setMaxFileSize(Long.parseLong(newMaxFileSize));
        return ResponseEntity.ok().body("{}");
    }

    @PostMapping(path = "/upload", produces = {MediaType.TEXT_PLAIN_VALUE})
    @CrossOrigin(origins = "*")
    public ResponseEntity<String> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("ticketId") int ticketId,
            @RequestParam("checksum") String originalChecksum
    ) {
        logger.info("file name: " + file.getOriginalFilename());
        logger.info("file size: " + file.getSize());
        logger.info("ticket id: " + ticketId);
        if (new FileValidator().validateFileName(file.getName())) {
            boolean status = fileManagerService.storeFile(file, ticketId, originalChecksum);
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
