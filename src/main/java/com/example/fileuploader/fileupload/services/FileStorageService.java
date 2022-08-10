package com.example.fileuploader.fileupload.services;

import com.example.fileuploader.fileupload.models.CampaignNumberModel;
import com.example.fileuploader.fileupload.repositories.CampaignNumbersRepository;
import com.example.fileuploader.ticketing.models.TicketStatus;
import com.example.fileuploader.ticketing.services.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileStorageService {

    @Autowired
    CampaignNumbersRepository campaignNumbersRepository;

    @Autowired
    TicketService ticketService;

    Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    @Async
    public void storeCampaignNumbers(File targetFile, int userId, int ticketId) {
        try {
            logger.info("Started record storage service for file: " + targetFile.getName() + "with ticket id: " + ticketId);
            List<CampaignNumberModel> records = new ArrayList<>();
            ticketService.setTicketStatus(ticketId, TicketStatus.STORING);

            //get stream reader for the file.
            FileInputStream targetFileInputStream = new FileInputStream(targetFile);
            InputStreamReader targetFileInputStreamReader = new InputStreamReader(targetFileInputStream);
            BufferedReader targetFileBufferReader = new BufferedReader(targetFileInputStreamReader);

            //save records.
            while (targetFileBufferReader.ready()) {
                String record = targetFileBufferReader.readLine();
                if (!records.contains(new CampaignNumberModel(userId, record, ticketId)))
                    records.add(new CampaignNumberModel(userId, record, ticketId));
            }
            campaignNumbersRepository.saveAll(records);
            logger.info("Campaign storage succeeded for file: " + targetFile.getName() + "with ticket id: " + ticketId);
            ticketService.setTicketStatus(ticketId, TicketStatus.STORED);
            targetFileBufferReader.close();
            targetFileInputStreamReader.close();
            targetFileInputStream.close();
            targetFile.delete();
        } catch (Exception e) {
            logger.error(e.getMessage());
            ticketService.setTicketStatus(ticketId, TicketStatus.SERVER_ERROR);
            logger.error("Cannot Store Data for file: " + targetFile.getName() + "with ticket id: " + ticketId);
        }
    }
}
