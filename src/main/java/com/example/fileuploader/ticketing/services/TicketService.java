package com.example.fileuploader.ticketing.services;

import com.example.fileuploader.configurations.MultipartElementConfig;
import com.example.fileuploader.ticketing.exceptions.TicketsLimitExceededException;
import com.example.fileuploader.ticketing.models.SystemTicketModel;
import com.example.fileuploader.ticketing.models.UploadRequestMetadataModel;
import com.example.fileuploader.ticketing.models.UserModel;
import com.example.fileuploader.ticketing.repositories.TicketRepository;
import com.example.fileuploader.ticketing.repositories.UserRepository;
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
    private int maxTotalTickets;


    public SystemTicketModel generateTicket(UploadRequestMetadataModel metadata) throws TicketsLimitExceededException {
        if (multipartConfig.getMaxFileSize() > metadata.getFileSize()) {
            SystemTicketModel unusedTicket = getUnusedTickets(metadata.getUserId());
            if (unusedTicket == null) {
                if (checkTicketAvailability(metadata.getUserId())) {
                    return ticketRepository.save(
                            new SystemTicketModel(
                                    metadata.getUserId(),
                                    metadata.getFileSize(),
                                    metadata.getFileName(),
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

    @Transactional
    public void removeTicketById(int ticketId) {
        this.ticketRepository.removeByTicketId(ticketId);
    }

    public SystemTicketModel getTicketById(int ticketId) {
        return this.ticketRepository.findByTicketId(ticketId);
    }

    @Transactional
    public void removeUnusedTickets() {
        this.ticketRepository.removeAllByUsed(false);
    }

    private boolean checkTicketAvailability(long userId) {
        List<SystemTicketModel> userTickets = ticketRepository.findAllByUserId(userId);
        long total = ticketRepository.count();
        return userTickets.size() < this.maxUserTickets && total < maxTotalTickets;
    }

    private SystemTicketModel getUnusedTickets(long userId) {
        return ticketRepository.findByUserIdAndUsed(userId, false);
    }
}
