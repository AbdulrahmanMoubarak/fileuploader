package com.example.fileuploader.filechecksum.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FILES_CHECKSUM")
@AllArgsConstructor
@NoArgsConstructor
public class FileChecksumModel {
    @Id
    @Getter @Setter private Integer ticketId;
    @Getter @Setter private String fileChecksum;
}
