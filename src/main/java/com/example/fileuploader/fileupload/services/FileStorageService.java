package com.example.fileuploader.fileupload.services;

import com.example.fileuploader.fileupload.models.CampaignNumberModel;
import com.example.fileuploader.fileupload.repositories.CampaignNumberRepository;
import com.example.fileuploader.ticketing.models.TicketStatus;
import com.example.fileuploader.ticketing.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class FileStorageService {

    @Autowired
    CampaignNumberRepository campaignRepo;

    @Autowired
    TicketService ticketService;

    @Async
    public void storeCampaignNumbers(File file, int userId, int ticketId){
        try{
            System.out.println("Began record storage service");
            List<CampaignNumberModel> records = new ArrayList<>();
            ticketService.setTicketStatus(ticketId, TicketStatus.STORING);
            //get stream reader for the file.
            FileInputStream in = new FileInputStream(file);
            InputStreamReader inReader = new InputStreamReader(in);
            BufferedReader reader = new BufferedReader(inReader);
            //save records.
            while(reader.ready()){
                String record = reader.readLine();
                records.add(new CampaignNumberModel(userId, record));
            }
//            System.out.println("finished filling records list");
            campaignRepo.saveAll(records);
            System.out.println("Campaign storage succeeded");
            ticketService.setTicketStatus(ticketId, TicketStatus.SUCCEEDED);
            reader.close();
            inReader.close();
            in.close();
            file.delete();
            System.out.println("file deleted");
//            this.ticketService.removeTicketById(ticketId);
//            System.out.println("ticket removed");
        }catch (Exception e){
            System.out.println(e.getMessage());
            ticketService.setTicketStatus(ticketId, TicketStatus.SERVER_ERROR);
            System.out.println("Cannot Store Data");
        }
    }
}
