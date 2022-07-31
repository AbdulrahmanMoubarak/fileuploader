package com.example.fileuploader.ticketing.controllers;

import com.example.fileuploader.ticketing.exceptions.TicketsLimitExceededException;
import com.example.fileuploader.ticketing.models.SystemTicketModel;
import com.example.fileuploader.ticketing.models.UploadRequestMetadataModel;
import com.example.fileuploader.ticketing.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TicketController {

    @Autowired
    TicketService ticketService;

    @GetMapping(path = "/requestTicket")
    @CrossOrigin()
    public ResponseEntity<?> provideTicket(@RequestParam("metadata") UploadRequestMetadataModel metadata) {
        try {
            SystemTicketModel ticket = ticketService.generateTicket(metadata);
            return ResponseEntity
                    .ok(ticket);

        } catch (TicketsLimitExceededException e) {
            return ResponseEntity.
                    status(HttpStatus.TOO_MANY_REQUESTS).
                    body(String.format("{message:%s}", e.getMessage()));
        }
    }
}
