package com.example.fileuploader.ticketing.services;

import com.example.fileuploader.fileupload.configurations.MultipartElementConfig;
import com.example.fileuploader.ticketing.exceptions.TicketsLimitExceededException;
import com.example.fileuploader.ticketing.models.SystemTicketModel;
import com.example.fileuploader.ticketing.models.TicketStatus;
import com.example.fileuploader.ticketing.models.UploadRequestMetadataModel;
import com.example.fileuploader.ticketing.repositories.TicketRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private MultipartElementConfig multipartConfig;

    @Value("${MAX_USER_TICKETS}")
    private int maxUserTickets;

    @Value("${MAX_TOTAL_TICKETS}")
    private int maxSystemTotalTickets;

    Logger logger = LoggerFactory.getLogger(TicketService.class);


    public SystemTicketModel generateTicket(UploadRequestMetadataModel requestMetadata) throws TicketsLimitExceededException {
        if (multipartConfig.getMaxFileSize() > requestMetadata.getFileSize()) {
            SystemTicketModel unusedTicket = getUnusedTickets(requestMetadata.getUserId());
            if (unusedTicket == null) {
                if (checkTicketAvailability(requestMetadata.getUserId())) {
                    return ticketRepository.save(
                            new SystemTicketModel(
                                    requestMetadata.getUserId(),
                                    requestMetadata.getFileSize(),
                                    requestMetadata.getFileName(),
                                    Calendar.getInstance().getTimeInMillis()
                            )
                    );
                } else {
                    throw new TicketsLimitExceededException();
                }
            } else {
                return unusedTicket;
            }
        } else {
            return new SystemTicketModel();
        }
    }

    public SystemTicketModel getTicketById(int ticketId) {
        return ticketRepository.findByTicketId(ticketId);
    }

    private boolean checkTicketAvailability(long userId) {
        long totalUserTickets = ticketRepository.getUserActiveTickets(userId);
        long totalSystemTickets = getActiveTicketsCount();
        return totalUserTickets < maxUserTickets && totalSystemTickets < maxSystemTotalTickets;
    }

    private SystemTicketModel getUnusedTickets(long userId) {
        return ticketRepository.findByUserIdAndStatus(userId, TicketStatus.CREATED);
    }

    @Transactional
    public void setTicketStatus(int ticketId, TicketStatus ticketStatus) {
        logger.info("Ticket with id: " + ticketId + " changed status to " + ticketStatus.name());
        ticketRepository.updateTicketStatus(ticketStatus, ticketId);
    }

    private long getActiveTicketsCount() {
        return ticketRepository.getAllActiveTickets();
    }
}
