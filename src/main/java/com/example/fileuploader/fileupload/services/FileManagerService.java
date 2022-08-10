package com.example.fileuploader.fileupload.services;

import com.example.fileuploader.filechecksum.services.FileValidationService;
import com.example.fileuploader.ticketing.models.SystemTicketModel;
import com.example.fileuploader.ticketing.models.TicketStatus;
import com.example.fileuploader.ticketing.services.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileManagerService {

    @Autowired
    TicketService ticketService;
    @Autowired
    FileValidationService fileValidationService;
    @Autowired
    FileStorageService fileStorageService;

    Logger logger = LoggerFactory.getLogger(FileManagerService.class);

    @Value("${fileuploader_dir}")
    private String fileUploadPath;
    public boolean storeFile(MultipartFile uploadedFile, int ticketId, String checksum){
        SystemTicketModel userTicket = ticketService.getTicketById(ticketId);
        int userId = (int) userTicket.getUserId();
        if(userTicket != null) {
            ticketService.setTicketStatus(ticketId, TicketStatus.UPLOADED);
            String filename = uploadedFile.getOriginalFilename();
            try {
                File storageFile = new File(fileUploadPath + filename);
                uploadedFile.transferTo(storageFile);
                logger.info("File "+filename+" Transferred with ticket id: " + ticketId);
                if(fileValidationService.isFileCorrupted(storageFile, checksum)){
                    storageFile.delete();
                    return false;
                }
                if(fileValidationService.fileChecksumExists(storageFile, ticketId)){
                    storageFile.delete();
                    logger.warn("Checksum already exists for file "+filename+" with ticket id: " + ticketId);
                    logger.warn("File "+filename+" Deleted with ticket id: " + ticketId);
                    return false;
                } else {
                    fileStorageService.storeCampaignNumbers(storageFile, userId, ticketId);
                }
            } catch (IOException e) {
                return false;
            }
            return true;
        } else{
            logger.error("Ticket with id: "+ticketId+" not found!!");
            return false;
        }
    }
}
