package com.example.fileuploader.ticketing.models;

import javax.persistence.*;

@Entity
@Table(name = "UPLOAD_TICKETS")
public class SystemTicketModel {

    @Id
    @GeneratedValue
    private Integer ticketId;
    private long userId;
    private float size;
    private String fileName;
    private boolean used;
    private long timestamp;

    public SystemTicketModel(long userId, float size, String fileName, long timestamp) {
        this.used = false;
        this.userId = userId;
        this.size = size;
        this.fileName = fileName;
        this.timestamp = timestamp;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticket_id) {
        this.ticketId = ticket_id;
    }

    public SystemTicketModel() {
        this.ticketId = -1;
        this.used = false;
        this.userId = -1;
        this.size = -1;
        this.fileName = "";
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String file_name) {
        this.fileName = file_name;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long user_id) {
        this.userId = user_id;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
