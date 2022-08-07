package com.example.fileuploader.fileupload.services;

import com.example.fileuploader.filechecksum.services.FileValidationService;
import com.example.fileuploader.ticketing.models.SystemTicketModel;
import com.example.fileuploader.ticketing.services.TicketService;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutionException;

@Service
public class FileManagerService {

    @Autowired
    TicketService ticketService;
    @Autowired
    FileValidationService validationService;
    @Autowired
    FileStorageService campaignService;

    @Value("${fileuploader_dir}")
    private String fileUploadPath;
    public boolean storeFile(MultipartFile file, int ticketId, String checksum){
        SystemTicketModel userTicket = ticketService.getTicketById(ticketId);
        int userId = (int) userTicket.getUserId();
        if(userTicket != null) {
            String filename = file.getOriginalFilename();
            try {
                File newFile = new File(fileUploadPath + filename);
                file.transferTo(newFile);
                System.out.println("File Transferred");
                if(validationService.fileCorrupted(newFile, checksum)){
                    newFile.delete();
                    return false;
                }
                if(validationService.fileChecksumExists(newFile)){
                    newFile.delete();
                    System.out.println("File deleted");
                    return false;
                } else {
                    campaignService.storeCampaignNumbers(newFile, userId, ticketId);
                }
            } catch (IOException e) {
                return false;
            }
            return true;
        } else{
            System.out.println("Ticket not found!!");
            return false;
        }
    }
}
