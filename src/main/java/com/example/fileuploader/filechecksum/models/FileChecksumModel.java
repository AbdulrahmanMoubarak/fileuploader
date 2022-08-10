package com.example.fileuploader.filechecksum.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FILES_CHECKSUM")
public class FileChecksumModel {

    @Id
    private Integer ticketId;
    private String checksum;

    public FileChecksumModel(int ticketId ,String checksum) {
        this.checksum = checksum;
        this.ticketId = ticketId;
    }

    public FileChecksumModel() {
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer id) {
        this.ticketId = id;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }
}
