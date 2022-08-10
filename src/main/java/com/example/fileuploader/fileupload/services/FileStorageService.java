package com.example.fileuploader.fileupload.services;

import com.example.fileuploader.fileupload.models.CampaignNumberModel;
import com.example.fileuploader.fileupload.repositories.CampaignNumberRepository;
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
    CampaignNumberRepository campaignRepo;

    @Autowired
    TicketService ticketService;

    Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    @Async
    public void storeCampaignNumbers(File file, int userId, int ticketId) {
        try {
            logger.info("Began record storage service for file: " + file.getName() + "with ticket id: " + ticketId);
            List<CampaignNumberModel> records = new ArrayList<>();
            ticketService.setTicketStatus(ticketId, TicketStatus.STORING);
            //get stream reader for the file.
            FileInputStream in = new FileInputStream(file);
            InputStreamReader inReader = new InputStreamReader(in);
            BufferedReader reader = new BufferedReader(inReader);

            //save records.
            while (reader.ready()) {
                String record = reader.readLine();
                if (!records.contains(new CampaignNumberModel(userId, record, ticketId)))
                    records.add(new CampaignNumberModel(userId, record, ticketId));
            }
            campaignRepo.saveAll(records);
            logger.info("Campaign storage succeeded for file: " + file.getName() + "with ticket id: " + ticketId);
            ticketService.setTicketStatus(ticketId, TicketStatus.STORED);
            reader.close();
            inReader.close();
            in.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
            ticketService.setTicketStatus(ticketId, TicketStatus.SERVER_ERROR);
            logger.error("Cannot Store Data for file: " + file.getName() + "with ticket id: " + ticketId);
        }
    }
}
