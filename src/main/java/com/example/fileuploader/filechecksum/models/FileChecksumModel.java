package com.example.fileuploader.filechecksum.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FILES_CHECKSUM")
public class FileChecksumModel {

    @Id
    @GeneratedValue
    private Integer id;
    private String checksum;

    public FileChecksumModel(String checksum) {
        this.checksum = checksum;
    }

    public FileChecksumModel() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }
}
