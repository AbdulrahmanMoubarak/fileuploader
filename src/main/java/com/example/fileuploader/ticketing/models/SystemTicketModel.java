package com.example.fileuploader.ticketing.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SYSTEM_TICKETS")
public class SystemTicketModel {

    @Id
    private int user_id;
    private int size;

    private String file_name;

    public SystemTicketModel(int user_id, int size, String file_name) {
        this.user_id = user_id;
        this.size = size;
        this.file_name = file_name;
    }

    public SystemTicketModel() {

    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
