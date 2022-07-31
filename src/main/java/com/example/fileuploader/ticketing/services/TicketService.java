package com.example.fileuploader.ticketing.services;

import com.example.fileuploader.ticketing.exceptions.TicketsLimitExceededException;
import com.example.fileuploader.ticketing.models.SystemTicketModel;
import com.example.fileuploader.ticketing.models.UploadRequestMetadataModel;
import com.example.fileuploader.ticketing.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Value("${MAX_USER_TICKETS}")
    private int maxUserTickets;

    public SystemTicketModel generateTicket(UploadRequestMetadataModel metadata) throws TicketsLimitExceededException {
        if (checkTicketAvailability(metadata.getUserId())) {
            return ticketRepository.save(
                    new SystemTicketModel(
                            metadata.getUserId(),
                            metadata.getFileSize(),
                            metadata.getFileName()
                    )
            );
        } else {
            throw new TicketsLimitExceededException();
        }
    }

    private boolean checkTicketAvailability(int userId) {
        List<SystemTicketModel> userTickets = ticketRepository.findAllByUserId(userId);
        return userTickets.size() < this.maxUserTickets;
    }
}
