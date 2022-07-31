package com.example.fileuploader.ticketing.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SYSTEM_TICKETS")
public class SystemTicketModel {

    @Id
    private int ticketId;
    private int userId;
    private int size;
    private String fileName;

    private boolean used;

    public SystemTicketModel(int userId, int size, String fileName) {
        this.userId = userId;
        this.size = size;
        this.fileName = fileName;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticket_id) {
        this.ticketId = ticket_id;
    }

    public SystemTicketModel() {

    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String file_name) {
        this.fileName = file_name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int user_id) {
        this.userId = user_id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
