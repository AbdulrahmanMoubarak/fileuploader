package com.example.fileuploader.ticketing.services;

import com.example.fileuploader.ticketing.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

@Component
public class CleanupJob {
    @Autowired
    private TicketRepository ticketRepository;

    @Value("${TICKET_EXPIRATION_MINUTES}")
    private int ticketExpiration;


    @Transactional
    @Scheduled(fixedRate = 180000, initialDelay = 0)
    public void removeUnusedTickets() {
        Calendar cal = Calendar.getInstance();
        this.ticketRepository.removeExpiredTickets(cal.getTimeInMillis());
        System.out.println("unused tickets removed at " +cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));
        System.out.println("time in millis " + cal.getTimeInMillis());
    }

    //1659452295249
    //1659452459459

}
