package com.example.fileuploader.ticketing.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "UPLOAD_TICKETS")
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class SystemTicketModel {
    @Id
    @GeneratedValue
    private Integer ticketId;


    @NonNull
    private long userId;


    @NonNull
    private float size;


    @NonNull
    private String fileName;
    @Enumerated(EnumType.STRING)


    @NonNull
    private TicketStatus status = TicketStatus.CREATED;


    @NonNull
    private long timestamp;

    public SystemTicketModel() {
        this.ticketId = -1;
        this.status = TicketStatus.UPLOAD_ERROR;
        this.userId = -1;
        this.size = -1;
        this.fileName = "";
        this.timestamp = 0;
    }
}
