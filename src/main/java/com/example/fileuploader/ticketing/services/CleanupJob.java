package com.example.fileuploader.ticketing.services;

import com.example.fileuploader.ticketing.models.TicketStatus;
import com.example.fileuploader.ticketing.repositories.TicketRepository;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

@Component
public class CleanupJob {
    @Autowired
    private TicketRepository ticketRepository;

    @Value("${TICKET_EXPIRATION_MINUTES}")
    private int ticketExpiration;

    @Value("${fileuploader_dir}")
    private String file_dir;

    @Transactional
    @Scheduled(fixedRate = 180000, initialDelay = 0)
    public void removeExpiredItems() {
        Calendar cal = Calendar.getInstance();
        this.ticketRepository.removeExpiredTickets(cal.getTimeInMillis(), TicketStatus.CREATED);
        System.out.println("unused tickets items at " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));
        System.out.println("time in millis " + cal.getTimeInMillis());
    }
}
