package com.example.fileuploader.ticketing.services;

import com.example.fileuploader.ticketing.models.TicketStatus;
import com.example.fileuploader.ticketing.repositories.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

@Component
public class CleanupJob {
    Logger logger = LoggerFactory.getLogger(CleanupJob.class);
    @Autowired
    private TicketRepository ticketRepository;

    @Value("${TICKET_EXPIRATION_TIME_IN_MINUTES}")
    int ticketExpirationTimeInMinutes;

    @Transactional
    @Scheduled(fixedRate = 180000, initialDelay = 0)
    public void updateTicketStatus() {
        Calendar cal = Calendar.getInstance();
        this.ticketRepository.updateTicketStatusToExpired(TicketStatus.CREATED, ticketExpirationTimeInMinutes, cal.getTimeInMillis());
        this.ticketRepository.updateStuckUploadingTickets(TicketStatus.UPLOADING, ticketExpirationTimeInMinutes,cal.getTimeInMillis());
        logger.info("updated upload and created tickets statuses at " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE));
    }
}
