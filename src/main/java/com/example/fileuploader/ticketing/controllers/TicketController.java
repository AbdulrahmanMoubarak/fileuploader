package com.example.fileuploader.ticketing.controllers;

import com.example.fileuploader.ticketing.exceptions.TicketsLimitExceededException;
import com.example.fileuploader.ticketing.models.SystemTicketModel;
import com.example.fileuploader.ticketing.models.TicketStatus;
import com.example.fileuploader.ticketing.models.UploadRequestMetadataModel;
import com.example.fileuploader.ticketing.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TicketController {

    @Autowired
    TicketService ticketService;

    @PostMapping(path = "/requestTicket", produces = {MediaType.APPLICATION_JSON_VALUE})
    @CrossOrigin()
    public ResponseEntity<?> provideTicket(
            @RequestParam("userId") int userId,
            @RequestParam("fileName") String fileName,
            @RequestParam("fileSize") float fileSize
    ) {
        try {
            UploadRequestMetadataModel metadata = new UploadRequestMetadataModel(userId, fileSize, fileName);
            SystemTicketModel ticket = ticketService.generateTicket(metadata);
            System.out.println("ticket generated");
            return ResponseEntity
                    .ok(ticket);
        } catch (TicketsLimitExceededException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.
                    status(HttpStatus.TOO_MANY_REQUESTS).
                    body(String.format("{message:%s}", e.getMessage()));
        }
    }

    @PutMapping(path = "/activateTicket")
    @CrossOrigin()
    public ResponseEntity<?> activateTicket(@RequestParam("ticketId") int ticketId) {
        ticketService.activateTicket(ticketId, TicketStatus.ACTIVE.name());
        System.out.println("Ticket activated");
        return ResponseEntity.ok("{}");
    }
}
