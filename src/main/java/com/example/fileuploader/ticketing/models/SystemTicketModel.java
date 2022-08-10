package com.example.fileuploader.ticketing.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "UPLOAD_TICKETS")
@AllArgsConstructor
@RequiredArgsConstructor
public class SystemTicketModel {
    @Id
    @GeneratedValue
    @Getter
    @Setter
    private Integer ticketId;
    @Getter
    @Setter
    @NonNull
    private long userId;
    @Getter
    @Setter
    @NonNull
    private float size;
    @Getter
    @Setter
    @NonNull
    private String fileName;
    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    @NonNull
    private TicketStatus status = TicketStatus.CREATED;
    @Getter
    @Setter
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
